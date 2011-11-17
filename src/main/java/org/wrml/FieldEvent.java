
package org.wrml;

public interface FieldEvent {

    public String getFieldName();

    public Object getNewFieldValue();

    public Object getOldFieldValue();
}
