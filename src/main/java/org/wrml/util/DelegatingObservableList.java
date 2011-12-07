package org.wrml.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class DelegatingObservableList<E> extends AbstractObservableList<E> {

    private List<E> backingList;

    public DelegatingObservableList(List<E> backingList) {
        this.backingList = backingList;
    }

    public int size() {
        return backingList.size();
    }

    public boolean isEmpty() {
        return backingList.isEmpty();
    }

    public boolean contains(Object o) {
        return backingList.contains(o);
    }

    public Iterator<E> iterator() {
        return backingList.iterator();
    }

    public Object[] toArray() {
        return backingList.toArray();
    }

    public <T> T[] toArray(T[] ts) {
        return backingList.toArray(ts);
    }

    public boolean add(E e) {
        final ListEvent<E> insertingEvent = new ListEvent<E>(this, true, e, null);
        fireInsertingElementEvent(insertingEvent);

        if(insertingEvent.isCancelled()) {
            return false;
        }

        final boolean result = backingList.add(e);
        fireElementInsertedEvent(new ListEvent<E>(this, false, e, null));
        return result;
    }

    public boolean remove(Object o) {
        final ListEvent<E> removingEvent = new ListEvent<E>(this, true, null, (E) o);
        fireRemovingElementEvent(removingEvent);
        if(removingEvent.isCancelled()) {
            return false;
        }
        final boolean result = backingList.remove(o);
        fireElementRemovedEvent(new ListEvent<E>(this, false, null, (E) o));
        return result;
    }

    public boolean containsAll(Collection<?> objects) {
        return backingList.containsAll(objects);
    }

    public boolean addAll(Collection<? extends E> es) {
        return backingList.addAll(es);
    }

    public boolean addAll(int i, Collection<? extends E> es) {
        return backingList.addAll(i, es);
    }

    public boolean removeAll(Collection<?> objects) {
        return backingList.removeAll(objects);
    }

    public boolean retainAll(Collection<?> objects) {
        return backingList.retainAll(objects);
    }

    public void clear() {
        final ListEvent<E> clearingEvent = new ListEvent<E>(this, true);
        fireClearingEvent(clearingEvent);

        if(!clearingEvent.isCancelled()) {
            backingList.clear();
            fireClearedEvent(new ListEvent<E>(this, false));
        }
    }

    @Override
    public boolean equals(Object o) {
        return backingList.equals(o);
    }

    @Override
    public int hashCode() {
        return backingList.hashCode();
    }

    public E get(int i) {
        return backingList.get(i);
    }

    public E set(int i, E e) {
        return backingList.set(i, e);
    }

    public void add(int i, E e) {
        backingList.add(i, e);
    }

    public E remove(int i) {
        return backingList.remove(i);
    }

    public int indexOf(Object o) {
        return backingList.indexOf(o);
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

    public List<E> subList(int i, int i1) {
        return backingList.subList(i, i1);
    }
}
