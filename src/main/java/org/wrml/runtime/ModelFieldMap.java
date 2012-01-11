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

import java.net.URI;
import java.util.Map;

import org.wrml.TypeSystem;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Type;
import org.wrml.util.DelegatingFieldMap;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;

public class ModelFieldMap extends DelegatingFieldMap {

    private final transient Context _Context;
    private final URI _SchemaId;

    public ModelFieldMap(final Context context, final URI schemaId, final Map<String, Object> delegate) {
        super(delegate);

        _Context = context;
        _SchemaId = schemaId;
    }

    public final Context getContext() {
        return _Context;
    }

    public final URI getSchemaId() {
        return _SchemaId;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <V> Class<V> getFieldType(String fieldName) {

        final Prototype prototype = getContext().getPrototype(getSchemaId());

        final ObservableMap<String, Field> prototypeFields = prototype.getFields();
        final Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;

        if (prototypeField == null) {
            return (Class<V>) Object.class;
        }

        final Type type = prototypeField.getType();
        return TypeSystem.instance.getJavaType(type);
    }

    @Override
    protected boolean isReadOnly(String fieldName) {

        final Prototype prototype = getContext().getPrototype(getSchemaId());
        final ObservableMap<String, Field> prototypeFields = prototype.getFields();
        final Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;
        if (prototypeField != null) {
            return prototypeField.isReadOnly();
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    protected <V> V setRawFieldValue(String fieldName, V fieldValue) {

        final Prototype prototype = getContext().getPrototype(getSchemaId());
        final ObservableMap<String, Field> prototypeFields = prototype.getFields();

        final Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;
        if (prototypeField != null) {
            if (prototypeField.isReadOnly()) {
                throw new IllegalAccessError("Field \"" + fieldName + "\" is read only in \"" + prototype.getSchemaId()
                        + "\"");
            }

            if (prototypeField.isRequired() && (fieldValue == null)) {
                throw new NullPointerException("Field \"" + fieldName + "\" is requires a value in \""
                        + prototype.getSchemaId() + "\"");
            }

            final ObservableList<URI> constraintIds = prototypeField.getConstraintIds();
            if ((constraintIds != null) && (constraintIds.size() > 0)) {
                // MSMTODO: Validate constraints
            }

        }

        return (V) super.setRawFieldValue(fieldName, fieldValue);
    }

}
