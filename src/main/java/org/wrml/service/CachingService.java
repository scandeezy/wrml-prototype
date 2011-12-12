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
import org.wrml.util.ObservableMap;

public class CachingService extends ProxyService {

    private final ObservableMap<URI, Model> _Cache;

    public CachingService(Context context, Service originService, ObservableMap<URI, Model> cache) {
        super(context, originService);
        _Cache = cache;
    }

    public ObservableMap<URI, Model> getCache() {
        return _Cache;
    }

    @Override
    public Model create() {
        // TODO Auto-generated method stub
        return super.create();
    }

    @Override
    public Model create(Model requestor) {
        // TODO Auto-generated method stub
        return super.create(requestor);
    }

    @Override
    public Model create(URI id) {
        // TODO Auto-generated method stub
        return super.create(id);
    }

    @Override
    public Model create(URI id, Model requestor) {
        // TODO Auto-generated method stub
        return super.create(id, requestor);
    }

    @Override
    public Model get(URI id, Model requestor) {

        if (id == null) {
            throw new NullPointerException("id (URI) cannot be null");
        }

        System.out.println("CachingService.get - id: " + id + " requestor: " + requestor);

        Map<URI, Model> cache = getCache();
        
        boolean isRefresh = (requestor != null && id.equals(requestor.getId()));
                
        if (!isRefresh) {            
            if (cache.containsKey(id) && !isRefresh) {
                return cache.get(id);
            }
        }
        
        Model model = super.get(id, requestor);
        
        if (!isRefresh) {
            cache.put(id, model);
        }

        return model;
    }

    @Override
    public Model put(URI id, Model modelToSave, Model requestor) {
        // TODO Auto-generated method stub
        return super.put(id, modelToSave, requestor);
    }

    @Override
    public Model remove(URI id, Model requestor) {
        // TODO Auto-generated method stub
        return super.remove(id, requestor);
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        super.clear();
    }

    @Override
    public boolean containsKey(Object o) {
        // TODO Auto-generated method stub
        return super.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        // TODO Auto-generated method stub
        return super.containsValue(o);
    }

    @Override
    public Model get(Object o) {
        // TODO Auto-generated method stub
        return super.get(o);
    }

    @Override
    public Model put(URI k, Model v) {
        // TODO Auto-generated method stub
        return super.put(k, v);
    }

    @Override
    public void putAll(Map<? extends URI, ? extends Model> map) {
        // TODO Auto-generated method stub
        super.putAll(map);
    }

    @Override
    public Model remove(Object key) {
        // TODO Auto-generated method stub
        return super.remove(key);
    }

    @Override
    public String toString() {
        return getClass().getName() + " [originService=" + getOriginService() + "]";
    }

    /*
     * TODO: Add code that listens to self (using ProxyService's add
     * ServiceListener) and syncs local cache with the originService
     */

    // TODO: Implement self cache busting. 
    // If a model requests itself, skip the cache and go to the origin.

}
