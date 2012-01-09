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

import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;

import org.wrml.Model;
import org.wrml.model.schema.Type;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;

public final class TypeSystem {

    public static final TypeSystem instance = new TypeSystem();

    // TODO: Cache based on class name (string) not class (don't hold classes)
    private final Map<Class<?>, Object> _DefaultValues;

    private TypeSystem() {
        _DefaultValues = new WeakHashMap<Class<?>, Object>();
    }

    public final Object getDefaultValue(final Class<?> type) {

        Object defaultValue = null;

        if (_DefaultValues.containsKey(type)) {
            defaultValue = _DefaultValues.get(type);
            System.out.println("The default value for type \"" + type.getCanonicalName() + "\" is already mapped as \""
                    + defaultValue + "\"");
            return defaultValue;
        }

        Class<?> keyType = type;

        if (String.class.equals(type)) {
            defaultValue = null;
        }
        else if (Boolean.TYPE.equals(type) || Boolean.class.equals(type)) {
            defaultValue = Boolean.FALSE;
        }
        else if (Integer.TYPE.equals(type) || Integer.class.equals(type)) {
            defaultValue = 0;
        }
        else if (Float.TYPE.equals(type) || Float.class.equals(type)) {
            defaultValue = (Double) 0.0;
        }
        else if (Double.TYPE.equals(type) || Double.class.equals(type)) {
            defaultValue = 0.0;
        }
        else if (Long.TYPE.equals(type) || Long.class.equals(type)) {
            defaultValue = 0L;
        }
        else if (Void.TYPE.equals(type) || Void.class.equals(type)) {
            defaultValue = type;
        }
        else if (Enum.class.isAssignableFrom(type)) {
            Object[] enumConstants = type.getEnumConstants();
            if (enumConstants.length > 0) {
                defaultValue = enumConstants[0];
            }
        }
        else {
            keyType = Object.class;
        }

        _DefaultValues.put(keyType, defaultValue);

        System.out.println("The default value for type \"" + type.getCanonicalName() + "\" is \"" + defaultValue
                + "\" it is now mapped with key type \"" + keyType.getCanonicalName() + "\"");

        return defaultValue;
    }

    public final Class<?> getJavaType(final Type type) {

        Class<?> javaType = Object.class;
        switch (type) {

        case Boolean:
            javaType = Boolean.class;
            break;

        case Choice:
            // TODO: Handle choice type
            break;

        case DateTime:
            // TODO: Change to Joda DateTime
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
