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

package org.wrml.core.runtime;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;

import com.googlecode.gentyref.GenericTypeReflector;

import org.wrml.core.model.schema.Type;
import org.wrml.core.runtime.system.transformer.SystemTransformers;
import org.wrml.core.transformer.Transformers;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.www.MediaType;

public final class TypeSystem extends RuntimeObject {

    // This could grow large from Enums
    private final Map<java.lang.reflect.Type, Object> _DefaultValues;

    public TypeSystem(Context context) {
        super(context);
        _DefaultValues = new WeakHashMap<java.lang.reflect.Type, Object>();
    }

    public Object getDefaultValue(final java.lang.reflect.Type fieldNativeType) {

        Object defaultValue = null;

        if (_DefaultValues.containsKey(fieldNativeType)) {
            defaultValue = _DefaultValues.get(fieldNativeType);
            return defaultValue;
        }

        final SystemTransformers systemTransformers = getContext().getSystemTransformers();
        final Type type = systemTransformers.getNativeTypeToTypeTransformer().aToB(fieldNativeType);

        switch (type) {

        case Text:
        case Model:
        case Map:
        case List:
        case DateTime:
        case Native:

            defaultValue = null;
            break;

        case Boolean:
            defaultValue = Boolean.FALSE;
            break;

        case Integer:
            defaultValue = 0;
            break;

        case Double:
            defaultValue = 0.0;
            break;

        case Long:
            defaultValue = 0L;
            break;

        case Choice:
            if (fieldNativeType instanceof Class<?>) {
                final Object[] enumConstants = ((Class<?>) fieldNativeType).getEnumConstants();
                if ((enumConstants != null) && (enumConstants.length > 0)) {
                    defaultValue = enumConstants[0];
                }
            }

            break;

        default:
            defaultValue = null;
            break;
        }

        _DefaultValues.put(fieldNativeType, defaultValue);

        return defaultValue;
    }

    public <T extends Enum<T>> T getEnumFromString(Class<T> enumType, String name) {
        if ((enumType != null) && (name != null)) {
            try {
                return Enum.valueOf(enumType, name);
            }
            catch (final IllegalArgumentException ex) {
            }
        }
        return null;
    }

    public java.lang.reflect.Type getNativeReturnType(Method method, java.lang.reflect.Type type) {

        // http://code.google.com/p/gentyref/source/browse/src/main/java/com/googlecode/gentyref/GenericTypeReflector.java
        final java.lang.reflect.Type nativeType = GenericTypeReflector.getExactReturnType(method, type);

        return nativeType;
    }

    public java.lang.reflect.Type[] getNativeTypeParameters(java.lang.reflect.Type type) {

        java.lang.reflect.Type[] nativeTypeParameters = null;
        if (type instanceof ParameterizedType) {
            nativeTypeParameters = ((ParameterizedType) type).getActualTypeArguments();
        }

        return nativeTypeParameters;
    }

    public MediaType normalize(MediaType other) {
        if (!other.isWrml()) {
            return other;
        }

        final ObservableMap<String, String> parameters = other.getParameters();

        if ((parameters != null) && parameters.containsKey(MediaType.PARAMETER_NAME_FORMAT)) {

            final SortedMap<String, String> normalizedParameters = new TreeMap<String, String>(parameters);
            normalizedParameters.remove(MediaType.PARAMETER_NAME_FORMAT);

            final String normalizedMediaTypeString = MediaType.createWrmlMediaTypeString(
                    parameters.get(MediaType.PARAMETER_NAME_SCHEMA), normalizedParameters);

            final Transformers<String> stringTransformers = getContext().getStringTransformers();
            return stringTransformers.getTransformer(MediaType.class).bToA(normalizedMediaTypeString);
        }

        return other;

    }

    public boolean schematicallyEquals(MediaType a, MediaType b) {
        return normalize(a).equals(normalize(b));
    }

}
