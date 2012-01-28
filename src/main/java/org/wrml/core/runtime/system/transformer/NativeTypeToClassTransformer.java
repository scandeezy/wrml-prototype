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

package org.wrml.core.runtime.system.transformer;

import java.lang.reflect.ParameterizedType;

import org.wrml.core.runtime.Context;

public class NativeTypeToClassTransformer extends ConstantTransformer<java.lang.reflect.Type, Class<?>> {

    public NativeTypeToClassTransformer(Context context) {
        super(context);
    }

    public Class<?> aToB(java.lang.reflect.Type type) {

        if (type instanceof Class<?>) {

            if (Boolean.TYPE.equals(type)) {
                return Boolean.class;
            }

            if (Integer.TYPE.equals(type)) {
                return Integer.class;
            }

            if (Float.TYPE.equals(type)) {
                return Double.class;
            }

            if (Double.TYPE.equals(type)) {
                return Double.class;
            }

            if (Long.TYPE.equals(type)) {
                return Long.class;
            }

            return (Class<?>) type;
        }
        else if (type instanceof ParameterizedType) {

            final ParameterizedType parameterizedType = (ParameterizedType) type;

            final java.lang.reflect.Type rawType = parameterizedType.getRawType();

            // TODO: Do we need to loop here to find the class part of the ParameterizedType?

            if (rawType instanceof Class<?>) {
                return (Class<?>) rawType;
            }
        }

        return Object.class;
    }

    public java.lang.reflect.Type bToA(Class<?> clazz) {
        return clazz;
    }

}
