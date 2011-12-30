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
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.wrml.runtime.Context;

/**
 * A base service class that provides some helpful reusable implementations of
 * the Service interface's methods.
 * 
 */
public abstract class AbstractService implements Service {

    private final Context _Context;

    public AbstractService(Context context) {
        _Context = context;
    }

    /*
     * TODO: These methods should throw a MethodNotAllowed/Supported/Implemented
     * exception instead of returning null, yes?
     */

    public void clear() {
    }

    public boolean containsKey(Object key) {
        return false;
    }

    public boolean containsValue(Object value) {
        return false;
    }

    public Object create() {
        return create(null);
    }

    public Set<java.util.Map.Entry<URI, Object>> entrySet() {
        return null;
    }

    public Object get(Object key) {
        return get((URI) key, null);
    }

    public final Context getContext() {
        return _Context;
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public Set<URI> keySet() {
        return null;
    }

    public Object put(URI id, Object requestEntity) {
        return put(id, requestEntity, null);
    }

    public void putAll(Map<? extends URI, ? extends Object> map) {
        // TODO ? Loop?        
    }

    public Object remove(Object key) {
        return remove((URI) key, null);
    }

    public int size() {
        return -1;
    }

    public Collection<Object> values() {
        return null;
    }

}
