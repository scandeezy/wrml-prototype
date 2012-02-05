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
import org.wrml.core.util.observable.ListEventListener.ListEventName;

public abstract class AbstractObservableList<E> extends EventSource<ListEventListener> implements ObservableList<E> {

    public AbstractObservableList() {
        this(ListEventListener.class);
    }

    public AbstractObservableList(Class<ListEventListener> listenerClass) {
        super(listenerClass);
    }

    protected void fireListCleared(ListEvent event) {
        fireEvent(ListEventName.listCleared, event);
    }

    protected boolean fireListClearing(CancelableListEvent event) {
        fireEvent(ListEventName.listClearing, event);
        return !event.isCancelled();
    }

    protected void fireListElementInserted(ListEvent event) {
        fireEvent(ListEventName.listElementInserted, event);
    }

    protected void fireListElementRemoved(ListEvent event) {
        fireEvent(ListEventName.listElementRemoved, event);
    }

    protected void fireListElementUpdated(ListEvent event) {
        fireEvent(ListEventName.listElementUpdated, event);
    }

    protected boolean fireListInsertingElement(CancelableListEvent event) {
        fireEvent(ListEventName.listInsertingElement, event);
        return !event.isCancelled();
    }

    protected boolean fireListRemovingElement(CancelableListEvent event) {
        fireEvent(ListEventName.listRemovingElement, event);
        return !event.isCancelled();
    }

    protected boolean fireListUpdatingElement(CancelableListEvent event) {
        fireEvent(ListEventName.listUpdatingElement, event);
        return !event.isCancelled();
    }

}
