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

package org.wrml.event;

import java.util.ArrayList;
import java.util.EventObject;

public class EventSource<L extends java.util.EventListener> {

    private final transient Class<L> _EventListenerClass;
    private transient EventListenerList<L> _EventListeners;

    public EventSource(Class<L> eventListenerClass) {
        _EventListenerClass = eventListenerClass;
    }

    public boolean addEventListener(L eventListener) {
        if (_EventListeners == null) {
            _EventListeners = createEventListenerList();
        }
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

    public Class<L> getEventListenerClass() {
        return _EventListenerClass;
    }

    public boolean removeEventListener(L eventListener) {
        if (_EventListeners == null) {
            return false;
        }
        return _EventListeners.remove(eventListener);
    }

    private EventListenerList<L> createEventListenerList() {
        return new DelegatingEventListenerList<L>(getEventListenerClass(), new ArrayList<L>());
    }

}
