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

package org.wrml.runtime;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

public abstract class FieldMap implements Map<String, Object> {

    //
    // Map API
    //

    public void clear() {
        clearFields();
    }

    public boolean containsKey(final Object fieldName) {
        return isFieldValueSet((String) fieldName);
    }

    public boolean containsValue(final Object value) {
        return getFields().containsValue(value);
    }

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        return getFields().entrySet();
    }

    public Object get(final Object fieldName) {
        return getFieldValue((String) fieldName);
    }

    public boolean isEmpty() {
        return keySet().isEmpty();
    }

    public Set<String> keySet() {
        return getFieldNames();
    }

    public Object put(final String fieldName, Object fieldValue) {
        return setFieldValue(fieldName, fieldValue);
    }

    @SuppressWarnings("unchecked")
    public void putAll(final Map<? extends String, ? extends Object> fields) {
        setFields((Map<String, Object>) fields);
    }

    public Object remove(final Object fieldName) {
        return put((String) fieldName, getDefaultValue((String) fieldName));
    }

    public int size() {
        return keySet().size();
    }

    public Collection<Object> values() {
        return getFields().values();
    }

    //
    // Field-access API
    //

    protected void clearFields() {
        Set<String> fieldNames = keySet();
        if (fieldNames == null) {
            return;
        }

        for (String fieldName : fieldNames) {

            if (!isReadOnly(fieldName)) {
                put(fieldName, getDefaultValue(fieldName));
            }
        }

    }

    protected final Object getDefaultValue(final String fieldName) {
        final Class<?> fieldType = getFieldType(fieldName);
        return getDefaultValue(fieldType);
    }

    private Object getDefaultValue(Class<?> fieldType) {        
        return TypeSystem.instance.getDefaultValue(fieldType);
    }

    protected abstract SortedSet<String> getFieldNames();

    protected SortedMap<String, Object> getFields() {
        Set<String> fieldNames = keySet();
        if (fieldNames == null) {
            return null;
        }

        SortedMap<String, Object> fields = new TreeMap<String, Object>();
        for (String fieldName : fieldNames) {
            fields.put(fieldName, getFieldValue(fieldName));
        }
        return fields;
    }

    protected abstract Class<?> getFieldType(final String fieldName);

    protected Object getFieldValue(final String fieldName) {
        Object fieldValue = getRawFieldValue(fieldName);

        if (fieldValue == null) {
            Class<?> fieldType = getFieldType(fieldName);
            fieldValue = getDefaultValue(fieldType);
        }

        return fieldValue;
    }

    protected abstract Object getRawFieldValue(final String fieldName);

    protected boolean isFieldValueSet(final String fieldName) {
        return getFieldNames().contains(fieldName);
    }

    protected abstract boolean isReadOnly(final String fieldName);

    protected void setFields(Map<String, Object> fields) {
        for (String fieldName : fields.keySet()) {
            put(fieldName, fields.get(fieldName));
        }
    }

    protected final Object setFieldValue(final String fieldName, final Object fieldValue) {

        Object oldFieldValue = getFieldValue(fieldName);
        Object newFieldValue = fieldValue;

        if (oldFieldValue == null && newFieldValue == null) {
            return null;
        }

        if ((oldFieldValue != null && oldFieldValue.equals(newFieldValue))
                || (newFieldValue != null && newFieldValue.equals(oldFieldValue))) {

            return oldFieldValue;
        }

        if (newFieldValue == null) {
            Class<?> fieldType = getFieldType(fieldName);
            newFieldValue = getDefaultValue(fieldType);
        }
        else if (newFieldValue instanceof Boolean) {
            Boolean booleanFieldValue = (Boolean) newFieldValue;
            newFieldValue = booleanFieldValue.booleanValue() ? Boolean.TRUE : Boolean.FALSE;
        }

        return setRawFieldValue(fieldName, newFieldValue);
    }

    protected abstract Object setRawFieldValue(final String fieldName, final Object fieldValue);

}
