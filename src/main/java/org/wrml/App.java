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

import org.wrml.model.relation.LinkRelation;
import org.wrml.service.AbstractService;
import org.wrml.service.Service;
import org.wrml.util.UriTransformer;

/**
 * Greetings Program! http://www.moviesounds.com/tron/grtprgrm.wav
 */
public class App {

    public static void main(String[] args) throws Throwable {

        String title = "Self";
        System.out.println("Title: " + title);

        Context context = new Context();

        Class<?> linkRelationClass = LinkRelation.class;

        final URI linkRelationSchemaId = context.getSchemaId(linkRelationClass);

        Service linkRelationService = context.instantiateCachingService(new LinkRelationService(context));
        context.getServiceMap().put(linkRelationSchemaId, linkRelationService);

        URI modelId = URI.create("http://api.relations.wrml.org/common/self");

        Service fetchedTitleService = context.getService(linkRelationClass);
        Model dynamicModel = fetchedTitleService.get(modelId, null);

        dynamicModel.setFieldValue("title", title);

        System.out.println("Dynamic Title: " + dynamicModel.getFieldValue("title"));

        LinkRelation staticModel = (LinkRelation) dynamicModel.getStaticInterface();

        staticModel.setId(modelId);

        System.out.println("Static Title: " + staticModel.getTitle());
        System.out.println("Static Id: " + staticModel.getId());
    }

    private static class LinkRelationService extends AbstractService {

        public LinkRelationService(Context context) {
            super(context);
        }

        public Model create(URI documentId, Model requestor) {
            // TODO Auto-generated method stub
            return null;
        }

        public UriTransformer<?> getIdTransformer() {
            // TODO Auto-generated method stub
            return null;
        }

        public Model put(URI documentId, Model document, Model requestor) {
            // TODO Auto-generated method stub
            return null;
        }

        public Model remove(URI documentId, Model requestor) {
            // TODO Auto-generated method stub
            return null;
        }

        public Model get(URI documentId, Model requestor) {
            return getContext().instantiateModel(LinkRelation.class);
        }

    }

}
