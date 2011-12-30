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

import org.wrml.Model;
import org.wrml.model.Document;
import org.wrml.runtime.Context;
import org.wrml.util.observable.ObservableMap;

public class CachingService extends ProxyService {

    /*
     * TODO: Add code that listens to self (using ProxyService's add
     * ServiceListener) and syncs local cache with the originService
     * 
     * TODO: Implement self cache busting. If a model requests itself, skip the
     * cache and go to the origin.
     */

    private final ObservableMap<URI, Object> _Cache;

    public CachingService(Context context, Service originService, ObservableMap<URI, Object> cache) {
        super(context, originService);
        _Cache = cache;
    }

    public ObservableMap<URI, Object> getCache() {
        return _Cache;
    }

    @Override
    public Object get(URI id, Model referrer) {

        if (id == null) {
            throw new NullPointerException("id (URI) cannot be null");
        }

        System.out.println("CachingService.get - id: " + id + " referrer: " + referrer);

        Map<URI, Object> cache = getCache();

        boolean isRefresh = (referrer != null && referrer instanceof Document && id.equals(((Document) referrer)
                .getId()));

        if (!isRefresh) {
            if (cache.containsKey(id) && !isRefresh) {
                return cache.get(id);
            }
        }

        Object responseEntity = super.get(id, referrer);

        // TODO: Consider a composite key for the cache map to consider response type attributes (like a good HTTP cache would)
        // TODO: Honor TTL and Etags etc

        if (!isRefresh) {
            cache.put(id, responseEntity);
        }

        return responseEntity;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [originService=" + getOriginService() + "]";
    }

}
