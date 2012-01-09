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

import org.wrml.model.schema.Constraint;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Type;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;

public class ModelFieldMap extends DynamicFieldMap {

    private final transient Context _Context;
    private final URI _SchemaId;

    public ModelFieldMap(final Context context, final URI schemaId, final Map<String, Object> delegate) {
        super(delegate);

        _Context = context;
        _SchemaId = schemaId;
    }

    public Context getContext() {
        return _Context;
    }

    public Prototype getPrototype() {
        return getContext().getPrototype(getSchemaId());
    }

    public URI getSchemaId() {
        return _SchemaId;
    }

    @Override
    protected Class<?> getFieldType(String fieldName) {

        Prototype prototype = getPrototype();

        ObservableMap<String, Field> prototypeFields = prototype.getFields();
        Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;

        if (prototypeField == null) {
            return Object.class;
        }

        Type type = prototypeField.getType();
        return TypeSystem.instance.getJavaType(type);
    }

    @Override
    protected boolean isReadOnly(String fieldName) {

        Prototype prototype = getPrototype();
        ObservableMap<String, Field> prototypeFields = prototype.getFields();
        Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;
        if (prototypeField != null) {
            return prototypeField.isReadOnly();
        }

        return true;
    }

    @Override
    protected Object setRawFieldValue(String fieldName, Object fieldValue) {

        Prototype prototype = getPrototype();
        ObservableMap<String, Field> prototypeFields = prototype.getFields();

        Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;
        if (prototypeField != null) {
            if (prototypeField.isReadOnly()) {
                throw new IllegalAccessError("Field \"" + fieldName + "\" is read only in \"" + prototype.getSchemaId()
                        + "\"");
            }

            if (prototypeField.isRequired() && fieldValue == null) {
                throw new NullPointerException("Field \"" + fieldName + "\" is requires a value in \""
                        + prototype.getSchemaId() + "\"");
            }

            ObservableList<Constraint> constraints = prototypeField.getConstraints();
            if (constraints != null && constraints.size() > 0) {
                // MSMTODO: Validate constraints
            }

        }

        return super.setRawFieldValue(fieldName, fieldValue);
    }

}
