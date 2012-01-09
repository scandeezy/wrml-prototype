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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.wrml.util.Delegating;

public class DelegatingObservableList<E> extends AbstractObservableList<E> implements Delegating<List<E>> {

    private final List<E> _Delegate;

    public DelegatingObservableList(List<E> delegate) {
        _Delegate = delegate;
    }

    public boolean add(E e) {
        final ListEvent<E> insertingEvent = new ListEvent<E>(this, true, e, null);
        fireInsertingElementEvent(insertingEvent);

        if (insertingEvent.isCancelled()) {
            return false;
        }

        final boolean result = _Delegate.add(e);
        fireElementInsertedEvent(new ListEvent<E>(this, false, e, null));
        return result;
    }

    public void add(int i, E e) {
        _Delegate.add(i, e);
    }

    public boolean addAll(Collection<? extends E> es) {
        return _Delegate.addAll(es);
    }

    public boolean addAll(int i, Collection<? extends E> es) {
        return _Delegate.addAll(i, es);
    }

    public void clear() {
        final ListEvent<E> clearingEvent = new ListEvent<E>(this, true);
        fireClearingEvent(clearingEvent);

        if (!clearingEvent.isCancelled()) {
            _Delegate.clear();
            fireClearedEvent(new ListEvent<E>(this, false));
        }
    }

    public boolean contains(Object o) {
        return _Delegate.contains(o);
    }

    public boolean containsAll(Collection<?> objects) {
        return _Delegate.containsAll(objects);
    }

    @Override
    public boolean equals(Object o) {
        return _Delegate.equals(o);
    }

    public E get(int i) {
        return _Delegate.get(i);
    }

    public List<E> getDelegate() {
        return _Delegate;
    }

    @Override
    public int hashCode() {
        return _Delegate.hashCode();
    }

    public int indexOf(Object o) {
        return _Delegate.indexOf(o);
    }

    public boolean isEmpty() {
        return _Delegate.isEmpty();
    }

    public Iterator<E> iterator() {
        return _Delegate.iterator();
    }

    public int lastIndexOf(Object o) {
        return _Delegate.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return _Delegate.listIterator();
    }

    public ListIterator<E> listIterator(int i) {
        return _Delegate.listIterator(i);
    }

    public E remove(int i) {
        return _Delegate.remove(i);
    }

    public boolean remove(Object o) {
        final ListEvent<E> removingEvent = new ListEvent<E>(this, true, null, (E) o);
        fireRemovingElementEvent(removingEvent);
        if (removingEvent.isCancelled()) {
            return false;
        }
        final boolean result = _Delegate.remove(o);
        fireElementRemovedEvent(new ListEvent<E>(this, false, null, (E) o));
        return result;
    }

    public boolean removeAll(Collection<?> objects) {
        return _Delegate.removeAll(objects);
    }

    public boolean retainAll(Collection<?> objects) {
        return _Delegate.retainAll(objects);
    }

    public E set(int i, E e) {
        return _Delegate.set(i, e);
    }

    public int size() {
        return _Delegate.size();
    }

    public List<E> subList(int i, int i1) {
        return _Delegate.subList(i, i1);
    }

    public Object[] toArray() {
        return _Delegate.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return _Delegate.toArray(ts);
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" + (_Delegate != null ? "Delegate=" + _Delegate : "") + "]";
    }}
