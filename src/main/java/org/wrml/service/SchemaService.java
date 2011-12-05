/**
 * Copyright (C) 2011 WRML.org <mark@wrml.org>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wrml.service;

import java.net.URI;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

import org.wrml.Service;
import org.wrml.model.Collection;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.FieldDefault;
import org.wrml.model.schema.Schema;
import org.wrml.util.ObservableMap;

public abstract class SchemaService implements Service<Schema> {

    private ObservableMap<URI, SchemaState> _SchemaStateMap;

    /**
     * Returns a Map of base schema IDs to all ("recursive") base schemas.
     * 
     * @param schema
     *            the schema to start from.
     * @return
     *         A Map of base schema IDs to all ("recursive") base schemas.
     */
    public LinkedHashMap<URI, Schema> getAllBaseSchemas(final URI id) {

        SchemaState schemaState = getSchemaState(id);
        LinkedHashMap<URI, Schema> allYourBase = schemaState.getAllBaseSchemas();
        if (allYourBase != null) {
            return allYourBase;
        }

        // Perform a breadth first traversal of the base schemas graph to produce an orderly result

        allYourBase = new LinkedHashMap<URI, Schema>();
        final Queue<Schema> queue = new LinkedList<Schema>();
        final HashMap<URI, URI> processedIds = new HashMap<URI, URI>();

        enqueueBaseSchemas(queue, id, processedIds);

        Schema baseSchema = queue.poll();
        while (baseSchema != null) {

            final URI baseSchemaId = baseSchema.getId();

            if (!allYourBase.containsKey(baseSchemaId) && !id.equals(baseSchemaId)) {
                allYourBase.put(baseSchemaId, baseSchema);
                enqueueBaseSchemas(queue, id, processedIds);
            }

            baseSchema = queue.poll();
        }

        schemaState.setAllBaseSchemas(allYourBase);

        return allYourBase;

    }

    public SortedMap<String, Field<?>> getAllFields(final URI id) {

        SchemaState schemaState = getSchemaState(id);
        SortedMap<String, Field<?>> allYourFields = schemaState.getAllFields();
        if (allYourFields != null) {
            return allYourFields;
        }
        
        // TODO: Finish this algorithm - Assemble all fields in reverse order

        LinkedHashMap<URI, Schema> allYourBase = getAllBaseSchemas(id);

        allYourFields = new TreeMap<String, Field<?>>();

        Schema schema = get(id);
        Map<String, Field<?>> fields = schema.getFields();
        allYourFields.putAll(fields);

        // TODO: Include the base schema fields

        schemaState.setAllFields(allYourFields);
        return allYourFields;
    }

    public Map<String, FieldDefault<?>> getFieldDefaults(final URI id) {

        SchemaState schemaState = getSchemaState(id);
        Map<String, FieldDefault<?>> fieldDefaults = schemaState.getFieldDefaults();
        if (fieldDefaults != null) {
            return fieldDefaults;
        }

        //        if (fieldDefaults != null && fieldDefaults.size() > 0) {
        //            _FieldDefaultMap = new HashMap<String, FieldDefault<?>>();
        //
        //            for (FieldDefault<?> fieldDefault : fieldDefaults) {
        //                _FieldDefaultMap.put(fieldDefault.getFieldName(), fieldDefault);
        //            }
        //        }

        //        final Schema schema = model.getSchema();
        //        final Field<?> field = schema.getFields().get(fieldName);
        //        final Object defaultValue = field.getDefaultValue();
        //        if (defaultValue != null) {
        //            return defaultValue;
        //        }

        // TODO: Traverse up the rest of resource template tree to find a
        // default specified in the resource hierarchy

        //final Schema fieldDeclaredSchema = getSchema(field.getDeclaredSchemaId());

        // TODO: Traverse up the base schemas from this object's schema through
        // the list of schemas between it and the field's declared schema.
        // This is not the same as going over all of the base schemas. Nor is it
        // necessary to go over all base schemas and their ancestors. Again, the
        // schemas that need to be checked range from this object's schema to
        // the fields delcared schema (only).

        /*
         * List<URI> baseSchemaIds = schema.getBaseSchemaIds(); if
         * (baseSchemaIds != null && !baseSchemaIds.isEmpty()) { for (URI
         * baseSchemaId : baseSchemaIds) {
         * 
         * baseSchema = getContext().getSchema();
         * 
         * }
         * 
         * }
         */

        //        final ObservableMap<URI, ObservableList<FieldDefault<?>>> fieldDefaultsMap = resourceTemplate
        //                .getSchemaFieldDefaultsMap();
        //
        //        final URI schemaId = getSchemaId();
        //        if (fieldDefaultsMap != null && fieldDefaultsMap.containsKey(schemaId)) {
        //
        //            ObservableList<FieldDefault<?>> schemaFieldDefaults = fieldDefaultsMap.get(schemaId);
        //            if (schemaFieldDefaults != null && schemaFieldDefaults.size() > 0) {
        //                for (FieldDefault<?> fieldDefault : schemaFieldDefaults) {
        //                    String fieldName = fieldDefault.getFieldName();
        //                    Object fieldValue = fieldDefault.getDefaultValue();
        //                    model.setFieldValue(fieldName, fieldValue);
        //                }
        //            }
        //        }

        schemaState.setFieldDefaults(fieldDefaults);
        return fieldDefaults;
    }

    private void enqueueBaseSchemas(final Queue<Schema> queue, final URI id, final HashMap<URI, URI> processedIds) {

        Schema schema = get(id);

        final Collection<Schema> baseSchemaCollection = schema.getBaseSchemas();
        if (baseSchemaCollection == null) {
            return;
        }

        final List<Schema> baseSchemas = baseSchemaCollection.getElements();
        if (baseSchemas == null || baseSchemas.size() == 0) {
            return;
        }

        final URI schemaId = schema.getId();

        for (Schema baseSchema : baseSchemas) {

            final URI baseSchemaId = baseSchema.getId();

            if (!processedIds.containsKey(baseSchemaId) && !schemaId.equals(baseSchemaId)) {
                queue.add(baseSchema);
                processedIds.put(baseSchemaId, baseSchemaId);
            }
        }
    }

    private SchemaState getSchemaState(URI id) {

        if (_SchemaStateMap == null) {
            _SchemaStateMap = new ObservableMap<URI, SchemaState>(new HashMap<URI, SchemaState>());
        }

        SchemaState schemaState;
        if (!_SchemaStateMap.containsKey(id)) {
            schemaState = new SchemaState(id);
            _SchemaStateMap.put(id, schemaState);
        }
        return _SchemaStateMap.get(id);
    }

    private static class SchemaState {

        private final URI _SchemaId;

        private LinkedHashMap<URI, Schema> _AllBaseSchemas;
        private Map<String, FieldDefault<?>> _FieldDefaults;
        private SortedMap<String, Field<?>> _AllFields;

        public SchemaState(URI schemaId) {
            _SchemaId = schemaId;
        }

        public LinkedHashMap<URI, Schema> getAllBaseSchemas() {
            return _AllBaseSchemas;
        }

        public SortedMap<String, Field<?>> getAllFields() {
            return _AllFields;
        }

        public Map<String, FieldDefault<?>> getFieldDefaults() {
            return _FieldDefaults;
        }

        public URI getSchemaId() {
            return _SchemaId;
        }

        public void setAllBaseSchemas(LinkedHashMap<URI, Schema> allBaseSchemas) {
            _AllBaseSchemas = allBaseSchemas;
        }

        public void setAllFields(SortedMap<String, Field<?>> allFields) {
            _AllFields = allFields;
        }

        public void setFieldDefaults(Map<String, FieldDefault<?>> fieldDefaults) {
            _FieldDefaults = fieldDefaults;
        }
    }

}
