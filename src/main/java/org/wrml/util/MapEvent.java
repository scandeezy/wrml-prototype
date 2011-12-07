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

/**
 * Fired from an ObservableMap whenever its contents are altered.
 * 
 * @param <K>
 *            The key type
 * @param <V>
 *            The value type
 */
public final class MapEvent<K, V> extends CancelableEvent {

    private static final long serialVersionUID = -4697339990791750523L;

    private final K _Key;
    private final V _NewValue;
    private final V _OldValue;

    public MapEvent(ObservableMap<K, V> map, boolean cancelable) {
        this(map, cancelable, null, null, null);
    }

    public MapEvent(ObservableMap<K, V> map, boolean cancelable, K _Key) {
        this(map, cancelable, _Key, null, null);
    }

    public MapEvent(ObservableMap<K, V> map, boolean cancelable, K key, V newValue, V oldValue) {
        super(map, cancelable);
        _Key = key;
        _NewValue = newValue;
        _OldValue = oldValue;
    }

    public K getKey() {
        return _Key;
    }

    @SuppressWarnings("unchecked")
    public ObservableMap<K, V> getMap() {
        return (ObservableMap<K, V>) getSource();
    }

    public V getNewValue() {
        return _NewValue;
    }

    public V getOldValue() {
        return _OldValue;
    }

}
