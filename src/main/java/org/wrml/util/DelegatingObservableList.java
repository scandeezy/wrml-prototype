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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

final class DelegatingObservableList<E> extends AbstractObservableList<E> {

    private final List<E> backingList;

    public DelegatingObservableList(List<E> backingList) {
        this.backingList = backingList;
    }

    public boolean add(E e) {
        final ListEvent<E> insertingEvent = new ListEvent<E>(this, true, e, null);
        fireInsertingElementEvent(insertingEvent);

        if (insertingEvent.isCancelled()) {
            return false;
        }

        final boolean result = backingList.add(e);
        fireElementInsertedEvent(new ListEvent<E>(this, false, e, null));
        return result;
    }

    public void add(int i, E e) {
        backingList.add(i, e);
    }

    public boolean addAll(Collection<? extends E> es) {
        return backingList.addAll(es);
    }

    public boolean addAll(int i, Collection<? extends E> es) {
        return backingList.addAll(i, es);
    }

    public void clear() {
        final ListEvent<E> clearingEvent = new ListEvent<E>(this, true);
        fireClearingEvent(clearingEvent);

        if (!clearingEvent.isCancelled()) {
            backingList.clear();
            fireClearedEvent(new ListEvent<E>(this, false));
        }
    }

    public boolean contains(Object o) {
        return backingList.contains(o);
    }

    public boolean containsAll(Collection<?> objects) {
        return backingList.containsAll(objects);
    }

    @Override
    public boolean equals(Object o) {
        return backingList.equals(o);
    }

    public E get(int i) {
        return backingList.get(i);
    }

    @Override
    public int hashCode() {
        return backingList.hashCode();
    }

    public int indexOf(Object o) {
        return backingList.indexOf(o);
    }

    public boolean isEmpty() {
        return backingList.isEmpty();
    }

    public Iterator<E> iterator() {
        return backingList.iterator();
    }

    public int lastIndexOf(Object o) {
        return backingList.lastIndexOf(o);
    }

    public ListIterator<E> listIterator() {
        return backingList.listIterator();
    }

    public ListIterator<E> listIterator(int i) {
        return backingList.listIterator(i);
    }

    public E remove(int i) {
        return backingList.remove(i);
    }

    public boolean remove(Object o) {
        final ListEvent<E> removingEvent = new ListEvent<E>(this, true, null, (E) o);
        fireRemovingElementEvent(removingEvent);
        if (removingEvent.isCancelled()) {
            return false;
        }
        final boolean result = backingList.remove(o);
        fireElementRemovedEvent(new ListEvent<E>(this, false, null, (E) o));
        return result;
    }

    public boolean removeAll(Collection<?> objects) {
        return backingList.removeAll(objects);
    }

    public boolean retainAll(Collection<?> objects) {
        return backingList.retainAll(objects);
    }

    public E set(int i, E e) {
        return backingList.set(i, e);
    }

    public int size() {
        return backingList.size();
    }

    public List<E> subList(int i, int i1) {
        return backingList.subList(i, i1);
    }

    public Object[] toArray() {
        return backingList.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return backingList.toArray(ts);
    }
}
