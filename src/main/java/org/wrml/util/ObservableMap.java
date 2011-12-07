package org.wrml.util;

import java.util.Map;

public interface ObservableMap<K, V> extends Map<K, V> {
    void addEventListener(MapEventListener<K, V> listener);
    void removeEventListener(MapEventListener<K, V> listener);
}
