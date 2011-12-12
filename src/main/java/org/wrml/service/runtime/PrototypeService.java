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

import org.wrml.Context;
import org.wrml.Model;
import org.wrml.model.runtime.Prototype;
import org.wrml.model.schema.Schema;
import org.wrml.runtime.RuntimeModel;
import org.wrml.runtime.RuntimePrototype;
import org.wrml.runtime.RuntimePrototypeField;
import org.wrml.runtime.StaticModelProxy;
import org.wrml.service.AbstractService;
import org.wrml.util.UriTransformer;

public class PrototypeService extends AbstractService {

    public PrototypeService(Context context) {
        super(context);
    }

    public Model create(URI id, Model requestor) {
        final Context context = (requestor != null) ? requestor.getContext() : getContext();
        final URI schemaId = getContext().getSchemaId(Prototype.class);                        
        final Model dynamicModel = new RuntimePrototype(schemaId, context, id);
        //Model staticModel = StaticModelProxy.newProxyInstance(dynamicModel);
        //return staticModel;
        return dynamicModel;
    }

    public UriTransformer getIdTransformer(Model requestor) {
        return getContext().getService(Schema.class).getIdTransformer(requestor);
    }

    public Model put(URI id, Model modelToSave, Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

    public Model remove(URI id, Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

}
