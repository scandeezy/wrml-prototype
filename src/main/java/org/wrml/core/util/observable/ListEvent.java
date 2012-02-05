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

import java.util.EventObject;

/**
 * Fired from an ObservableList whenever its contents are altered.
 */
public class ListEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    private final int _Index;
    private final Object _InsertionElement;
    private final Object _RemovalElement;

    public ListEvent(ObservableList<?> list) {
        this(list, null, null);
    }

    public ListEvent(ObservableList<?> list, Object insertionElement, Object removalElement) {
        this(list, insertionElement, removalElement, -1);
    }

    public ListEvent(ObservableList<?> list, Object insertionElement, Object removalElement, int index) {
        super(list);
        _InsertionElement = insertionElement;
        _RemovalElement = removalElement;
        _Index = index;
    }

    public int getIndex() {
        return _Index;
    }

    public Object getInsertionElement() {
        return _InsertionElement;
    }

    public ObservableList<?> getList() {
        return (ObservableList<?>) getSource();
    }

    public Object getRemovalElement() {
        return _RemovalElement;
    }

}
