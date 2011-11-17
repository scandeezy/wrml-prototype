
package org.wrml;

public interface WrmlObjectEventListener {

    public void fieldValueChanged(FieldEvent event);

    public void fieldValueInitialized(FieldEvent event);
}
