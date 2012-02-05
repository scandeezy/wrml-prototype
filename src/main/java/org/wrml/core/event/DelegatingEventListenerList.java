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
import java.util.List;

import org.wrml.core.util.observable.DelegatingObservableList;

public class DelegatingEventListenerList<L extends java.util.EventListener> extends DelegatingObservableList<L>
        implements EventListenerList<L> {

    private final Class<L> _EventListenerElementType;
    private EventListenerPrototype<L> _EventListenerPrototype;

    public DelegatingEventListenerList(Class<L> eventListenerElementType, List<L> delegate) {
        super(delegate);
        _EventListenerElementType = eventListenerElementType;
    }

    @Override
    public void fireEvent(String eventName, EventObject event) {
        if (_EventListenerPrototype == null) {
            _EventListenerPrototype = EventListenerPrototype.forEventListenerClass(getEventListenerElementType());
        }

        _EventListenerPrototype.fireEvent(eventName, event, getDelegate());
    }

    public Class<L> getEventListenerElementType() {
        return _EventListenerElementType;
    }

}
