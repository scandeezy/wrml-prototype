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

package org.wrml.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * TODO Just an idea that it might be cool to have a Map interface whose values
 * are local to a thread. This would be implemented by having a ThreadLocal per
 * key with the value part being stored in the ThreadLocal. This might be handy
 * to reuse all of a Model except its field and link state. Not sure if that is
 * a real use case but it could be.
 */
public class ThreadLocalMap<K, V> implements Map<K, V> {

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

    public void putAll(Map<? extends K, ? extends V> m) {
        // TODO Auto-generated method stub

    }

    public V remove(Object key) {
        // TODO Auto-generated method stub
        return null;
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
