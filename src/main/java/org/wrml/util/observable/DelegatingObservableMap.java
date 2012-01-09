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

package org.wrml.util.observable;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.wrml.util.Delegating;

public class DelegatingObservableMap<K, V> extends AbstractObservableMap<K, V> implements Delegating<Map<K, V>> {

    private final Map<K, V> _Delegate;

    public DelegatingObservableMap(Map<K, V> delegate) {
        _Delegate = delegate;
    }

    public void clear() {
        final MapEvent<K, V> clearingEvent = new MapEvent<K, V>(this, true);
        fireClearingEvent(clearingEvent);

        if (!clearingEvent.isCancelled()) {
            _Delegate.clear();
            fireClearedEvent(new MapEvent<K, V>(this, false));
        }
    }

    public boolean containsKey(Object o) {
        return _Delegate.containsKey(o);
    }

    public boolean containsValue(Object o) {
        return _Delegate.containsValue(o);
    }

    public Set<Entry<K, V>> entrySet() {
        return _Delegate.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return _Delegate.equals(o);
    }

    public V get(Object o) {
        return _Delegate.get(o);
    }

    public Map<K, V> getDelegate() {
        return _Delegate;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" + (_Delegate != null ? "Delegate=" + _Delegate : "") + "]";
    }

    @Override
    public int hashCode() {
        return _Delegate.hashCode();
    }

    public boolean isEmpty() {
        return _Delegate.isEmpty();
    }

    public Set<K> keySet() {
        return _Delegate.keySet();
    }

    public V put(K k, V v) {

        final V old = _Delegate.get(k);
        final MapEvent<K, V> event = new MapEvent<K, V>(this, true, k, v, old);
        fireUpdatingEntryEvent(event);

        if (!event.isCancelled()) {
            final V updated = _Delegate.put(k, v);
            fireEntryUpdatedEvent(new MapEvent<K, V>(this, false, k, v, updated));
            return updated;
        }

        return old;
    }

    public void putAll(Map<? extends K, ? extends V> map) {

        // TODO: May need to loop here to fire individual events or 
        // fire a big mega "container" event

        _Delegate.putAll(map);
    }

    public V remove(Object key) {

        final MapEvent<K, V> removingEvent = new MapEvent<K, V>(this, true, (K) key);
        fireRemovingEntryEvent(removingEvent);

        if (removingEvent.isCancelled()) {
            return null;
        }

        final V removed = _Delegate.remove(key);
        fireEntryRemovedEvent(new MapEvent<K, V>(this, false, (K) key, null, removed));
        return removed;
    }

    public int size() {
        return _Delegate.size();
    }

    public Collection<V> values() {
        return _Delegate.values();
    }
}
