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
import org.wrml.RuntimePrototypeField;
import org.wrml.model.runtime.PrototypeField;
import org.wrml.service.AbstractService;
import org.wrml.util.UriTransformer;

public class PrototypeFieldService extends AbstractService {

    private UriTransformer _UriTransformer;

    public PrototypeFieldService(Context context) {
        super(context);
    }

    public Model create(URI id, Model requestor) {
        return null;
    }

    @Override
    public final UriTransformer getIdTransformer() {
        if (_UriTransformer == null) {
            _UriTransformer = createIdTransformer();
        }
        return _UriTransformer;
    }

    protected UriTransformer createIdTransformer() {
        return new FieldNameIdTransformer();
    }

    // TODO: Replace this with UriTemplate and a SchemaFieldParameter["fieldName"]
    private static class FieldNameIdTransformer implements UriTransformer {

        public Object aToB(URI aValue) {
            // TODO: Implement
            return null;
        }

        public URI bToA(Object bValue) {
            // TODO: Implement
            return null;
        }

    }

    public Model get(URI modelId, Model requestor) {

        final Context context = (requestor != null) ? requestor.getContext() : getContext();

        final UriTransformer uriTransformer = getIdTransformer();
        final String fieldName = (String) uriTransformer.aToB(modelId);

        final URI schemaId = getContext().getSchemaId(PrototypeField.class);
        final Model dynamicModel = new RuntimePrototypeField(schemaId, context, fieldName);
        Model staticModel = context.instantiateStaticModel(dynamicModel);
        return staticModel;
    }

    public Model put(URI modelId, Model modelToSave, Model requestor) {
        // TODO Not supported. Throw an exception
        return null;
    }

    public Model remove(URI modelId, Model requestor) {
        // TODO Not supported. Throw an exception
        return null;
    }

}