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

import java.net.URI;
import java.util.Map;

import org.wrml.Context;
import org.wrml.Model;
import org.wrml.util.DelegatingObservableMap;
import org.wrml.util.UriTransformer;

public class ProxyService extends DelegatingObservableMap<URI, Model> implements DelegatingService<Map<URI, Model>> {

    // TODO: Add service event handler add/remove and event firing logic

    private final Context _Context;

    public ProxyService(Context context, Service originService) {
        super(originService);
        _Context = context;
    }

    public final Context getContext() {
        return _Context;
    }

    public UriTransformer<?> getIdTransformer() {
        return getOriginService().getIdTransformer();
    }

    public Service getOriginService() {
        return (Service) super.getDelegate();
    }

    public Model create(Model requestor) {
        return getOriginService().create(requestor);
    }

    public Model create(URI modelId, Model requestor) {
        return getOriginService().create(modelId, requestor);
    }

    public Model get(URI modelId, Model requestor) {
        return getOriginService().get(modelId, requestor);
    }

    public Model put(URI documentId, Model document, Model requestor) {
        return getOriginService().put(documentId, document, requestor);
    }

    public Model remove(URI modelId, Model requestor) {
        return getOriginService().remove(modelId, requestor);
    }

    public Model create() {
        return getOriginService().create();
    }

    public Model create(URI id) {
        return getOriginService().create(id);
    }

}
