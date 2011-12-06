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
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Prototype;
import org.wrml.model.schema.PrototypeField;
import org.wrml.model.schema.Schema;
import org.wrml.util.ObservableMap;

public class RuntimePrototype extends RuntimeModel implements Prototype {

    private static final long serialVersionUID = 918319903519537474L;

    // TODO: Make this an enum?

    /*
     * Should the Java code generation framework just auto generate a separate
     * "FieldNames" enum for each Schema - with the enum constants being the
     * field names.
     */

    public static final String ALL_BASE_SCHEMAS_FIELD_NAME = "allBaseSchemas";
    public static final String PROTOTYPE_FIELDS_FIELD_NAME = "prototypeFields";

    public RuntimePrototype(URI schemaId, Context context, URI id) {
        super(schemaId, context);
        setFieldValue(RuntimeModel.ID_FIELD_NAME, id);
    }

    /**
     * Returns a Map of base schema IDs to all ("recursive") base schemas.
     */
    @SuppressWarnings("unchecked")
    public ObservableMap<URI, Schema> getAllBaseSchemas() {

        ObservableMap<URI, Schema> allBaseSchemas = (ObservableMap<URI, Schema>) getFieldValue(ALL_BASE_SCHEMAS_FIELD_NAME);
        if (allBaseSchemas != null) {
            return allBaseSchemas;
        }

        // Perform a breadth first traversal of the base schemas graph to produce an orderly result

        final LinkedHashMap<URI, Schema> allYourBase = new LinkedHashMap<URI, Schema>();
        final Queue<Schema> queue = new LinkedList<Schema>();
        final HashMap<URI, URI> processedIds = new HashMap<URI, URI>();

        final URI id = getId();
        enqueueBaseSchemas(queue, id, processedIds);

        Schema baseSchema = queue.poll();
        while (baseSchema != null) {

            final URI baseSchemaId = baseSchema.getId();

            if (!allYourBase.containsKey(baseSchemaId) && !id.equals(baseSchemaId)) {
                allYourBase.put(baseSchemaId, baseSchema);
                enqueueBaseSchemas(queue, baseSchemaId, processedIds);
            }

            baseSchema = queue.poll();
        }

        allBaseSchemas = new ObservableMap<URI, Schema>(allYourBase);
        return (ObservableMap<URI, Schema>) setFieldValue(ALL_BASE_SCHEMAS_FIELD_NAME, allBaseSchemas);
    }

    @SuppressWarnings("unchecked")
    public ObservableMap<String, PrototypeField<?>> getPrototypeFields() {

        ObservableMap<String, PrototypeField<?>> prototypeFields = (ObservableMap<String, PrototypeField<?>>) getFieldValue(PROTOTYPE_FIELDS_FIELD_NAME);
        if (prototypeFields != null) {

            // We have been here before, and we've already done all of 
            // this work once, for the given schema, so return the 
            // cached results.

            return prototypeFields;
        }

        final SortedMap<String, PrototypeField<?>> allYourFields = new TreeMap<String, PrototypeField<?>>();

        final ObservableMap<URI, Schema> allYourBase = getAllBaseSchemas();
        if (allYourBase == null) {
            // We have no base schemas (like "Document").

            // Need to add just the schema's fields before 
            // we return here since we want _all_ fields.

            putSchemaFields(getId(), allYourFields);

            prototypeFields = new ObservableMap<String, PrototypeField<?>>(allYourFields);
            // Cache the schema's field prototypes for use by model instances
            return (ObservableMap<String, PrototypeField<?>>) setFieldValue(PROTOTYPE_FIELDS_FIELD_NAME,
                    prototypeFields);
        }

        // Assemble all fields in reverse order starting from the top end
        // of the schema hierarchy, which are the last entries in the
        // ordered LinkedHashMap "allYourBase".

        // TODO: Optimize this if you want to
        final List<URI> baseIds = new ArrayList<URI>(allYourBase.keySet());
        Collections.reverse(baseIds);
        for (final URI baseId : baseIds) {
            // Include the base schema fields
            putSchemaFields(baseId, allYourFields);
        }

        // Include our source schema fields last to achieve 
        // final locality
        putSchemaFields(getId(), allYourFields);

        prototypeFields = new ObservableMap<String, PrototypeField<?>>(allYourFields);
        // Cache the schema's field prototypes for use by model instances
        return (ObservableMap<String, PrototypeField<?>>) setFieldValue(PROTOTYPE_FIELDS_FIELD_NAME, prototypeFields);
    }

    public Schema getSourceSchema() {
        return getContext().getSchema(getId(), this);
    }

    private void enqueueBaseSchemas(final Queue<Schema> queue, final URI schemaId, final HashMap<URI, URI> processedIds) {

        final Schema schema = getContext().getSchema(schemaId, this);

        final Collection<Schema> baseSchemaCollection = schema.getBaseSchemas();
        if (baseSchemaCollection == null) {
            return;
        }

        final List<Schema> baseSchemas = baseSchemaCollection.getElements();
        if ((baseSchemas == null) || (baseSchemas.size() == 0)) {
            return;
        }

        for (final Schema baseSchema : baseSchemas) {

            final URI baseSchemaId = baseSchema.getId();

            if (!processedIds.containsKey(baseSchemaId) && !getId().equals(baseSchemaId)) {
                queue.add(baseSchema);
                processedIds.put(baseSchemaId, baseSchemaId);
            }
        }
    }

    private void putSchemaFields(final URI schemaId, final SortedMap<String, PrototypeField<?>> allYourFields) {

        final Schema schema = getContext().getSchema(schemaId, this);
        final Map<String, Field<?>> fields = schema.getFields();

        // TODO: Put or update the PrototypeField based on the Schema Field

    }

}

// TODO: Check to see if Field exists 

//        if (prototypeFields != null && prototypeFields.size() > 0) {
//            _FieldDefaultMap = new HashMap<String, PrototypeField<?>>();
//
//            for (PrototypeField<?> fieldDefault : prototypeFields) {
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

//        final ObservableMap<URI, ObservableList<PrototypeField<?>>> fieldDefaultsMap = resourceTemplate
//                .getSchemaPrototypeFieldsMap();
//
//        final URI schemaId = getSchemaId();
//        if (fieldDefaultsMap != null && fieldDefaultsMap.containsKey(schemaId)) {
//
//            ObservableList<PrototypeField<?>> schemaPrototypeFields = fieldDefaultsMap.get(schemaId);
//            if (schemaPrototypeFields != null && schemaPrototypeFields.size() > 0) {
//                for (PrototypeField<?> fieldDefault : schemaPrototypeFields) {
//                    String fieldName = fieldDefault.getFieldName();
//                    Object fieldValue = fieldDefault.getDefaultValue();
//                    model.setFieldValue(fieldName, fieldValue);
//                }
//            }
//        }

//        prototype.setPrototypeFields(prototypeFields);
//        return prototypeFields;
//    }

