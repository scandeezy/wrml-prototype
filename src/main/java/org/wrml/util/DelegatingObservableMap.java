package org.wrml.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DelegatingObservableMap<K,V> extends AbstractObservableMap<K, V> {

    private Map<K, V> delegate;

    public DelegatingObservableMap(Map<K, V> delegate) {
        this.delegate = delegate;
    }

    public int size() {
        return delegate.size();
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public boolean containsKey(Object o) {
        return delegate.containsKey(o);
    }

    public boolean containsValue(Object o) {
        return delegate.containsValue(o);
    }

    public V get(Object o) {
        return delegate.get(o);
    }

    public V put(K k, V v) {
        return delegate.put(k, v);
    }

    public V remove(Object key) {

        fireRemovingEntryEvent(new MapEvent<K, V>(this, true, (K) key));
        final V removed = delegate.remove(key);

        fireEntryRemovedEvent(new MapEvent<K, V>(this, true, (K) key, null, removed));

        return removed;
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        delegate.putAll(map);
    }

    public void clear() {
        fireClearingEvent(new MapEvent<K, V>(this, true));
        delegate.clear();
        fireClearedEvent(new MapEvent<K, V>(this, true));
    }

    public Set<K> keySet() {
        return delegate.keySet();
    }

    public Collection<V> values() {
        return delegate.values();
    }

    public Set<Entry<K, V>> entrySet() {
        return delegate.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return delegate.equals(o);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }
}
