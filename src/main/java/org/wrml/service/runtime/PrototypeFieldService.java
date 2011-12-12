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
import org.wrml.model.runtime.PrototypeField;
import org.wrml.runtime.RuntimePrototype;
import org.wrml.runtime.RuntimePrototypeField;
import org.wrml.runtime.StaticModelProxy;
import org.wrml.service.AbstractService;
import org.wrml.util.UriTransformer;

public class PrototypeFieldService extends AbstractService {

    private UriTransformer _UriTransformer;

    public PrototypeFieldService(Context context) {
        super(context);
    }

    public Model create(URI id, Model requestor) {
        final UriTransformer uriTransformer = getIdTransformer(requestor);
        final String fieldName = (String) uriTransformer.aToB(id);

        final Context context = (requestor != null) ? requestor.getContext() : getContext();

        final URI schemaId = getContext().getSchemaId(Prototype.class);

        final Model dynamicModel = new RuntimePrototypeField(schemaId, context, fieldName);
        //Model staticModel = StaticModelProxy.newProxyInstance(dynamicModel);
        //return staticModel;
        return dynamicModel;

    }

    public UriTransformer getIdTransformer(Model requestor) {
        if (_UriTransformer == null) {
            _UriTransformer = new FieldNameIdTransformer();
        }
        return _UriTransformer;
    }

    public Model put(URI id, Model modelToSave, Model requestor) {
        return null;
    }

    public Model remove(URI id, Model requestor) {
        return null;
    }

    // TODO: Replace this with UriTemplate and a SchemaFieldParameter["fieldName"]
    private static class FieldNameIdTransformer implements UriTransformer {

        public Object aToB(URI aValue) {
            return null;
        }

        public URI bToA(Object bValue) {
            return null;
        }

    }

}