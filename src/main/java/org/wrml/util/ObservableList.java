package org.wrml.util;

import java.util.List;

public interface ObservableList<E> extends List<E> {
    void addListEventListener(ListEventListener<E> listEventListener);
    void removeListEventListener(ListEventListener<E> listEventListener);
}
