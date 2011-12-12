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

import org.wrml.Context;
import org.wrml.Model;
import org.wrml.runtime.RuntimeModel;
import org.wrml.util.UriTransformer;

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

    public Model create() {
        return create(null, null);
    }

    public Model create(Model requestor) {
        return create(null, requestor);
    }

    public Model create(URI id) {
        return create(id, null);
    }

    public Set<java.util.Map.Entry<URI, Model>> entrySet() {
        return null;
    }

    public Model get(Object key) {
        return create((URI) key);
    }

    public Model get(URI id, Model requestor) {
        return create(id, requestor);
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

    public Model put(URI id, Model modelToSave) {
        return put(id, modelToSave, null);
    }

    public void putAll(Map<? extends URI, ? extends Model> map) {
        // TODO ? Loop?        
    }

    public Model remove(Object key) {
        return remove((URI) key, null);
    }

    public int size() {
        return -1;
    }

    public Collection<Model> values() {
        return null;
    }

    public UriTransformer getIdTransformer() {
        return getIdTransformer(null);
    }

}
