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

import org.wrml.Model;
import org.wrml.model.resource.Action;
import org.wrml.util.DelegatingObservableMap;

public class ProxyService<K, M extends Model, S extends Service<K, M>> extends DelegatingObservableMap<URI, M>
        implements Service<K, M> {

    // TODO: Add service event handler add/remove and event firing logic

    public ProxyService(S originService) {
        super(originService);
    }

    public M create() {
        return getOriginService().create();
    }

    public M create(Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

    public M create(URI id) {
        // TODO Auto-generated method stub
        return null;
    }

    public M create(URI id, Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

    public Model execute(URI id) {
        // TODO Auto-generated method stub
        return null;
    }

    public Model execute(URI id, Action action) {
        // TODO Auto-generated method stub
        return null;
    }

    public Model execute(URI id, Action action, Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

    public Model execute(URI id, Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

    public M get(URI id, Model requestor) {
        return getOriginService().get(id, requestor);
    }

    @SuppressWarnings("unchecked")
    public S getOriginService() {
        return (S) super.getDelegate();
    }

    public UriKeyTransformer<K> getUriKeyTransformer() {
        // TODO Auto-generated method stub
        return null;
    }

    public M put(URI id, M modelToSave, Model requestor) {
        return getOriginService().get(id, requestor);
    }

    public M remove(URI id, Model requestor) {
        return null;
    }

}
