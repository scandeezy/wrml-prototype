
package org.wrml;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Bag<K, V> extends Map<K, V> {

    public void addBagEventListener(BagEventListener<K, V> listener);

    public List<V> getAsList();

    public Set<V> getAsSet();

    public void removeBagEventListener(BagEventListener<K, V> listener);

}
