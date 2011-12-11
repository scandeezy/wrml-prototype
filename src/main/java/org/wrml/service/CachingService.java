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
import org.wrml.util.ObservableMap;

public class CachingService<K, M extends Model, S extends Service<K, M>> extends ProxyService<K, M, S> {

    private final ObservableMap<URI, M> _Cache;

    public CachingService(S originService, ObservableMap<URI, M> cache) {
        super(originService);
        _Cache = cache;
    }

    public ObservableMap<URI, M> getCache() {
        return _Cache;
    }

    /*
     * TODO: Add code that listens to self (using ProxyService's add
     * ServiceListener) and syncs local cache with the originService
     */

}
