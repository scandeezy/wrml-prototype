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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.wrml.core.util.Delegating;

public class DelegatingObservableList<E> extends AbstractObservableList<E> implements Delegating<List<E>> {

    private final List<E> _Delegate;

    public DelegatingObservableList(Class<ListEventListener> listenerClass, List<E> delegate) {
        super(listenerClass);
        _Delegate = delegate;
    }

    public DelegatingObservableList(List<E> delegate) {
        this(ListEventListener.class, delegate);
    }

    public boolean add(E insertionElement) {

        if (isEventHearable() && !fireListInsertingElement(new CancelableListEvent(this, insertionElement, null))) {
            return false;
        }

        final boolean added = _Delegate.add(insertionElement);

        if (added && isEventHearable()) {
            fireListElementInserted(new ListEvent(this, insertionElement, null));
        }

        return added;
    }

    public void add(int index, E insertionElement) {

        if (isEventHearable()
                && !fireListInsertingElement(new CancelableListEvent(this, insertionElement, null, index))) {
            return;
        }

        _Delegate.add(index, insertionElement);

        if (isEventHearable()) {
            fireListElementInserted(new ListEvent(this, insertionElement, null, index));
        }
    }

    public boolean addAll(Collection<? extends E> insertionElements) {

        if (isEventHearable()) {
            int elementIndex = 0;
            for (final Object insertionElement : insertionElements) {
                if (!fireListInsertingElement(new CancelableListEvent(this, insertionElement, null, elementIndex++))) {
                    // TODO: Filter out the unwanted elements and build a list of acceptable ones instead?
                    return false;
                }
            }
        }

        final boolean added = _Delegate.addAll(insertionElements);

        if (added && isEventHearable()) {
            int elementIndex = 0;
            for (final Object insertionElement : insertionElements) {
                fireListElementInserted(new ListEvent(this, insertionElement, null, elementIndex++));
            }
        }

        return added;

    }

    public boolean addAll(int index, Collection<? extends E> insertionElements) {

        if (isEventHearable()) {
            int elementIndex = index;
            for (final Object insertionElement : insertionElements) {
                if (!fireListInsertingElement(new CancelableListEvent(this, insertionElement, null, elementIndex++))) {
                    // TODO: Filter out the unwanted elements and build a list of acceptable ones instead?
                    return false;
                }
            }
        }

        final boolean added = _Delegate.addAll(index, insertionElements);

        if (added && isEventHearable()) {
            int elementIndex = index;
            for (final Object insertionElement : insertionElements) {
                fireListElementInserted(new ListEvent(this, insertionElement, null, elementIndex++));
            }
        }

        return added;
    }

    public void clear() {

        if (isEventHearable() && !fireListClearing(new CancelableListEvent(this))) {
            return;
        }

        _Delegate.clear();

        if (isEventHearable()) {
            fireListCleared(new ListEvent(this));
        }
    }

    public boolean contains(Object element) {
        return _Delegate.contains(element);
    }

    public boolean containsAll(Collection<?> elements) {
        return _Delegate.containsAll(elements);
    }

    @Override
    public boolean equals(Object otherList) {
        return _Delegate.equals(otherList);
    }

    public E get(int indexOfElement) {
        return _Delegate.get(indexOfElement);
    }

    public List<E> getDelegate() {
        return _Delegate;
    }

    @Override
    public int hashCode() {
        return _Delegate.hashCode();
    }

    public int indexOf(Object element) {
        return _Delegate.indexOf(element);
    }

    public boolean isEmpty() {
        return _Delegate.isEmpty();
    }

    public Iterator<E> iterator() {
        return _Delegate.iterator();
    }

    public int lastIndexOf(Object element) {
        return _Delegate.lastIndexOf(element);
    }

    public ListIterator<E> listIterator() {
        return _Delegate.listIterator();
    }

    public ListIterator<E> listIterator(int iterationStartIndex) {
        return _Delegate.listIterator(iterationStartIndex);
    }

    public E remove(int indexOfElement) {

        E removalElement = null;

        if (isEventHearable()) {
            removalElement = get(indexOfElement);
            if (!fireListRemovingElement(new CancelableListEvent(this, null, removalElement, indexOfElement))) {
                return null;
            }
        }

        removalElement = _Delegate.remove(indexOfElement);

        if (isEventHearable()) {
            fireListElementRemoved(new ListEvent(this, null, removalElement, indexOfElement));
        }

        return removalElement;
    }

    public boolean remove(Object removalElement) {

        if (isEventHearable() && !fireListRemovingElement(new CancelableListEvent(this, null, removalElement))) {
            return false;
        }

        final boolean removed = _Delegate.remove(removalElement);

        if (removed && isEventHearable()) {
            fireListElementRemoved(new ListEvent(this, null, removalElement));
        }

        return removed;
    }

    public boolean removeAll(Collection<?> removalElements) {

        if (isEventHearable()) {
            int elementIndex = 0;
            for (final Object removalElement : removalElements) {
                if (!fireListRemovingElement(new CancelableListEvent(this, null, removalElement, elementIndex++))) {
                    // TODO: Filter out the unwanted elements and build a list of acceptable ones instead?
                    return false;
                }
            }
        }

        final boolean removed = _Delegate.removeAll(removalElements);

        if (removed && isEventHearable()) {
            int elementIndex = 0;
            for (final Object removalElement : removalElements) {
                fireListElementRemoved(new ListEvent(this, null, removalElement, elementIndex++));
            }
        }

        return removed;
    }

    public boolean retainAll(Collection<?> elements) {
        return _Delegate.retainAll(elements);
    }

    public E set(int indexOfElement, E insertionElement) {

        E removalElement = null;
        if (isEventHearable()) {
            removalElement = get(indexOfElement);
            if (!fireListUpdatingElement(new CancelableListEvent(this, insertionElement, removalElement, indexOfElement))) {
                return null;
            }
        }

        removalElement = _Delegate.set(indexOfElement, insertionElement);

        if (isEventHearable()) {
            fireListElementUpdated(new ListEvent(this, insertionElement, removalElement, indexOfElement));
        }

        return removalElement;
    }

    public int size() {
        return _Delegate.size();
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return _Delegate.subList(fromIndex, toIndex);
    }

    public Object[] toArray() {
        return _Delegate.toArray();
    }

    public <T> T[] toArray(T[] arrayToHoldElements) {
        return _Delegate.toArray(arrayToHoldElements);
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" + (_Delegate != null ? "Delegate=" + _Delegate : "") + "]";
    }

}
