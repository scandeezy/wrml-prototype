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

public class DocumentBootstrapSchema extends BootstrapSchema {

    public static final String DOCUMENT_SCHEMA_NAME = "Document";

    private static final long serialVersionUID = 1L;

    public DocumentBootstrapSchema(Context context, URI id) {
        super(context, id);

        // Set some document fields
        setName(DOCUMENT_SCHEMA_NAME);

        //
        // Fields
        //

        final SortedMap<String, BootstrapField> fields = getBootstrapFields();
        String fieldName = null;

        // etag
        fieldName = FieldNames.Document.etag.toString();
        final BootstrapField etagField = createBootstrapField(fieldName, Type.Text);
        fields.put(fieldName, etagField);

        // id
        fieldName = FieldNames.Document.id.toString();
        final BootstrapField idField = createBootstrapField(fieldName, Type.Text);
        fields.put(fieldName, idField);

        // readOnly
        fieldName = FieldNames.Document.readOnly.toString();
        final BootstrapField readOnlyField = createBootstrapField(fieldName, Type.Boolean);
        fields.put(fieldName, readOnlyField);

        // secondsToLive
        fieldName = FieldNames.Document.secondsToLive.toString();
        final BootstrapField secondsToLiveField = createBootstrapField(fieldName, Type.Long);
        fields.put(fieldName, secondsToLiveField);

    }

}
