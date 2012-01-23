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

import org.wrml.model.config.Config;
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

        testGetResourceModel(context, "org.wrml.model.Document");
        testGetResourceModel(context, "org.wrml.model.api.Api");
        testGetResourceModel(context, "org.wrml.model.Collection");
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

    private static void testGetResourceModel(final Context context, final String resourceSchemaName)
            throws ClassNotFoundException {

        // Get a resource model
        final Model dynamicModel = getDynamicModel(context, "http://www.wrml.org/test/" + resourceSchemaName,
                resourceSchemaName);

        System.out.println("Dynamic Model: " + dynamicModel);

        // Make it static
        final Model staticModel = dynamicModel.getStaticInterface();

        System.out.println("Static Model (" + staticModel.getNativeType() + "): " + staticModel);
    }
}
