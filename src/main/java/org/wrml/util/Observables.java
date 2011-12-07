package org.wrml.util;

import java.util.Map;

/**
 * Utility factory methods for creating Observable instances
 *
 * @see java.util.Collections
 */
public final class Observables {
    private Observables() {}

    /**
     * Convenience utility method for decorating a Map as an ObservableMap.
     * @param map The Map to decorate
     * @param <K> The key type
     * @param <V> The value type
     * @return An ObservableMap backed by the given Map
     */
    public static <K, V> ObservableMap<K, V> observableMap(Map<K, V> map) {
        return new DelegatingObservableMap<K, V>(map);
    }
}
