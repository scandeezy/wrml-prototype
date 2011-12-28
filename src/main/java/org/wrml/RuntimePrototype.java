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

import org.wrml.model.Collection;
import org.wrml.model.runtime.Prototype;
import org.wrml.model.schema.Constraint;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Link;
import org.wrml.model.schema.Schema;
import org.wrml.util.ObservableMap;
import org.wrml.util.Observables;

/**
 * The runtime instance of the RuntimePrototype schema. This model is
 * implemented by
 * hand, as its core logic is always local to the WRML runtime and thus the
 * framework's lazy loading is not needed. If some future version or language
 * wishes to remote prototypes then this class will need to be refactored
 * somehow.
 */
public final class RuntimePrototype extends RuntimeModel implements Prototype {

    private static final long serialVersionUID = 918319903519537474L;

    /*
     * TODO: Should the Java code generation framework just auto generate a
     * separate
     * "FieldNames" enum for each Schema - with the enum constants being the
     * field names.
     */

    // TODO: Make this an enum?
    public static final String ALL_BASE_SCHEMAS_FIELD_NAME = "allBaseSchemas";
    public static final String FIELDS_FIELD_NAME = "fields";
    public static final String BLUEPRINT_SCHEMA_ID_FIELD_NAME = "blueprintSchemaId";
    

    public RuntimePrototype(URI schemaId, Context context, URI blueprintSchemaId) {
        super(schemaId, context);
        setBlueprintSchemaId(blueprintSchemaId);
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
     *            RuntimePrototype's  Schema ($)
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

        /*
         * Try to retrieve the cached answer from our (model) field map based on
         * a well-known field name lookup.
         */
        ObservableMap<URI, Schema> allBaseSchemas = (ObservableMap<URI, Schema>) getFieldValue(ALL_BASE_SCHEMAS_FIELD_NAME);
        if (allBaseSchemas != null) {
            return allBaseSchemas;
        }

        //
        // The following algorithm performs a breadth first traversal of 
        // the base schemas graph to produce an orderly result.
        //

        /*
         * This is the map that this method will return, wrapped by an
         * ObservableMap. It is an ordered map of (URI) schema id to the schemas
         * themselves.
         * 
         * This ordered map will hold all of our base schemas that are belong to
         * us schema. In other words it maps, by schema id, the base schemas of
         * the schema associated with our own schema id.
         */
        final LinkedHashMap<URI, Schema> allYourBase = new LinkedHashMap<URI, Schema>();

        /*
         * This queue is used to "process" our schema's base schemas. Processing
         * in this case means collecting all of the base schemas in the order
         * described above. This computation involves a tree traversal of our
         * base schemas.
         */
        final Queue<Schema> queue = new LinkedList<Schema>();

        /*
         * This map retains the URIs of the schemas that have already been
         * processed, meaning that they've been enqueued once already and should
         * not be processed again. It helps us address the fact that our schema
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
             * Check to see if we have already "processed" the base schema and
             * also double check to make sure that the base schema is not the
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
         * Create the ObservableMap wrapper and store it for future reference.
         */
        allBaseSchemas = Observables.observableMap(allYourBase);
        return (ObservableMap<URI, Schema>) setFieldValue(ALL_BASE_SCHEMAS_FIELD_NAME, allBaseSchemas);
    }

    public Schema getBlueprintSchema() {
        return getContext().getSchema(getBlueprintSchemaId());
    }

    public URI getBlueprintSchemaId() {        
        return (URI) getFieldValue(BLUEPRINT_SCHEMA_ID_FIELD_NAME);
    }

    public ObservableMap<URI, Constraint> getConstraints() {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    public ObservableMap<String, Field> getFields() {

        ObservableMap<String, Field> fields = (ObservableMap<String, Field>) getFieldValue(FIELDS_FIELD_NAME);
        if (fields != null) {

            /*
             * We have been here before, and we've already done all of
             * this work once, for the given schema, so return the
             * cached results.
             */

            return fields;
        }

        final SortedMap<String, Field> allYourFields = new TreeMap<String, Field>();

        final ObservableMap<URI, Schema> allYourBase = getAllBaseSchemas();
        if (allYourBase == null) {
            // We have no base schemas (like "Document").

            /*
             * Need to add just the blueprint schema's fields before
             * we return here since we want _all_ fields.
             */

            initFields(getBlueprintSchemaId(), allYourFields);

            fields = Observables.observableMap(allYourFields);
            // Cache the schema's field prototypes for use by model instances
            return (ObservableMap<String, Field>) setFieldValue(FIELDS_FIELD_NAME, fields);
        }

        /*
         * Assemble all fields in reverse order starting from the top end
         * of the schema hierarchy, which are the last entries in the
         * ordered LinkedHashMap "allYourBase".
         */

        // TODO: Optimize this if you want to
        final List<URI> baseIds = new ArrayList<URI>(allYourBase.keySet());
        Collections.reverse(baseIds);
        for (final URI baseId : baseIds) {
            // Include the base schema fields
            initFields(baseId, allYourFields);
        }

        // Include our blueprint schema fields last to achieve final locality
        initFields(getBlueprintSchemaId(), allYourFields);

        fields = Observables.observableMap(allYourFields);
        // Cache the schema's field prototypes for use by model instances
        return (ObservableMap<String, Field>) setFieldValue(FIELDS_FIELD_NAME, fields);
    }

    public ObservableMap<URI, Link> getLinks() {
        // TODO Auto-generated method stub
        return null;
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

        final Collection<Schema> baseSchemaCollection = schema.getBaseSchemas();
        if (baseSchemaCollection == null) {
            return;
        }

        final List<Schema> baseSchemas = baseSchemaCollection.getElements();
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

    private void initFields(final URI schemaId, final SortedMap<String, Field> allYourFields) {

        final Context context = getContext();
        final Schema schema = context.getSchema(schemaId);

        if (schema == null) {
            System.out.println("Schema is null: " + schemaId);
            return;
        }

        final Map<String, Field> fields = schema.getFields();

        for (final String fieldName : fields.keySet()) {

            final Field field = fields.get(fieldName);

            if (field == null) {
                continue;
            }

            Field prototypeField = allYourFields.get(fieldName);
            if (prototypeField == null) {
                prototypeField = (Field) context.instantiateModel(Field.class, this).getStaticInterface();
                allYourFields.put(fieldName, prototypeField);
            }

            final Object defaultValue = field.getDefaultValue();

            // TODO: Any other state to copy?

            prototypeField.setDefaultValue(defaultValue);

            // TODO:
            prototypeField.extend(true, field);
            
            
        }

    }

    private URI setBlueprintSchemaId(URI blueprintSchemaId) {
        return (URI) setFieldValue(BLUEPRINT_SCHEMA_ID_FIELD_NAME, blueprintSchemaId);        
    }

}
