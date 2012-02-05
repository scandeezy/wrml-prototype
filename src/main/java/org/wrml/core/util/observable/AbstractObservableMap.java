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

package org.wrml.core.util.observable;

import org.wrml.core.event.EventSource;
import org.wrml.core.util.observable.MapEventListener.MapEventName;

/**
 * A Map that fires events when entries are inserted, updated, removed, or
 * cleared.
 * 
 * @param <K>
 *            The key type
 * @param <V>
 *            The value type
 */
public abstract class AbstractObservableMap<K, V> extends EventSource<MapEventListener> implements ObservableMap<K, V> {

    public AbstractObservableMap() {
        this(MapEventListener.class);
    }

    public AbstractObservableMap(Class<MapEventListener> listenerClass) {
        super(listenerClass);
    }

    public void fireMapCleared(MapEvent event) {
        fireEvent(MapEventName.mapCleared, event);
    }

    public boolean fireMapClearing(CancelableMapEvent event) {
        fireEvent(MapEventName.mapClearing, event);
        return !event.isCancelled();
    }

    public void fireMapEntryRemoved(MapEvent event) {
        fireEvent(MapEventName.mapEntryRemoved, event);
    }

    public void fireMapEntryUpdated(MapEvent event) {
        fireEvent(MapEventName.mapEntryUpdated, event);
    }

    public boolean fireMapRemovingEntry(CancelableMapEvent event) {
        fireEvent(MapEventName.mapRemovingEntry, event);
        return !event.isCancelled();
    }

    public boolean fireMapUpdatingEntry(CancelableMapEvent event) {
        fireEvent(MapEventName.mapUpdatingEntry, event);
        return !event.isCancelled();
    }

}
