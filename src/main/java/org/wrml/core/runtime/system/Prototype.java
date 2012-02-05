/**
 * Copyright (C) 2011 WRML.org <mark@wrml.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wrml.core.runtime.system;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

import org.wrml.core.Model;
import org.wrml.core.model.schema.Constraint;
import org.wrml.core.model.schema.Field;
import org.wrml.core.model.schema.Link;
import org.wrml.core.model.schema.Schema;
import org.wrml.core.model.schema.Type;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.ModelGraph;
import org.wrml.core.runtime.RuntimeObject;
import org.wrml.core.runtime.TypeSystem;
import org.wrml.core.runtime.bootstrap.FieldNames;
import org.wrml.core.runtime.system.transformer.SystemTransformers;
import org.wrml.core.util.observable.ObservableList;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.util.observable.Observables;
import org.wrml.core.www.MediaType;

/**
 * The runtime instance of the Prototype schema. This model is
 * implemented by
 * hand, as its core logic is always local to the WRML runtime and thus the
 * framework's lazy loading is not needed. If some future version or language
 * wishes to remote prototypes then this class will need to be refactored
 * somehow.
 */
public final class Prototype extends RuntimeObject {

    private final java.lang.reflect.Type _NativeType;
    private MediaType _MediaType;

    private ObservableList<URI> _AllBaseSchemaIds;
    private final ObservableList<Constraint<Schema>> _AllConstraints;
    private final ObservableMap<String, Field> _Fields;

    private final ObservableMap<URI, Link> _LinksByRel;
    private ObservableMap<String, Link> _LinksByName;

    private ObservableMap<String, FieldPrototype> _FieldPrototypes;
    private ObservableMap<String, LinkPrototype> _LinkPrototypes;

    public Prototype(final Context context, final java.lang.reflect.Type nativeType) {
        super(context);

        if (nativeType == null) {
            throw new NullPointerException("Static Interface Class cannot be null");
        }

        _NativeType = nativeType;

        final SortedMap<String, Field> allYourFields = new TreeMap<String, Field>();
        final SortedMap<URI, Link> allYourLinks = new TreeMap<URI, Link>();
        final List<Constraint<Schema>> allYourConstraints = new ArrayList<Constraint<Schema>>();

        _Fields = Observables.observableMap(allYourFields);
        _LinksByRel = Observables.observableMap(allYourLinks);
        _AllConstraints = Observables.observableList(allYourConstraints);

    }

    /**
     * Returns an orderly Map of base schema IDs to all ("recursive") base
     * schemas.
     * 
     * Our own schema is at the base of a schema tree in which it, and
     * (recursively) its children, are allowed to have multiple (and shared)
     * base schemas. This is sound and sane because schemas are interfaces, not
     * implementations.
     * 
     * This method returns all of the base schemas collected together in an
     * order that reflects how WRML views schema inheritance precedence - with
     * respect to overriding/duplicating fields and link formula definitions.
     * This "overriding" occurs whenever a base schema reuses a field name or
     * link formula relation id.
     * 
     * The prototype relies on this order so that it can accurately compute
     * the state of a new model conforming to this prototype's schema.
     * 
     * The order is produced using a row-based tree traversal algorithm that
     * goes from the bottom (our schema) to the top (it stops at the base
     * schema).
     * 
     * The map returned will be ordered "alphabetically", where the letters of
     * the alphabet represent the base schema "node" positions shown below:
     * 
     * <pre>
     * 
     *            [E, F,    G,  H,  I]
     *             \ /       \ / \ /
     *             [A,   B,   C,  D]
     *               \   |    |  /
     *            Prototype's  Schema ($)
     * 
     * $ extends A, B, C, D (, Model)
     * A extends E, F (, Model)
     * B (extends Model)
     * C extends G, H (, Model)
     * D extends H, I (, Model)
     * </pre>
     * 
     * So, effectively:
     * 
     * <pre>
     * $ extends A, B, C, D, E, F, G, H, I (, Model)
     * </pre>
     * 
     * Which is the order of the entries within the returned map, as shown
     * below:
     * 
     * <pre>
     * [(URI-A, A), ... (URI-I, I)]
     * </pre>
     * 
     * <a href="http://en.wikipedia.org/wiki/Breadth-first_search">Wikipedia</a>
     * describes the algorithm better than I can.
     */
    public ObservableList<URI> getAllBaseSchemaIds() {

        if (_AllBaseSchemaIds == null) {

            //
            // The following algorithm performs a breadth first traversal of 
            // the base schemas graph to produce an orderly result.
            //

            /*
             * This is the map that this method will return, wrapped by an
             * ObservableMap. It is an ordered map of (URI) schema id to the
             * schemas
             * themselves.
             * 
             * This ordered map will hold all of our base schemas that are
             * belong to
             * us schema. In other words it maps, by schema id, the base schemas
             * of
             * the schema associated with our own schema id.
             */
            final LinkedList<URI> allYourBase = new LinkedList<URI>();

            /*
             * This queue is used to "process" our schema's base schemas.
             * Processing
             * in this case means collecting all of the base schemas in the
             * order
             * described above. This computation involves a tree traversal of
             * our
             * base schemas.
             */
            final Queue<URI> queue = new LinkedList<URI>();

            /*
             * This map retains the URIs of the schemas that have already been
             * processed, meaning that they've been enqueued once already and
             * should
             * not be processed again. It helps us address the fact that our
             * schema
             * tree might actually be a graph with cycles.
             */
            final HashMap<URI, URI> enqueuedIds = new HashMap<URI, URI>();

            /*
             * Start by enqueueing our blueprint's immediate base schemas.
             */
            final URI schemaId = getSchemaId();
            enqueueBaseSchemas(queue, schemaId, enqueuedIds);

            /*
             * Dequeue the first base schema and start the processing loop.
             */
            URI baseSchemaId = queue.poll();
            while (baseSchemaId != null) {

                /*
                 * Check to see if we have already "processed" the base schema
                 * and
                 * also double check to make sure that the base schema is not
                 * the
                 * blueprint - that would be weird.
                 */
                if (!enqueuedIds.containsKey(baseSchemaId) && !schemaId.equals(baseSchemaId)) {

                    /*
                     * Process the base schema by adding it to our ordered
                     * collection. Enqueue its base schemas too.
                     */
                    allYourBase.add(baseSchemaId);
                    enqueueBaseSchemas(queue, baseSchemaId, enqueuedIds);
                }

                /*
                 * Dequeue the next base schema to prep the next iteration for
                 * processing.
                 */
                baseSchemaId = queue.poll();
            }

            /*
             * Create the ObservableMap wrapper and store it for future
             * reference.
             */

            _AllBaseSchemaIds = Observables.observableList(allYourBase);
        }

        return _AllBaseSchemaIds;
    }

    public ObservableList<Constraint<Schema>> getAllConstraints() {
        return _AllConstraints;
    }

    public FieldPrototype getFieldPrototype(String fieldName) {

        if (fieldName == null) {
            return null;
        }

        FieldPrototype fieldPrototype = null;

        if (_FieldPrototypes == null) {
            _FieldPrototypes = Observables.observableMap(new TreeMap<String, FieldPrototype>());
        }

        if (_FieldPrototypes.containsKey(fieldName)) {
            return _FieldPrototypes.get(fieldName);
        }

        Method fieldMethod = null;
        final Method[] methods = getNativeClass().getMethods();

        if (methods != null) {

            for (final Method method : methods) {

                final String methodName = method.getName();
                String possibleFieldName = null;
                if (methodName.startsWith("get")) {
                    possibleFieldName = methodName.substring(3);
                }
                else if (methodName.startsWith("is")) {
                    possibleFieldName = methodName.substring(2);
                }

                if (possibleFieldName != null) {
                    possibleFieldName = Character.toLowerCase(possibleFieldName.charAt(0))
                            + possibleFieldName.substring(1);

                    if (fieldName.equals(possibleFieldName)) {
                        fieldMethod = method;
                        break;
                    }
                }
            }
        }

        if (fieldMethod == null) {
            _FieldPrototypes.put(fieldName, null);
            return null;
        }

        final Context context = getContext();
        final TypeSystem typeSystem = context.getTypeSystem();
        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final java.lang.reflect.Type nativeType = typeSystem.getNativeReturnType(fieldMethod, _NativeType);
        final Type type = systemTransformers.getNativeTypeToTypeTransformer().aToB(nativeType);

        fieldPrototype = new FieldPrototype(getContext(), fieldName, type, nativeType);

        _FieldPrototypes.put(fieldName, fieldPrototype);

        return fieldPrototype;
    }

    public ObservableMap<String, Field> getFields() {
        return _Fields;
    }

    public LinkPrototype getLinkPrototype(String methodKey, Method method) {
        if (_LinkPrototypes == null) {
            _LinkPrototypes = Observables.observableMap(new TreeMap<String, LinkPrototype>());
        }

        LinkPrototype linkPrototype = null;
        if (_LinkPrototypes.containsKey(methodKey)) {
            linkPrototype = _LinkPrototypes.get(methodKey);
        }
        else {

            final ObservableMap<String, Link> linksByName = getLinksByName();
            if (linksByName != null) {

                final String methodName = method.getName();
                String relName = methodName;
                Link link = linksByName.get(relName);

                if ((link == null) && relName.startsWith("get")) {
                    relName = relName.substring(3);
                    relName = Character.toLowerCase(relName.charAt(0)) + relName.substring(1);

                    link = linksByName.get(relName);
                    if (link == null) {
                        // Check for links like getAuthorAsWriter where the rel name is "author"
                        // Here we need to convert "authorAsWriter" to just "author"
                        final int asIndex = relName.indexOf("As");
                        if (asIndex >= 1) {
                            relName = relName.substring(0, asIndex);
                            link = linksByName.get(relName);
                        }
                    }

                    // We have done all of this assuming that the link we just found
                    // has a method of GET. Confirm that here.
                    if ((link != null) && (link.getRel().getMethod() != org.wrml.core.www.http.Method.GET)) {
                        link = null;
                    }
                }

                if (link != null) {
                    final Context context = getContext();
                    final TypeSystem typeSystem = context.getTypeSystem();
                    final Class<?> nativeType = (Class<?>) typeSystem.getNativeReturnType(method, _NativeType);
                    linkPrototype = new LinkPrototype(context, link.getRelId(), nativeType);
                }
            }

            _LinkPrototypes.put(methodKey, linkPrototype);
        }

        return linkPrototype;

    }

    public ObservableMap<String, Link> getLinksByName() {

        if (_LinksByName == null) {
            final ObservableMap<URI, Link> linksByRel = getLinksByRel();
            if (linksByRel != null) {
                _LinksByName = Observables.observableMap(new TreeMap<String, Link>());
                for (final URI relId : linksByRel.keySet()) {
                    final Link link = linksByRel.get(relId);
                    _LinksByName.put(link.getRel().getName(), link);
                }
            }
        }

        return _LinksByName;
    }

    public ObservableMap<URI, Link> getLinksByRel() {
        return _LinksByRel;
    }

    public MediaType getMediaType() {

        if (_MediaType == null) {
            final SystemTransformers systemTransformers = getContext().getSystemTransformers();
            _MediaType = systemTransformers.getMediaTypeToNativeTypeTransformer().bToA(getNativeType());
        }

        return _MediaType;
    }

    public Class<?> getNativeClass() {
        final SystemTransformers systemTransformers = getContext().getSystemTransformers();
        return systemTransformers.getNativeTypeToClassTransformer().aToB(getNativeType());
    }

    public java.lang.reflect.Type getNativeType() {
        return _NativeType;
    }

    public java.lang.reflect.Type[] getNativeTypeParameters() {
        final Context context = getContext();
        final TypeSystem typeSystem = context.getTypeSystem();
        return typeSystem.getNativeTypeParameters(getNativeType());
    }

    public final URI getSchemaId() {
        final SystemTransformers systemTransformers = getContext().getSystemTransformers();
        return systemTransformers.getMediaTypeToSchemaIdTransformer().aToB(getMediaType());
    }

    @Override
    public String toString() {
        return "Prototype (" + hashCode() + ") : \"" + getSchemaId() + "\"";
    }

    void init() {

        final URI schemaId = getSchemaId();

        System.out.println(this + " is being initialized.");

        List<URI> allYourBase = getAllBaseSchemaIds();
        if (allYourBase != null) {

            if (allYourBase.size() > 1) {

                /*
                 * Assemble all schemas in reverse order starting from the top
                 * end
                 * of the schema hierarchy, which are the last entries in the
                 * ordered LinkedHashMap "allYourBase".
                 */

                allYourBase = new ArrayList<URI>(allYourBase);
                Collections.reverse(allYourBase);
            }

        }
        else {
            allYourBase = new ArrayList<URI>();
        }

        // Include our blueprint schema fields last to achieve final locality
        allYourBase.add(schemaId);

        final int totalNumberOfSchemasToPrototype = allYourBase.size();
        for (int i = 0; i < totalNumberOfSchemasToPrototype; i++) {
            final URI baseId = allYourBase.get(i);

            final Context context = getContext();
            final Schema baseSchema = context.getSchema(baseId);

            System.out.println(this + " has \"" + baseSchema + "\" as basis[" + i + "].");

            if (baseSchema == null) {
                // TODO: Handle the problem differently?
                System.err.println("==== WARNING: " + this
                        + " has === N U L L ===  as a base schema is for schema id: " + baseId);
                return;
            }

            initFields(_Fields, baseSchema);
            initLinks(_LinksByRel, baseSchema);

            // TODO: Implement contstraints
            //initConstraints(allYourConstraints, baseSchema);
        }

        System.out.println(this + " has been \"fully\" initialized.");
    }

    /*
     * private void initConstraints(final SortedMap<URI, ConstraintDefinition>
     * allYourConstraints, final Schema baseSchema) {
     * new PrototypicalExtension<URI, ConstraintDefinition>(allYourConstraints,
     * baseSchema.getConstraints(), getContext()
     * .getSchemaIdToNativeTypeTransformer().bToA(ConstraintDefinition.class));
     * }
     */

    /**
     * Adds the specified schema's base schemas to the algorithmically
     * scoped/reused queue and also marks them as enqueued in the similarly
     * reused map.
     * 
     * @param queue
     *            The reused queue of Schemas that temporarily holds (for
     *            processing) this prototype's schema's base schemas
     * @param schemaId
     * @param enqueuedIds
     *            The reused map enqueued schema ids
     */
    private void enqueueBaseSchemas(final Queue<URI> queue, final URI schemaId, final HashMap<URI, URI> enqueuedIds) {

        final Context context = getContext();
        final Schema schema = context.getSchema(schemaId);
        if (schema == null) {
            System.out.println("Schema is null: " + schemaId);
            return;
        }

        @SuppressWarnings("unchecked")
        final List<URI> baseSchemaIds = (List<URI>) schema.getFieldValue(FieldNames.Schema.baseSchemaIds.toString());
        if ((baseSchemaIds == null) || (baseSchemaIds.size() == 0)) {
            return;
        }

        final URI mySchemaId = getSchemaId();

        for (final URI baseSchemaId : baseSchemaIds) {

            /*
             * Double check that we haven't enqueued this base schema yet and
             * that it isn't our schema.
             */
            if (!enqueuedIds.containsKey(baseSchemaId) && !mySchemaId.equals(baseSchemaId)) {

                /**
                 * Add the base schema to the queue and mark it as such (in the
                 * reused map) so that we never enqueue it again.
                 */
                queue.add(baseSchemaId);
                enqueuedIds.put(baseSchemaId, baseSchemaId);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initFields(final Map<String, Field> allYourFields, final Schema baseSchema) {

        final String fieldName = FieldNames.Schema.fields.toString();
        final List<Field> fields = (List<Field>) baseSchema.getFieldValue(fieldName);
        prototypicalExtension(baseSchema.getModelGraph(), allYourFields, fields, Field.class,
                FieldNames.Named.name.toString());
    }

    @SuppressWarnings("unchecked")
    private void initLinks(final Map<URI, Link> allYourLinks, final Schema baseSchema) {

        final String fieldName = FieldNames.Schema.links.toString();
        final List<Link> links = (List<Link>) baseSchema.getFieldValue(fieldName);

        prototypicalExtension(baseSchema.getModelGraph(), allYourLinks, links, Link.class,
                FieldNames.Link.relId.toString());
    }

    @SuppressWarnings("unchecked")
    private <K, M extends Model> void prototypicalExtension(final ModelGraph modelGraph, final Map<K, M> allModels,
            final List<M> extensionModels, Class<?> staticInterfaceType, String keyFieldName) {

        if (extensionModels == null) {
            return;
        }

        for (final M extensionModel : extensionModels) {

            final K modelKey = (K) extensionModel.getFieldValue(keyFieldName);

            M model = allModels.get(modelKey);

            if (model == null) {

                final Context context = getContext();
                modelGraph.setInitCursorFocusRelationShipName(String.valueOf(modelKey));
                model = (M) context.getModelHeap().newModel(staticInterfaceType, modelGraph).getStaticInterface();
                modelGraph.popInitCursorBack();
                allModels.put(modelKey, model);
            }

            model.extend(extensionModel);
        }

    }

}
