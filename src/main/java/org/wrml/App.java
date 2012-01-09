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

import org.wrml.model.schema.Schema;
import org.wrml.runtime.Context;
import org.wrml.service.Service;
import org.wrml.util.MediaType;

/**
 * Greetings Program! http://www.moviesounds.com/tron/grtprgrm.wav
 */
public class App {

    public static void main(String[] args) throws Throwable {

        Context context = new Context();

        MediaType schemaMediaType = context.getMediaTypeToClassTransformer().bToA(Schema.class);
        Service service = context.getService(schemaMediaType);

        URI modelId = context.getSchemaIdToMediaTypeTransformer().bToA(schemaMediaType);
        Model dynamicModel = (Model) service.get(modelId, null, schemaMediaType, null);

        System.out.println("Dynamic name: " + dynamicModel.getFieldValue("name"));
        System.out.println("Dynamic id: " + dynamicModel.getFieldValue("id"));
        System.out.println("Dynamic description: " + dynamicModel.getFieldValue("description"));
        System.out.println("Dynamic baseSchemas: " + dynamicModel.getFieldValue("baseSchemas"));
        System.out.println("Dynamic fields: " + dynamicModel.getFieldValue("fields"));

        Schema staticModel = (Schema) dynamicModel.getStaticInterface();

        System.out.println("Static name: " + staticModel.getName());
        System.out.println("Static id: " + staticModel.getId());
        System.out.println("Static description: " + staticModel.getDescription());
        System.out.println("Static baseSchemas: " + staticModel.getBaseSchemas());
        System.out.println("Static fields: " + staticModel.getFields());

        /*
         * 
         * 
         * 
         * 
         * 
         * 
         * 
         * 
         * 
         * 
         * String title = "Self";
         * System.out.println("Title: " + title);
         * 
         * Context context = new Context();
         * 
         * MediaType linkRelationMediaType =
         * context.getMediaTypeToClassTransformer().bToA(LinkRelation.class);
         * Service service = context.getService(linkRelationMediaType);
         * 
         * URI modelId =
         * URI.create("http://api.relations.wrml.org/common/self");
         * Model dynamicModel = (Model) service.get(modelId, null,
         * linkRelationMediaType, null);
         * 
         * dynamicModel.setFieldValue("title", title);
         * 
         * System.out.println("Dynamic Title: " +
         * dynamicModel.getFieldValue("title"));
         * 
         * LinkRelation staticModel = (LinkRelation)
         * dynamicModel.getStaticInterface();
         * 
         * staticModel.setId(modelId);
         * 
         * System.out.println("Static Title: " + staticModel.getTitle());
         * System.out.println("Static Id: " + staticModel.getId());
         */
    }

}
