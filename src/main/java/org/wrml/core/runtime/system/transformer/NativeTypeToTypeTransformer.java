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

import java.util.Date;

import org.wrml.core.Model;
import org.wrml.core.model.schema.Type;
import org.wrml.core.runtime.Context;
import org.wrml.core.transformer.Transformer;
import org.wrml.core.transformer.Transformers;
import org.wrml.core.util.observable.ObservableList;
import org.wrml.core.util.observable.ObservableMap;

/**
 * Transforms WRML's type system to/from Java's.
 */

public final class NativeTypeToTypeTransformer extends ConstantTransformer<java.lang.reflect.Type, Type> {

    public NativeTypeToTypeTransformer(Context context) {
        super(context);
    }

    public Type aToB(final java.lang.reflect.Type nativeType) {

        final Context context = getContext();
        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final Class<?> nativeClass = systemTransformers.getNativeTypeToClassTransformer().aToB(nativeType);

        if (String.class.equals(nativeClass)) {
            return Type.Text;
        }

        if (Model.class.isAssignableFrom(nativeClass)) {
            return Type.Model;
        }

        if (ObservableMap.class.isAssignableFrom(nativeClass)) {
            return Type.Map;
        }

        if (ObservableList.class.isAssignableFrom(nativeClass)) {
            return Type.List;
        }

        if (Boolean.class.equals(nativeClass)) {
            return Type.Boolean;
        }

        if (Enum.class.isAssignableFrom(nativeClass)) {
            return Type.Choice;
        }

        if (Integer.class.equals(nativeClass)) {
            return Type.Integer;
        }

        if (Float.class.equals(nativeClass)) {
            return Type.Double;
        }

        if (Date.class.isAssignableFrom(nativeClass)) {
            // TODO: Change to Joda?
            return Type.DateTime;
        }

        if (Double.class.equals(nativeClass)) {
            return Type.Double;
        }

        if (Long.class.equals(nativeClass)) {
            return Type.Long;
        }

        final Transformers<String> stringTransformers = context.getStringTransformers();
        final Transformer<?, String> stringTransformer = stringTransformers.getTransformer(nativeClass);
        if (stringTransformer != null) {
            return Type.Text;
        }

        return Type.Native;
    }

    public final java.lang.reflect.Type bToA(final Type type) {

        Class<?> javaType = Object.class;
        switch (type) {

        case Boolean:
            javaType = Boolean.class;
            break;

        case Choice:
            // TODO: Handle choice type
            javaType = Enum.class;
            break;

        case DateTime:
            // TODO: Change to Joda DateTime?
            javaType = Date.class;
            break;

        case Double:
            javaType = Double.class;
            break;

        case Integer:
            javaType = Integer.class;
            break;

        case List:
            // TODO: Is this right or does it need to be a parameterized type?
            javaType = ObservableList.class;
            break;

        case Long:

            javaType = Long.class;
            break;

        case Map:
            // TODO: Is this right or does it need to be a parameterized type?
            javaType = ObservableMap.class;
            break;

        case Model:
            // TODO: Is this right or does it need to be a parameterized type or autogen schema subclass? 
            javaType = Model.class;
            break;

        case Native:
            // TODO: Handle other hints?

            javaType = Object.class;
            break;

        case Text:
            // TODO: Handle other syntax

            javaType = String.class;
            break;
        }

        return javaType;
    }

}
