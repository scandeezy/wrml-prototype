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

import java.util.LinkedList;
import java.util.List;

/**
 * A Map that fires events when entries are inserted, updated, removed, or
 * cleared.
 * 
 * @param <K>
 *            The key type
 * @param <V>
 *            The value type
 */
public abstract class AbstractObservableMap<K, V> implements ObservableMap<K, V> {

    private final List<MapEventListener<K, V>> mapEventListeners = new LinkedList<MapEventListener<K, V>>();

    public void addMapEventListener(MapEventListener<K, V> mapEventListener) {
        mapEventListeners.add(mapEventListener);
    }

    public void removeMapEventListener(MapEventListener<K, V> mapEventListener) {
        mapEventListeners.remove(mapEventListener);
    }

    protected final void fireClearedEvent(MapEvent<K, V> mapEvent) {
        fireEvent(mapEvent, new MapEventHandler<K, V>() {

            public void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener) {
                mapEventListener.cleared(mapEvent);
            }
        });
    }

    protected final void fireClearingEvent(MapEvent<K, V> mapEvent) {
        fireEvent(mapEvent, new MapEventHandler<K, V>() {

            public void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener) {
                mapEventListener.clearing(mapEvent);
            }
        });
    }

    protected final void fireEntryInsertedEvent(MapEvent<K, V> mapEvent) {
        fireEvent(mapEvent, new MapEventHandler<K, V>() {

            public void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener) {
                mapEventListener.entryInserted(mapEvent);
            }
        });
    }

    protected final void fireEntryRemovedEvent(MapEvent<K, V> mapEvent) {
        fireEvent(mapEvent, new MapEventHandler<K, V>() {

            public void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener) {
                mapEventListener.entryRemoved(mapEvent);
            }
        });
    }

    protected final void fireEntryUpdatedEvent(MapEvent<K, V> mapEvent) {
        fireEvent(mapEvent, new MapEventHandler<K, V>() {

            public void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener) {
                mapEventListener.entryUpdated(mapEvent);
            }
        });
    }

    protected void fireEvent(MapEvent<K, V> mapEvent, MapEventHandler handler) {
        for (final MapEventListener<K, V> mapEventListener : mapEventListeners) {
            handler.handleEvent(mapEvent, mapEventListener);
        }
    }

    protected final void fireInsertingEntryEvent(MapEvent<K, V> mapEvent) {
        fireEvent(mapEvent, new MapEventHandler<K, V>() {

            public void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener) {
                mapEventListener.insertingEntry(mapEvent);
            }
        });
    }

    protected final void fireRemovingEntryEvent(MapEvent<K, V> mapEvent) {
        fireEvent(mapEvent, new MapEventHandler<K, V>() {

            public void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener) {
                mapEventListener.removingEntry(mapEvent);
            }
        });
    }

    protected final void fireUpdatingEntryEvent(MapEvent<K, V> mapEvent) {
        fireEvent(mapEvent, new MapEventHandler<K, V>() {

            public void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener) {
                mapEventListener.updatingEntry(mapEvent);
            }
        });
    }

    private interface MapEventHandler<K, V> {

        void handleEvent(MapEvent<K, V> mapEvent, MapEventListener<K, V> mapEventListener);
    }
}
