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

package org.wrml.model.schema;

public enum Type {

    /**
     * In Java, maps to: boolean
     */
    Boolean("Boolean"),

    /**
     * In Java, maps to: Enum<T>, where T is determined by
     * ChoiceMenuConstraint's ChoiceMenu.
     */
    Choice("Choice"),

    /**
     * In Java, maps to: org.joda.time.DateTime
     */
    DateTime("DateTime"),

    /**
     * In Java, maps to: either double or java.lang.Double, dependent on Field's
     * isRequired flag's value.
     */
    Double("Double"),

    /**
     * In Java, maps to: either int or java.lang.Integer, dependent on Field's
     * isRequired flag's value.
     */
    Integer("Integer"),

    /**
     * In Java, maps to: org.wrml.util.ObservableList<T>, where "primitive" type
     * is determined by TypeConstraint's Type.
     * 
     * Note: If the TypeConstraint's Type is "Model", then T is either
     * org.wrml.Model or a subclass T determined by an added SchemaConstraint's
     * Schema class
     */
    List("List"),

    /**
     * In Java, maps to: either long or java.lang.Long, dependent on Field's
     * isRequired flag's value.
     */
    Long("Long"),

    /**
     * In Java, maps to: org.wrml.util.ObservableMap<K,V>, where "param" type
     * is determined by TypeConstraints.
     */
    Map("Map"),

    /**
     * In Java, maps to: org.wrml.Model or a subclass T determined by
     * SchemaConstraint's Schema class.
     */
    Model("Model"),

    /**
     * In Java, maps to: java.lang.Object
     */
    Native("Native"),

    /**
     * In Java, maps to: java.lang.String or T, where T is determined by a
     * configuration-based mapping of TextSyntaxConstraint to
     * StringTransformer<T>
     */
    Text("Text");

    private final String _Name;

    private Type(final String name) {
        _Name = name;
    }

    public String getName() {
        return _Name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
