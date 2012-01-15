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

package org.wrml.util.transformer;

import java.util.Date;

import org.wrml.Model;
import org.wrml.model.schema.Type;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;

/**
 * Transforms WRML's type system to/from Java's.
 */

public final class TypeToClassTransformer implements Transformer<Type, Class<?>>,
        ConstantTransformation<Type, Class<?>> {

    public TypeToClassTransformer() {
    }

    public Type bToA(final Class<?> clazz) {

        if (String.class.equals(clazz)) {
            return Type.Text;
        }

        if (Model.class.isAssignableFrom(clazz)) {
            return Type.Model;
        }

        if (ObservableMap.class.isAssignableFrom(clazz)) {
            return Type.Map;
        }

        if (ObservableList.class.isAssignableFrom(clazz)) {
            return Type.List;
        }

        if (Boolean.TYPE.equals(clazz) || Boolean.class.equals(clazz)) {
            return Type.Boolean;
        }

        if (Enum.class.isAssignableFrom(clazz)) {
            return Type.Choice;
        }

        if (Integer.TYPE.equals(clazz) || Integer.class.equals(clazz)) {
            return Type.Integer;
        }

        if (Float.TYPE.equals(clazz) || Float.class.equals(clazz)) {
            return Type.Double;
        }

        if (Date.class.isAssignableFrom(clazz)) {
            // TODO: Change to Joda?
            return Type.DateTime;
        }

        if (Double.TYPE.equals(clazz) || Double.class.equals(clazz)) {
            return Type.Double;
        }

        if (Long.TYPE.equals(clazz) || Long.class.equals(clazz)) {
            return Type.Long;
        }

        return Type.Native;
    }

    public final Class<?> aToB(final Type type) {

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
