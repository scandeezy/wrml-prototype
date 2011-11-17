
package org.wrml;

public interface Link {

    public void addEventListener(LinkEventListener listener);

    public ResourceTemplate getDestination();

    public LinkFormula getEnabledFormula();

    public LinkRelation getRelation();

    public Bag<String, MediaType> getRequestMediaTypes();

    public Bag<String, MediaType> getResponseMediaTypes();

    public ResourceTemplate getSource();

    public boolean isEnabled();

    public void removeEventListener(LinkEventListener listener);
}
