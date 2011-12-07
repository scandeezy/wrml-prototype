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

package org.wrml.service;

import org.wrml.AbstractService;
import org.wrml.Context;
import org.wrml.Model;
import org.wrml.RuntimePrototype;
import org.wrml.model.schema.Prototype;
import org.wrml.util.AbstractObservableMap;
import org.wrml.util.DelegatingObservableMap;

import java.net.URI;
import java.util.HashMap;

/**
 * TODO: Refactor this into a more useful proxy/cache base service.
 */
public class PrototypeService extends AbstractService<Prototype> {

    private AbstractObservableMap<URI, Prototype> _PrototypeMap;

    public Prototype get(URI id, Model requestor) {

        if (_PrototypeMap == null) {
            return null;
        }

        if ((requestor == null) || (id == null)) {
            return null;
        }

        Prototype prototype;
        if (!_PrototypeMap.containsKey(id)) {
            prototype = create(id, requestor);
            prototype = save(id, prototype, requestor);
        }

        return _PrototypeMap.get(id);
    }

    public Prototype save(URI id, Prototype prototype, Model requestor) {

        if ((requestor == null) || (id == null) || (prototype == null)) {
            // TODO: Handle as an error?
            return null;
        }

        if (_PrototypeMap == null) {
            _PrototypeMap = new DelegatingObservableMap<URI, Prototype>(new HashMap<URI, Prototype>());
        }

        _PrototypeMap.put(id, prototype);

        return _PrototypeMap.get(id);
    }

    /**
     * Creates a prototypical instance of the requestor's schema
     * 
     * @param id
     *            the ID of the schema to prototype
     * @param requestor
     *            the model requesting its prototypical instance
     * @return
     */
    private Prototype create(URI id, Model requestor) {
        final Context context = requestor.getContext();
        final URI prototypeSchemaId = context.getSchemaIdForClass(Prototype.class);
        final Prototype prototype = new RuntimePrototype(prototypeSchemaId, context, id);
        return prototype;
    }
}
