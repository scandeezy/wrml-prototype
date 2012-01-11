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

package org.wrml.bootstrap;

import java.net.URI;
import java.util.SortedMap;

import org.wrml.model.schema.Type;
import org.wrml.runtime.Context;
import org.wrml.util.observable.ObservableList;

public class SchemaBootstrapSchema extends DocumentBootstrapSchema {

    public static final String SCHEMA_SCHEMA_NAME = "Schema";
    public static final String SCHEMA_SCHEMA_DESCRIPTION = "A schema describes the structure of a model independent of its format. Schemas provide contractual resource type definitions, which are a crucial component of the interface that binds a server and its clients together.";

    public static final String DOCUMENT_SCHEMA_FULL_NAME = "org.wrml.model.Document";

    private static final long serialVersionUID = 1L;

    public SchemaBootstrapSchema(Context context, URI id) {
        super(context, id);

        // Set some document fields
        setName(SCHEMA_SCHEMA_NAME);
        setDescription(SCHEMA_SCHEMA_DESCRIPTION);

        //
        // Base Schemas
        //

        final ObservableList<URI> baseSchemaIds = getBaseSchemaIds();
        baseSchemaIds.add(getSchemaId(DOCUMENT_SCHEMA_FULL_NAME));

        //
        // Fields
        //

        final SortedMap<String, BootstrapField> fields = getBootstrapFields();
        String fieldName = null;

        // baseSchemaIds
        fieldName = FieldNames.Schema.baseSchemaIds.toString();
        final BootstrapField baseSchemaIdsField = createBootstrapField(fieldName, Type.List);
        fields.put(fieldName, baseSchemaIdsField);

        // constraintIds
        fieldName = FieldNames.Schema.constraintIds.toString();
        final BootstrapField constraintIdsField = createBootstrapField(fieldName, Type.List);
        fields.put(fieldName, constraintIdsField);

        // description
        fieldName = FieldNames.Descriptive.description.toString();
        final BootstrapField descriptionField = createBootstrapField(fieldName, Type.Text);
        fields.put(fieldName, descriptionField);

        // fields
        fieldName = FieldNames.Schema.fields.toString();
        final BootstrapField fieldsField = createBootstrapField(fieldName, Type.Map);
        fields.put(fieldName, fieldsField);

        // links
        fieldName = FieldNames.Schema.links.toString();
        final BootstrapField linksField = createBootstrapField(fieldName, Type.List);
        fields.put(fieldName, linksField);

        // name
        fieldName = FieldNames.Named.name.toString();
        final BootstrapField nameField = createBootstrapField(fieldName, Type.Text);
        fields.put(fieldName, nameField);

        // version
        fieldName = FieldNames.Versioned.version.toString();
        final BootstrapField versionField = createBootstrapField(fieldName, Type.Long);
        fields.put(fieldName, versionField);
    }

}
