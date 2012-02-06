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

package org.wrml.core.runtime.system;

import org.wrml.core.Model;
import org.wrml.core.model.schema.Type;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.FieldAccessType;
import org.wrml.core.runtime.RuntimeObject;
import org.wrml.core.runtime.TypeSystem;
import org.wrml.core.runtime.system.transformer.SystemTransformers;

public final class FieldPrototype extends RuntimeObject {

    private final String _FieldName;
    private final java.lang.reflect.Type _NativeType;
    private final Type _Type;

    public FieldPrototype(final Context context, final String fieldName, final Type type,
            java.lang.reflect.Type nativeType) {
        super(context);
        _FieldName = fieldName;
        _Type = type;
        _NativeType = nativeType;
    }

    public Object accessField(final Model model, final FieldAccessType accessType, final Object newValue) {

        Object oldFieldValue = null;

        switch (accessType) {
        case GET:
            oldFieldValue = model.getFieldValue(_FieldName);
            break;

        case SET:
            oldFieldValue = model.setFieldValue(_FieldName, newValue);
            break;
        }

        return oldFieldValue;
    }

    public String getFieldName() {
        return _FieldName;
    }

    public Class<?> getNativeClass() {
        final SystemTransformers systemTransformers = getContext().getSystemTransformers();
        return systemTransformers.getNativeTypeToClassTransformer().aToB(getNativeType());
    }

    public java.lang.reflect.Type getNativeType() {
        return _NativeType;
    }

    public java.lang.reflect.Type[] getNativeTypeParameters() {
        final Context context = getContext();
        final TypeSystem typeSystem = context.getTypeSystem();
        return typeSystem.getNativeTypeParameters(getNativeType());
    }

    public Type getType() {
        return _Type;
    }

}