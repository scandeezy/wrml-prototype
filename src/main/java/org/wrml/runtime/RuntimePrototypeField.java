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

import org.wrml.Context;
import org.wrml.model.runtime.PrototypeField;

public class RuntimePrototypeField extends RuntimeModel implements PrototypeField {

    private static final long serialVersionUID = -5771927546979489208L;

    // TODO: Make this an enum?
    public static final String FIELD_NAME_FIELD_NAME = "fieldName";
    public static final String DEFAULT_VALUE_FIELD_NAME = "defaultValue";

    public RuntimePrototypeField(URI schemaId, Context context, String fieldName) {
        super(schemaId, context);
        setFieldValue(FIELD_NAME_FIELD_NAME, fieldName);
    }

    public Object getDefaultValue() {
        return getFieldValue(DEFAULT_VALUE_FIELD_NAME);
    }

    public String getFieldName() {
        return (String) getFieldValue(FIELD_NAME_FIELD_NAME);
    }

    public Object setDefaultValue(Object defaultValue) {
        return setFieldValue(DEFAULT_VALUE_FIELD_NAME, defaultValue);
    }

}
