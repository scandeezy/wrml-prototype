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

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractObservableList<E> implements ObservableList<E> {

    private final List<ListEventListener<E>> listeners = new LinkedList<ListEventListener<E>>();

    public void addListEventListener(ListEventListener<E> listener) {
        listeners.add(listener);
    }

    public void removeListEventListener(ListEventListener<E> listener) {
        listeners.remove(listener);
    }

    protected final void fireClearedEvent(ListEvent<E> event) {
        fireEvent(event, new ListEventHandler<E>() {

            public void handleEvent(ListEvent<E> event, ListEventListener<E> listener) {
                listener.cleared(event);
            }
        });
    }

    protected final void fireClearingEvent(ListEvent<E> event) {
        fireEvent(event, new ListEventHandler<E>() {

            public void handleEvent(ListEvent<E> event, ListEventListener<E> listener) {
                listener.clearing(event);
            }
        });
    }

    protected final void fireElementInsertedEvent(ListEvent<E> event) {
        fireEvent(event, new ListEventHandler<E>() {

            public void handleEvent(ListEvent<E> event, ListEventListener<E> listener) {
                listener.elementInserted(event);
            }
        });
    }

    protected final void fireElementRemovedEvent(ListEvent<E> event) {
        fireEvent(event, new ListEventHandler<E>() {

            public void handleEvent(ListEvent<E> event, ListEventListener<E> listener) {
                listener.elementRemoved(event);
            }
        });
    }

    protected final void fireElementUpdatedEvent(ListEvent<E> event) {
        fireEvent(event, new ListEventHandler<E>() {

            public void handleEvent(ListEvent<E> event, ListEventListener<E> listener) {
                listener.elementUpdated(event);
            }
        });
    }

    protected void fireEvent(ListEvent<E> event, ListEventHandler handler) {
        for (final ListEventListener<E> listener : listeners) {
            handler.handleEvent(event, listener);
        }
    }

    protected final void fireInsertingElementEvent(ListEvent<E> event) {
        fireEvent(event, new ListEventHandler<E>() {

            public void handleEvent(ListEvent<E> event, ListEventListener<E> listener) {
                listener.insertingElement(event);
            }
        });
    }

    protected final void fireRemovingElementEvent(ListEvent<E> event) {
        fireEvent(event, new ListEventHandler<E>() {

            public void handleEvent(ListEvent<E> event, ListEventListener<E> listener) {
                listener.removingElement(event);
            }
        });
    }

    protected final void fireUpdatingElementEvent(ListEvent<E> event) {
        fireEvent(event, new ListEventHandler<E>() {

            public void handleEvent(ListEvent<E> event, ListEventListener<E> listener) {
                listener.updatingElement(event);
            }
        });
    }

    private interface ListEventHandler<E> {

        void handleEvent(ListEvent<E> event, ListEventListener<E> listener);
    }
}
