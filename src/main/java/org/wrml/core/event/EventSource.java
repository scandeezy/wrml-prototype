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

package org.wrml.core.event;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventSource<L extends java.util.EventListener> {

    private final transient Class<L> _EventListenerClass;
    private transient EventListenerList<L> _EventListeners;
    private transient Set<L> _EventListenerSet;

    public EventSource(Class<L> eventListenerClass) {
        _EventListenerClass = eventListenerClass;
    }

    public boolean addEventListener(L eventListener) {
        if (_EventListeners == null) {
            init();
        }

        if (_EventListenerSet.contains(eventListener)) {
            return false;
        }

        _EventListenerSet.add(eventListener);

        return _EventListeners.add(eventListener);
    }

    public void fireEvent(Enum<?> eventEnum, EventObject event) {
        fireEvent(String.valueOf(eventEnum), event);
    }

    public void fireEvent(String eventName, EventObject event) {
        if (_EventListeners == null) {
            return;
        }

        _EventListeners.fireEvent(eventName, event);
    }

    public void free() {

        if (_EventListeners != null) {
            _EventListeners.clear();
            _EventListeners = null;
        }

        if (_EventListenerSet != null) {
            _EventListenerSet.clear();
            _EventListenerSet = null;
        }

    }

    public Class<L> getEventListenerClass() {
        return _EventListenerClass;
    }

    public int getEventListenerCount() {
        return (_EventListenerSet != null) ? _EventListenerSet.size() : 0;
    }

    public EventListenerList<L> getEventListeners() {
        if (_EventListeners == null) {
            init();
        }
        return _EventListeners;
    }

    public Set<L> getEventListenerSet() {
        if (_EventListenerSet == null) {
            init();
        }
        return _EventListenerSet;
    }

    public boolean isEventHearable() {
        return getEventListenerCount() > 0;
    }

    public boolean removeEventListener(L eventListener) {
        if (_EventListeners == null) {
            return false;
        }
        return _EventListeners.remove(eventListener);
    }

    private void init() {
        _EventListeners = new DelegatingEventListenerList<L>(getEventListenerClass(), new CopyOnWriteArrayList<L>());
        _EventListenerSet = new HashSet<L>();
    }

}
