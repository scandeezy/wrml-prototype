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

package org.wrml.core;

import java.net.URI;

import org.wrml.core.model.config.Config;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.system.transformer.SystemTransformers;
import org.wrml.core.service.Service;
import org.wrml.core.www.MediaType;

/**
 * Greetings Program! http://www.moviesounds.com/tron/grtprgrm.wav
 */
public class App {

    public static void main(String[] args) throws Throwable {
        System.out
                .println("<a rel=\"greeting\" href=\"http://www.moviesounds.com/tron/grtprgrm.wav\">Greetings Program!</a>");

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

        final Model document = testGetResourceModel(context, "org.wrml.core.model.Document");
        final Model api = testGetResourceModel(context, "org.wrml.core.model.api.Api");
        final Model collection = testGetResourceModel(context, "org.wrml.core.model.Collection");

        System.out.println("End of line.");
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

    private static Model getDynamicModel(final Context context, final String resourceIdString,
            final String resourceSchemaName) throws ClassNotFoundException {

        final URI resourceId = context.getStringTransformers().getTransformer(URI.class).bToA(resourceIdString);

        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final MediaType responseType = systemTransformers.getMediaTypeToNativeTypeTransformer().bToA(
                Class.forName(resourceSchemaName));
        final Service service = context.getService(responseType);

        return (Model) service.get(resourceId, null, responseType, null);
    }

    private static Model testGetResourceModel(final Context context, final String resourceSchemaName)
            throws ClassNotFoundException {

        // Get a resource model
        final Model dynamicModel = getDynamicModel(context, "http://www.wrml.org/test/" + resourceSchemaName,
                resourceSchemaName);

        System.out.println("Dynamic Model: " + dynamicModel);

        // Make it static
        final Model staticModel = dynamicModel.getStaticInterface();

        System.out.println("Static Model (" + staticModel.getNativeType() + "): " + staticModel);

        return staticModel;
    }
}
