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

import org.wrml.model.Document;
import org.wrml.model.config.Config;
import org.wrml.model.schema.Schema;
import org.wrml.runtime.Context;
import org.wrml.runtime.system.transformer.SystemTransformers;
import org.wrml.service.Service;
import org.wrml.www.MediaType;

/**
 * Greetings Program! http://www.moviesounds.com/tron/grtprgrm.wav
 */
public class App {

    public static void main(String[] args) throws Throwable {

        // Create a context for this simple test application

        final Config config = createConfig(args);

        final Context context = new Context(config);

        /*
         * The above code exercised the "bootstrap" meta schema, which is
         * handled internally by the core framework implementation, without
         * fetching "remote" data from a Service.
         * 
         * 
         * The following code references Schemas which have NOT been
         * implemented as bootstrap classes. This means that they WILL be
         * fetched (remotely) from the schema service.
         */

        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final MediaType schemaMediaType = systemTransformers.getMediaTypeToNativeTypeTransformer().bToA(Schema.class);
        final Service schemaService = context.getService(schemaMediaType);

        // Get the media type representing the base Document: application/wrml; schema="http://.../org/wrml/model/Document" 
        final MediaType documentMediaType = systemTransformers.getMediaTypeToNativeTypeTransformer().bToA(
                Document.class);

        // Get the schema id representing the base Document: http://.../org/wrml/model/Document
        final URI documentSchemaId = systemTransformers.getMediaTypeToSchemaIdTransformer().aToB(documentMediaType);

        // Get the (remote) model representing the Document schema.
        final Model dynamicDocumentSchemaModel = (Model) schemaService.get(documentSchemaId, null, schemaMediaType,
                null);

        // Use the dynamic interface
        System.out.println("Dynamic Schema id: " + dynamicDocumentSchemaModel.getFieldValue("id"));
        System.out.println("Dynamic Schema description: " + dynamicDocumentSchemaModel.getFieldValue("description"));
        System.out
                .println("Dynamic Schema baseSchemaIds: " + dynamicDocumentSchemaModel.getFieldValue("baseSchemaIds"));
        System.out.println("Dynamic Schema fields: " + dynamicDocumentSchemaModel.getFieldValue("fields"));

        // Get the static interface
        final Schema staticDocumentSchemaModel = (Schema) dynamicDocumentSchemaModel.getStaticInterface();

        // Use the static interface
        System.out.println("Static Schema id: " + staticDocumentSchemaModel.getId());
        System.out.println("Static Schema description: " + staticDocumentSchemaModel.getDescription());
        System.out.println("Static Schema baseSchemaIds: " + staticDocumentSchemaModel.getBaseSchemaIds());

        System.out.println("Static Schema fields: " + staticDocumentSchemaModel.getFields());

        /*
         * Fetch a simple Document from the Web. This is the first non-schema
         * model loading test.
         */

        // There is no specially configured service for the WRML Document media type, so this will use the WWW (defaults to the web client)
        final Service documentService = context.getService(documentMediaType);

        // The test document's ID
        final URI documentModelId = context.getStringTransformers().getTransformer(URI.class)
                .bToA("http://www.wrml.org/test/docroot");

        // GET the doc
        final Model dynamicDocumentModel = (Model) documentService.get(documentModelId, null, documentMediaType, null);

        // Make it static
        final Document staticDocumentModel = (Document) dynamicDocumentModel.getStaticInterface();

        
        System.out.println("Static Document id: " + staticDocumentModel.getId());

    }

    private static Config createConfig(String[] args) {

        /*
         * TODO: How should we handle config.
         * 
         * I would like it to be a model.
         * 
         * Since config is needed very early in any WRML program, I would assume
         * that Config model instances would need to be backed by a static class
         * (for bootstrapping).
         */

        return null;
    }
}
