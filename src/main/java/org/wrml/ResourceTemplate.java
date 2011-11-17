
package org.wrml;

public interface ResourceTemplate {

    public String getName();
    
    public String getPath();

    public ResourceTemplate getParent();

    public ResourceArchetype getResourceArchetype();

    public UriTemplate getUriTemplate();

    /*
     * 
     * 
     * 
     * public Set<Link> getEndingLinks(); public Set<Link> getBeginningLinks();
     * 
     * Current Schema Past Schema (list) Link Templates per schema (list) Field
     * Templates per schema (list)
     */
}
