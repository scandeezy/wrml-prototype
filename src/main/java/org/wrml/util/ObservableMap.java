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

package org.wrml.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A Map that fires events when entries are inserted, updated, removed, or
 * cleared.
 * 
 * @param <K>
 *            The key type
 * @param <V>
 *            The value type
 */
public class ObservableMap<K, V> implements Map<K, V>, Serializable {

    private static final long serialVersionUID = -4210504376374050501L;

    private final Map<K, V> _Delegate;

    // TODO: Make sure any listeners are marked as transient

    // Perhaps use constraints to determine what kind of delegate should be used.
    // E.G. A schema constraint like "sorted" would use a tree map
    // E.G. A schema constraint like "ordered" would use a linked has map
    public ObservableMap(final Map<K, V> delegate) {
        _Delegate = delegate;
    }

    public void addEventListener(MapEventListener<K, V> listener) {
        // TODO
    }

    public void clear() {
        // TODO Auto-generated method stub

    }

    public boolean containsKey(Object key) {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        return false;
    }

    public Set<java.util.Map.Entry<K, V>> entrySet() {
        // TODO Auto-generated method stub
        return null;
    }

    public V get(Object key) {
        // TODO Auto-generated method stub
        return null;
    }

    public Map<K, V> getDelegate() {
        return _Delegate;
    }

    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    public Set<K> keySet() {
        // TODO Auto-generated method stub
        return null;
    }

    public V put(K key, V value) {
        // TODO Auto-generated method stub
        return null;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        // TODO Auto-generated method stub

    }

    public V remove(Object key) {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeEventListener(MapEventListener<K, V> listener) {
        // TODO
    }

    public int size() {
        // TODO Auto-generated method stub
        return 0;
    }

    public Collection<V> values() {
        // TODO Auto-generated method stub
        return null;
    }

}
