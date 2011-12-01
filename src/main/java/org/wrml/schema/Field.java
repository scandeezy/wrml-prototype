/**
 * Copyright (C) 2011 WRML.org <mark@wrml.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wrml.schema;

import java.net.URI;

import org.wrml.WrmlObject;
import org.wrml.util.ObservableList;

/**
 * A web resource schema's field. Conceptually a field is a typed data slot,
 * like a field in a Java object or a field on a web form. Instances of this
 * class group the metadata associated with a specific schema's field.
 * 
 */
public final class Field<T> extends Member<String> {

    private static final long serialVersionUID = -3759803881552241076L;

    private String _Name;
    private String _Description;
    private String _Title;

    private T _DefaultValue;

    private boolean _ReadOnly;
    private boolean _Hidden;
    private boolean _Required;
    private boolean _Transient;

    private ObservableList<URI> _ConstraintIds;

    public Field(Schema schema, URI declaredSchemaId) {
        super(schema, declaredSchemaId);
    }

    public Field(Schema schema, URI declaredSchemaId, String name) {
        super(schema, declaredSchemaId);
        setName(name);
    }

    public ObservableList<URI> getConstraintIds() {
        return _ConstraintIds;
    }

    public T getDefaultValue() {
        return _DefaultValue;
    }

    public final String getDescription() {
        return _Description;
    }

    /**
     * Returns the value of the field represented by this Field, on the
     * specified WRML object.
     * 
     * @param wrmlObject
     *            the instance
     * @return the field value on the instance
     */
    @SuppressWarnings("unchecked")
    public T getFieldValue(WrmlObject wrmlObject) {

        if (wrmlObject == null) {
            return null;
        }

        return (T) wrmlObject.getFieldValue(getName());
    }

    public String getName() {
        return _Name;
    }

    public final String getTitle() {
        return _Title;
    }

    public boolean isHidden() {
        return _Hidden;
    }

    public boolean isReadOnly() {
        return _ReadOnly;
    }

    public boolean isRequired() {
        return _Required;
    }

    public boolean isTransient() {
        return _Transient;
    }

    public void setConstraintIds(ObservableList<URI> constraintIds) {
        _ConstraintIds = constraintIds;
    }

    public void setDefaultValue(T defaultValue) {
        _DefaultValue = defaultValue;
    }

    public final void setDescription(String description) {
        _Description = description;
    }

    /**
     * Sets the field represented by this Field object on the specified WRML
     * object argument to the specified new value.
     * 
     * @param wrmlObject
     *            the instance whose field should be modified
     * @param value
     *            the new value for the field of instance being modified
     * @return the previous value of the field of the instance being modified
     */
    @SuppressWarnings("unchecked")
    public T setFieldValue(WrmlObject wrmlObject, T value) {
        return (T) wrmlObject.setFieldValue(getName(), value);
    }

    public void setHidden(boolean hidden) {
        _Hidden = hidden;
    }

    public void setName(String name) {
        _Name = name;
        setId(name);
    }

    public void setReadOnly(boolean readOnly) {
        _ReadOnly = readOnly;
    }

    public void setRequired(boolean required) {
        _Required = required;
    }

    public final void setTitle(String title) {
        _Title = title;
    }

    public void setTransient(boolean transientFlag) {
        _Transient = transientFlag;
    }

}
