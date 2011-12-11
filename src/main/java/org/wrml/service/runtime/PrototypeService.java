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

package org.wrml.service.runtime;

import java.net.URI;

import org.wrml.Model;
import org.wrml.model.runtime.Prototype;
import org.wrml.runtime.Context;
import org.wrml.runtime.RuntimePrototype;
import org.wrml.service.AbstractService;
import org.wrml.service.UriKeyTransformer;

public class PrototypeService extends AbstractService<URI, Prototype> {

    /**
     * Creates a prototypical instance of the requestor's schema
     * 
     * @param id
     *            the ID of the schema to prototype
     * @param requestor
     *            the model requesting its prototypical instance
     * @return
     */
    public Prototype create(URI id, Model requestor) {
        final Context context = requestor.getContext();
        final URI prototypeSchemaId = context.getSchemaIdForClass(Prototype.class);
        final Prototype prototype = new RuntimePrototype(prototypeSchemaId, context, id);
        return prototype;
    }

    public UriKeyTransformer<URI> getUriKeyTransformer() {
        // TODO Auto-generated method stub
        return null;
    }

    public Prototype put(URI id, Prototype modelToSave, Model requestor) {
        return null;
    }

    public Prototype remove(URI id, Model requestor) {
        return null;
    }

}
