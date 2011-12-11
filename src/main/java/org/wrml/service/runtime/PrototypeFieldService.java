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
import org.wrml.model.runtime.PrototypeField;
import org.wrml.runtime.Context;
import org.wrml.runtime.RuntimePrototypeField;
import org.wrml.service.AbstractService;
import org.wrml.service.UriKeyTransformer;

/**
 * TODO: Refactor this into a more useful proxy/cache base service.
 */
public class PrototypeFieldService extends AbstractService<String, PrototypeField> {

    /**
     * Creates a prototypical instance of the requestor's schema
     * 
     * @param id
     *            the ID of the schema to prototype
     * @param requestor
     *            the model requesting its prototypical instance
     * @return
     */
    public PrototypeField create(URI id, Model requestor) {
        final Context context = requestor.getContext();
        final URI prototypeSchemaId = context.getSchemaIdForClass(Prototype.class);

        // TODO:
        final PrototypeField prototype = new RuntimePrototypeField(prototypeSchemaId, context);
        return prototype;
    }

    public UriKeyTransformer<String> getUriKeyTransformer() {
        // TODO Auto-generated method stub
        return null;
    }

    public PrototypeField put(URI id, PrototypeField modelToSave, Model requestor) {
        return null;
    }

    public PrototypeField remove(URI id, Model requestor) {
        return null;
    }
}
