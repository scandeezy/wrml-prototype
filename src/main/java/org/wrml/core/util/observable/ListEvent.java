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

import org.wrml.core.util.CancelableEvent;

/**
 * Fired from an ObservableList whenever its contents are altered.
 * 
 * @param <K>
 *            The key type
 * @param <V>
 *            The value type
 */
public final class ListEvent<T> extends CancelableEvent {

    private static final long serialVersionUID = 3666047515348093100L;

    private final T _NewValue;
    private final T _OldValue;

    public ListEvent(ObservableList<T> list, boolean cancelable) {
        this(list, cancelable, null, null);
    }

    public ListEvent(ObservableList<T> list, boolean cancelable, T newValue, T oldValue) {
        super(list, cancelable);
        _NewValue = newValue;
        _OldValue = oldValue;
    }

    @SuppressWarnings("unchecked")
    public ObservableList<T> getList() {
        return (ObservableList<T>) getSource();
    }

    public T getNewValue() {
        return _NewValue;
    }

    public T getOldValue() {
        return _OldValue;
    }

}
