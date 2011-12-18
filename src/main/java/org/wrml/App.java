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

import org.wrml.model.Titled;
import org.wrml.service.AbstractService;
import org.wrml.service.Service;
import org.wrml.util.UriTransformer;

/**
 * Greetings Program! http://www.moviesounds.com/tron/grtprgrm.wav
 */
public class App {

    public static void main(String[] args) throws Throwable {

        String title = "Greetings Program!";
        System.out.println("Introduction Title: " + title);

        Context context = new Context();

        Class<?> titledClass = Titled.class;

        final URI titledSchemaId = context.getSchemaId(titledClass);

        Service titledService = context.instantiateCachingService(new TitledService(context));
        context.getServiceMap().put(titledSchemaId, titledService);

        URI modelId = URI.create("http://localhost/titles/1234");

        Service fetchedTitleService = context.getService(titledClass);
        Model dynamicModel = fetchedTitleService.create(modelId, null);

        dynamicModel.setFieldValue("title", title);

        System.out.println("Dynamic Title: " + dynamicModel.getFieldValue("title"));

        Titled staticModel = (Titled) dynamicModel;

        System.out.println("Static Title: " + staticModel.getTitle());
        System.out.println("Static Id: " + staticModel.getId());
        System.out.println("Static ReadOnly: " + staticModel.isReadOnly());        
    }

    private static class TitledService extends AbstractService {

        public TitledService(Context context) {
            super(context);
        }

        public Model create(URI modelId, Model requestor) {
            final Titled model = (Titled) getContext().instantiateStaticModel(Titled.class, modelId, requestor);
            model.setReadOnly(true);
            return model;
        }

        public UriTransformer getIdTransformer() {
            // TODO Auto-generated method stub
            return null;
        }

        public Model put(URI id, Model modelToSave, Model requestor) {
            // TODO Auto-generated method stub
            return null;
        }

        public Model remove(URI id, Model requestor) {
            // TODO Auto-generated method stub
            return null;
        }

        public Model get(URI id, Model requestor) {
            // TODO Auto-generated method stub
            return null;
        }

    }

}
