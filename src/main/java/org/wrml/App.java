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
import org.wrml.service.Service;
import org.wrml.util.MediaType;

/**
 * Greetings Program! http://www.moviesounds.com/tron/grtprgrm.wav
 */
public class App {

    public static void main(String[] args) throws Throwable {

        // Create a context for this simple test application

        Config config = createConfig(args);

        final Context context = new Context(config);

        // Get the media type: application/wrml; schema="http://.../org/wrml/model/schema/Schema"
        final MediaType schemaMediaType = context.getMediaTypeToClassTransformer().bToA(Schema.class);

        /*
         * Get the Context-configured Service that is able to "service" requests
         * for this media type.
         * 
         * In this instance we are testing a special case where the service
         * is responsible for facilitating interactions with Schema resources.
         */
        final Service schemaService = context.getService(schemaMediaType);

        /*
         * Like all WRML Services, the Schema Service is a Map that is keyed on
         * URI. If we want to get a Schema model (value) from the Schema Service
         * (Map) then we need to know the Schema's id, its URI (key).
         * 
         * If we want to get the Schema that represents the form of all WRML
         * models, then we are looking for the MetaSchema, which has an id value
         * of "http://.../org/wrml/model/schema/Schema", which we know is part
         * of the media type that we used to get the service. So we already have
         * what we need to get the URI in the media type but we can use a
         * pre-existing transformer to handle this conversion for us.
         * 
         * Using the right transformer, we can get the URI id of a given schema
         * by starting with a WRML media type (with a schema param).
         */
        final URI schemaSchemaId = context.getSchemaIdToMediaTypeTransformer().bToA(schemaMediaType);

        // Use the Service's overloaded get method to request the MetaSchema.
        final Model dynamicMetaSchemaModel = (Model) schemaService.get(schemaSchemaId, null, schemaMediaType, null);

        /*
         * Using the model instance's dynamic (Map-like) Java API we can get
         * field values (Objects) names based on their names (Strings).
         * 
         * Generally speaking accessing the fields of a model dynamically is
         * appropriate in cases where the static type information is not
         * necessary.
         */

        System.out.println("Dynamic name: " + dynamicMetaSchemaModel.getFieldValue("name"));
        System.out.println("Dynamic id: " + dynamicMetaSchemaModel.getFieldValue("id"));
        System.out.println("Dynamic description: " + dynamicMetaSchemaModel.getFieldValue("description"));
        System.out.println("Dynamic baseSchemaIds: " + dynamicMetaSchemaModel.getFieldValue("baseSchemaIds"));
        //System.out.println("Dynamic fields: " + dynamicMetaSchemaModel.getFieldValue("fields"));

        /*
         * In the Java implementation of WRML, models have two APIs.
         * 
         * First, as demonstrated above, there is the inner more "dynamic"
         * interface with the getFieldValue, setFieldValue, and clickLink
         * methods.
         * 
         * Second, there is a reflection proxy-based static interface that
         * allows Java programs to address WRML models by their auto-generated
         * Java interface. The interface reference that is returned is a thin
         * "facade" over the model's dynamic interface, which ensures that
         * behavior will be consistent between the two APIs.
         * 
         * In this example, we have the model that represents the schema for
         * Schemas (like WRML's Class<Class<?>>).
         */

        final Schema staticMetaSchemaModel = (Schema) dynamicMetaSchemaModel.getStaticInterface();

        System.out.println("Static name: " + staticMetaSchemaModel.getName());
        System.out.println("Static id: " + staticMetaSchemaModel.getId());
        System.out.println("Static description: " + staticMetaSchemaModel.getDescription());
        System.out.println("Static baseSchemaIds: " + staticMetaSchemaModel.getBaseSchemaIds());
        //System.out.println("Static fields: " + staticModel.getFields());

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

        // Get the media type representing the base Document: application/wrml; schema="http://.../org/wrml/model/Document" 
        final MediaType documentMediaType = context.getMediaTypeToClassTransformer().bToA(Document.class);

        // Get the schema id representing the base Document: http://.../org/wrml/model/Document
        final URI documentSchemaId = context.getSchemaIdToMediaTypeTransformer().bToA(documentMediaType);

        // Get the (remote) model representing the Document schema.
        final Model dynamicDocumentSchemaModel = (Model) schemaService.get(documentSchemaId, null, schemaMediaType,
                null);

        // Use the dynamic interface
        System.out.println("Dynamic name: " + dynamicDocumentSchemaModel.getFieldValue("name"));
        System.out.println("Dynamic id: " + dynamicDocumentSchemaModel.getFieldValue("id"));
        System.out.println("Dynamic description: " + dynamicDocumentSchemaModel.getFieldValue("description"));
        System.out.println("Dynamic baseSchemaIds: " + dynamicDocumentSchemaModel.getFieldValue("baseSchemaIds"));
        System.out.println("Dynamic fields: " + dynamicDocumentSchemaModel.getFieldValue("fields"));

        // Get the static interface
        final Schema staticDocumentSchemaModel = (Schema) dynamicDocumentSchemaModel.getStaticInterface();

        // Use the static interface
        System.out.println("Static name: " + staticDocumentSchemaModel.getName());
        System.out.println("Static id: " + staticDocumentSchemaModel.getId());
        System.out.println("Static description: " + staticDocumentSchemaModel.getDescription());
        System.out.println("Static baseSchemaIds: " + staticDocumentSchemaModel.getBaseSchemaIds());

        System.out.println("Static fields: " + staticDocumentSchemaModel.getFields());
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
