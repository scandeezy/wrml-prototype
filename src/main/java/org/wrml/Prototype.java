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

package org.wrml;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

import org.wrml.FieldAccessor.AccessType;
import org.wrml.model.Document;
import org.wrml.model.communication.http.MediaType;
import org.wrml.model.schema.Constraint;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Link;
import org.wrml.model.schema.Schema;
import org.wrml.util.ObservableMap;
import org.wrml.util.Observables;
import org.wrml.util.Transformer;

/**
 * The runtime instance of the Prototype schema. This model is
 * implemented by
 * hand, as its core logic is always local to the WRML runtime and thus the
 * framework's lazy loading is not needed. If some future version or language
 * wishes to remote prototypes then this class will need to be refactored
 * somehow.
 */
final class Prototype {

    private final Context _Context;
    private final URI _BlueprintSchemaId;
    private ObservableMap<URI, Schema> _AllBaseSchemas;
    private ObservableMap<URI, Constraint> _Constraints;
    private ObservableMap<String, Field> _Fields;
    private ObservableMap<URI, Link> _Links;

    private ObservableMap<String, FieldAccessor> _FieldAccessors;
    private ObservableMap<String, LinkClicker> _LinkClickers;

    public Prototype(Context context, URI blueprintSchemaId) {
        _Context = context;
        _BlueprintSchemaId = blueprintSchemaId;
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
    @SuppressWarnings("unchecked")
    public ObservableMap<URI, Schema> getAllBaseSchemas() {

        if (_AllBaseSchemas == null) {

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
            final LinkedHashMap<URI, Schema> allYourBase = new LinkedHashMap<URI, Schema>();

            /*
             * This queue is used to "process" our schema's base schemas.
             * Processing
             * in this case means collecting all of the base schemas in the
             * order
             * described above. This computation involves a tree traversal of
             * our
             * base schemas.
             */
            final Queue<Schema> queue = new LinkedList<Schema>();

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
            final URI blueprintSchemaId = getBlueprintSchemaId();
            enqueueBaseSchemas(queue, blueprintSchemaId, enqueuedIds);

            /*
             * Dequeue the first base schema and start the processing loop.
             */
            Schema baseSchema = queue.poll();
            while (baseSchema != null) {

                final URI baseSchemaId = baseSchema.getId();

                /*
                 * Check to see if we have already "processed" the base schema
                 * and
                 * also double check to make sure that the base schema is not
                 * the
                 * blueprint - that would be weird.
                 */
                if (!allYourBase.containsKey(baseSchemaId) && !blueprintSchemaId.equals(baseSchemaId)) {

                    /*
                     * Process the base schema by adding it to our ordered
                     * collection. Enqueue its base schemas too.
                     */
                    allYourBase.put(baseSchemaId, baseSchema);
                    enqueueBaseSchemas(queue, baseSchemaId, enqueuedIds);
                }

                /*
                 * Dequeue the next base schema to prep the next iteration for
                 * processing.
                 */
                baseSchema = queue.poll();
            }

            /*
             * Create the ObservableMap wrapper and store it for future
             * reference.
             */
            _AllBaseSchemas = Observables.observableMap(allYourBase);
        }

        return _AllBaseSchemas;
    }

    public Schema getBlueprintSchema() {
        return getContext().getSchema(getBlueprintSchemaId());
    }

    public URI getBlueprintSchemaId() {
        return _BlueprintSchemaId;
    }

    public ObservableMap<URI, Constraint> getConstraints() {
        if (_Constraints == null) {
            init();
        }

        return _Constraints;
    }

    public Context getContext() {
        return _Context;
    }

    public ObservableMap<String, Field> getFields() {

        if (_Fields == null) {
            init();
        }

        return _Fields;

    }

    public FieldAccessor getFieldAccessor(String methodKey, String methodName) {

        if (_FieldAccessors == null) {
            _FieldAccessors = Observables.observableMap(new TreeMap<String, FieldAccessor>());
        }

        FieldAccessor fieldAccessor = null;
        if (_FieldAccessors.containsKey(methodKey)) {
            fieldAccessor = _FieldAccessors.get(methodKey);
        }
        else {
            
            String fieldName = null;
            FieldAccessor.AccessType accessType = AccessType.GET;
            if (methodName.startsWith("get")) {
                fieldName = methodName.substring(3);
            }
            else if (methodName.startsWith("is")) {
                fieldName = methodName.substring(2);
            }
            else if (methodName.startsWith("set")) {
                fieldName = methodName.substring(3);
                accessType = AccessType.SET;
            }

            if (fieldName != null) {
                fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

                ObservableMap<String, Field> prototypeFields = getFields();
                Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;

                if (prototypeField != null) {                                        
                    fieldAccessor = new FieldAccessor(fieldName, accessType);                    
                }
            }

                        
            _FieldAccessors.put(methodKey, fieldAccessor);
        }

        return fieldAccessor;
    }

    private void init() {

        List<URI> baseIds = null;
        final ObservableMap<URI, Schema> allYourBase = getAllBaseSchemas();
        if (allYourBase != null) {

            /*
             * Assemble all schemas in reverse order starting from the top end
             * of the schema hierarchy, which are the last entries in the
             * ordered LinkedHashMap "allYourBase".
             */

            // TODO: Optimize this if you want to
            baseIds = new ArrayList<URI>(allYourBase.keySet());
            Collections.reverse(baseIds);
        }
        else {
            baseIds = new ArrayList<URI>();
        }

        // Include our blueprint schema fields last to achieve final locality
        baseIds.add(getBlueprintSchemaId());

        final SortedMap<String, Field> allYourFields = new TreeMap<String, Field>();
        final SortedMap<URI, Link> allYourLinks = new TreeMap<URI, Link>();
        final SortedMap<URI, Constraint> allYourConstraints = new TreeMap<URI, Constraint>();

        for (final URI baseId : baseIds) {

            final Context context = getContext();
            final Schema baseSchema = context.getSchema(baseId);

            if (baseSchema == null) {
                System.out.println("Schema is null: " + baseId);
                return;
            }

            initFields(allYourFields, baseSchema);
            initLinks(allYourLinks, baseSchema);
            initConstraints(allYourConstraints, baseSchema);
        }

        _Fields = Observables.observableMap(allYourFields);
        _Links = Observables.observableMap(allYourLinks);
        _Constraints = Observables.observableMap(allYourConstraints);
    }

    public ObservableMap<URI, Link> getLinks() {
        if (_Links == null) {
            init();
        }

        return _Links;
    }

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
    private void enqueueBaseSchemas(final Queue<Schema> queue, final URI schemaId, final HashMap<URI, URI> enqueuedIds) {

        final Schema schema = getContext().getSchema(schemaId);
        if (schema == null) {
            System.out.println("Schema is null: " + schemaId);
            return;
        }

        final List<Schema> baseSchemas = schema.getBaseSchemas();
        if ((baseSchemas == null) || (baseSchemas.size() == 0)) {
            return;
        }

        final URI blueprintSchemaId = getBlueprintSchemaId();

        for (final Schema baseSchema : baseSchemas) {

            final URI baseSchemaId = baseSchema.getId();

            /*
             * Double check that we haven't enqueued this base schema yet and
             * that it isn't our blueprint schema.
             */
            if (!enqueuedIds.containsKey(baseSchemaId) && !blueprintSchemaId.equals(baseSchemaId)) {

                /**
                 * Add the base schema to the queue and mark it as such (in the
                 * reused map) so that we never enqueue it again.
                 */
                queue.add(baseSchema);
                enqueuedIds.put(baseSchemaId, baseSchemaId);
            }
        }
    }

    private void initFields(final SortedMap<String, Field> allYourFields, final Schema baseSchema) {
        new PrototypicalExtension<String, Field>(allYourFields, baseSchema.getFields(), getContext().getSchemaId(
                Field.class));
    }

    private void initLinks(final SortedMap<URI, Link> allYourLinks, final Schema baseSchema) {
        new PrototypicalExtension<URI, Link>(allYourLinks, baseSchema.getLinks(), getContext().getSchemaId(Link.class));
    }

    private void initConstraints(final SortedMap<URI, Constraint> allYourConstraints, final Schema baseSchema) {
        new PrototypicalExtension<URI, Constraint>(allYourConstraints, baseSchema.getConstraints(), getContext()
                .getSchemaId(Constraint.class));
    }

    private class PrototypicalExtension<K, M extends Model> {

        @SuppressWarnings("unchecked")
        public PrototypicalExtension(final Map<K, M> allModels, final Map<K, M> extensionModels, URI schemaId) {

            for (final K modelKey : extensionModels.keySet()) {

                final M extensionModel = extensionModels.get(modelKey);

                if (extensionModel == null) {
                    continue;
                }

                extend(allModels, modelKey, extensionModel, schemaId);

            }

        }

        @SuppressWarnings("unchecked")
        public PrototypicalExtension(final Map<K, M> allModels, final List<M> extensionModels, URI schemaId) {
            for (final M extensionModel : extensionModels) {
                K modelKey = (K) ((Document) extensionModel).getId();
                extend(allModels, modelKey, extensionModel, schemaId);
            }
        }

        @SuppressWarnings("unchecked")
        private void extend(final Map<K, M> allModels, K modelKey, M extensionModel, URI schemaId) {
            final Context context = getContext();
            M model = allModels.get(modelKey);
            if (model == null) {
                model = (M) context.instantiateModel(schemaId).getStaticInterface();
                allModels.put(modelKey, model);
            }

            model.extend(extensionModel);

        }

    }

    public LinkClicker getLinkClicker(String methodKey, String methodName, Class<?> returnType) {
        if (_LinkClickers == null) {
            _LinkClickers = Observables.observableMap(new TreeMap<String, LinkClicker>());
        }

        LinkClicker linkClicker = null;
        if (_LinkClickers.containsKey(methodKey)) {
            linkClicker = _LinkClickers.get(methodKey);
        }
        else {

            URI rel = null;
            String relName = null;
            if (methodName.startsWith("get")) {
                relName = methodName.substring(3);
                relName = Character.toLowerCase(relName.charAt(0)) + relName.substring(1);
            }

            // TODO: Determine the rel from the method
                        
            if (rel != null) {

                ObservableMap<URI, Link> prototypeLinks = getLinks();
                Link prototypeLink = (prototypeLinks != null) ? prototypeLinks.get(rel) : null;
                
                if (prototypeLink != null) {           
                    Transformer<MediaType, Class<?>> mediaTypeToClassTransformer = getContext().getMediaTypeToClassTransformer();
                    
                    linkClicker = new LinkClicker(rel, mediaTypeToClassTransformer.bToA(returnType));                    
                }
            }

                        
            _LinkClickers.put(methodKey, linkClicker);
        }

        return linkClicker;

    }

}
