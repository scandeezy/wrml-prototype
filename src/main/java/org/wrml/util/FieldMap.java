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

package org.wrml.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import org.wrml.TypeSystem;

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

    public Object put(final String fieldName, Object newValue) {
        return setFieldValue(fieldName, newValue);
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
        final Set<String> fieldNames = keySet();
        if (fieldNames == null) {
            return;
        }

        for (final String fieldName : fieldNames) {

            if (!isReadOnly(fieldName)) {

                Object currentValue = get(fieldName);

                if (currentValue instanceof Collection) {
                    ((Collection) currentValue).clear();
                }
                else {
                    put(fieldName, getDefaultValue(fieldName));
                }
            }
        }
    }

    protected final <V> V getDefaultValue(final String fieldName) {
        final Class<V> fieldType = getFieldType(fieldName);
        return (V) getDefaultValue(fieldType);
    }

    protected abstract SortedSet<String> getFieldNames();

    protected SortedMap<String, Object> getFields() {
        final Set<String> fieldNames = keySet();
        if (fieldNames == null) {
            return null;
        }

        final SortedMap<String, Object> fields = new TreeMap<String, Object>();
        for (final String fieldName : fieldNames) {
            fields.put(fieldName, getFieldValue(fieldName));
        }
        return fields;
    }

    protected abstract <V> Class<V> getFieldType(final String fieldName);

    protected <V> V getFieldValue(final String fieldName) {
        return getRawFieldValue(fieldName);
    }

    protected abstract <V> V getRawFieldValue(final String fieldName);

    protected boolean isFieldValueSet(final String fieldName) {
        return getFieldNames().contains(fieldName);
    }

    protected abstract boolean isReadOnly(final String fieldName);

    protected void setFields(Map<String, Object> fields) {
        for (final String fieldName : fields.keySet()) {
            setFieldValue(fieldName, fields.get(fieldName));
        }
    }

    @SuppressWarnings("unchecked")
    protected final <V> V setFieldValue(final String fieldName, V newValue) {

        final V oldFieldValue = getFieldValue(fieldName);

        if ((oldFieldValue == null) && (newValue == null)) {
            return null;
        }

        if (((oldFieldValue != null) && oldFieldValue.equals(newValue))
                || ((newValue != null) && newValue.equals(oldFieldValue))) {

            return oldFieldValue;
        }

        return (V) setRawFieldValue(fieldName, newValue);
    }

    protected abstract <V> V setRawFieldValue(final String fieldName, final V fieldValue);

    protected final <V> V getDefaultValue(Class<V> fieldType) {
        return TypeSystem.instance.getDefaultValue(fieldType);
    }

    @Override
    public String toString() {
        return getClass().getName() + " { fieldNames : [ " + getFieldNames() + " ] }";
    }

}
