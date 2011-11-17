
package org.wrml;

public interface WrmlObject {

    public void addEventListener(WrmlObjectEventListener listener);

    public Object getFieldValue(String fieldName);

    public Bag<String, Object> getFieldValues();

    public Link getLink(String relationName);

    public LinkFormula getLinkFormula(String relationName);

    public Bag<String, LinkFormula> getLinkFormulas();

    public Bag<String, Link> getLinks();

    public ResourceTemplate getResourceTemplate();

    public Schema getSchema();

    public void removeEventListener(WrmlObjectEventListener listener);

    public void setFieldValue(String fieldName, Object fieldValue);

}
