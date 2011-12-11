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

import org.wrml.Model;

/**
 * A base service class that provides some helpful reusable implementations of
 * the Service interface's methods.
 * 
 * @param <T>
 *            the Model subtype the is serviced here.
 */
public abstract class AbstractService<K, M extends Model> implements Service<K, M> {

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

    public M create() {
        return create(null, null);
    }

    public M create(Model requestor) {
        return create(null, requestor);
    }

    public M create(URI id) {
        return create(id, null);
    }

    public Set<java.util.Map.Entry<URI, M>> entrySet() {
        return null;
    }

    public M get(Object key) {
        return create((URI) key);
    }

    public M get(URI id, Model requestor) {
        return create(id, requestor);
    }

    public boolean isEmpty() {
        return size() <= 0;
    }

    public Set<URI> keySet() {
        return null;
    }

    public M put(URI id, M modelToSave) {
        return put(id, modelToSave, null);
    }

    public void putAll(Map<? extends URI, ? extends M> m) {
    }

    public M remove(Object key) {
        return remove((URI) key, null);
    }

    public int size() {
        return -1;
    }

    public Collection<M> values() {
        return null;
    }

}
