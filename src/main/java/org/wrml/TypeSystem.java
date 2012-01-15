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

package org.wrml;

import java.util.Map;
import java.util.WeakHashMap;

import org.wrml.model.schema.Type;
import org.wrml.util.transformer.CachingTransformer;
import org.wrml.util.transformer.ConstantTransformation;
import org.wrml.util.transformer.Transformer;
import org.wrml.util.transformer.TypeToClassTransformer;

public final class TypeSystem {

    public final static TypeSystem instance = new TypeSystem();

    // This could grow large from Enums
    private final Map<Class<?>, Object> _DefaultValues;
    private final Transformer<Type, Class<?>> _TypeToClassTransformer;

    private TypeSystem() {

        _DefaultValues = new WeakHashMap<Class<?>, Object>();

        _TypeToClassTransformer = new CachingTransformer<Type, Class<?>, ConstantTransformation<Type, Class<?>>>(
                new TypeToClassTransformer(), new WeakHashMap<Type, Class<?>>(), new WeakHashMap<Class<?>, Type>());
    }

    @SuppressWarnings("unchecked")
    public final <V> V getDefaultValue(final Class<?> clazz) {

        Object defaultValue = null;

        if (_DefaultValues.containsKey(clazz)) {
            defaultValue = _DefaultValues.get(clazz);
            return (V) defaultValue;
        }

        Type type = getTypeToClassTransformer().bToA(clazz);

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
            final Object[] enumConstants = clazz.getEnumConstants();
            if (enumConstants != null && enumConstants.length > 0) {
                defaultValue = enumConstants[0];
            }
            break;

        default:
            defaultValue = null;
            break;
        }

        _DefaultValues.put(clazz, defaultValue);

        return (V) defaultValue;
    }

    public Transformer<Type, Class<?>> getTypeToClassTransformer() {
        return _TypeToClassTransformer;
    }
}
