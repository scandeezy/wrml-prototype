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

public class FieldBootstrapSchema extends BootstrapSchema {

    public static final String FIELD_SCHEMA_NAME = "Field";

    private static final long serialVersionUID = 1L;

    public FieldBootstrapSchema(Context context, URI id) {
        super(context, id);

        setName(FIELD_SCHEMA_NAME);

        //
        // Fields
        //

        final SortedMap<String, BootstrapField> fields = getBootstrapFields();
        String fieldName = null;

        // constraintIds
        fieldName = FieldNames.Field.constraintIds.toString();
        final BootstrapField constraintIdsField = createBootstrapField(fieldName, Type.List);
        fields.put(fieldName, constraintIdsField);

        // defaultValue
        fieldName = FieldNames.Field.defaultValue.toString();
        final BootstrapField defaultValueField = createBootstrapField(fieldName, Type.Native);
        fields.put(fieldName, defaultValueField);

        // hidden
        fieldName = FieldNames.Field.hidden.toString();
        final BootstrapField hiddenField = createBootstrapField(fieldName, Type.Boolean);
        fields.put(fieldName, hiddenField);

        // readOnly
        fieldName = FieldNames.Field.readOnly.toString();
        final BootstrapField readOnlyField = createBootstrapField(fieldName, Type.Boolean);
        fields.put(fieldName, readOnlyField);

        // required
        fieldName = FieldNames.Field.required.toString();
        final BootstrapField requiredField = createBootstrapField(fieldName, Type.Boolean);
        fields.put(fieldName, requiredField);

        // local
        fieldName = FieldNames.Field.local.toString();
        final BootstrapField localField = createBootstrapField(fieldName, Type.Boolean);
        fields.put(fieldName, localField);

        // name
        fieldName = FieldNames.Named.name.toString();
        final BootstrapField nameField = createBootstrapField(fieldName, Type.Text);
        fields.put(fieldName, nameField);

        // type
        fieldName = FieldNames.Typed.type.toString();
        final BootstrapField typeField = createBootstrapField(fieldName, Type.Choice);
        fields.put(fieldName, typeField);

        // title
        fieldName = FieldNames.Titled.title.toString();
        final BootstrapField titleField = createBootstrapField(fieldName, Type.Text);
        fields.put(fieldName, titleField);

        // description
        fieldName = FieldNames.Descriptive.description.toString();
        final BootstrapField desctiptionField = createBootstrapField(fieldName, Type.Text);
        fields.put(fieldName, desctiptionField);

        // owner
        fieldName = FieldNames.Owned.owner.toString();
        final BootstrapField ownerField = createBootstrapField(fieldName, Type.Model);
        fields.put(fieldName, ownerField);

    }

}
