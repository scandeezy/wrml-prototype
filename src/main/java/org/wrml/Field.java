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

import java.util.List;

/**
 * A web resource schema's field. Conceptually a field is a typed data slot,
 * like a field in a Java object or a field on a web form. Instances of this
 * class group the metadata associated with a specific schema's field.
 *  
 */
public interface Field<T> extends Member, Comparable<Field<?>> {

    /**
     * Returns the value of the field represented by this Field, on the
     * specified WRML object.
     * 
     * @param wrmlObject
     *            the instance
     * @return the field value on the instance
     */
    public T get(WrmlObject wrmlObject);

    public List<Constraint> getConstraints();

    public T getDefaultValue();

    public String getName();

    public boolean isHidden();

    public boolean isReadOnly();

    public boolean isRequired();

    public boolean isTransient();

    /**
     * Sets the field represented by this Field object on the specified WRML
     * object argument to the specified new value.
     * 
     * @param wrmlObject
     *            the instance whose field should be modified
     * @param value
     *            the new value for the field of instance being modified
     * @return the previous value of the field of the instance being modified
     */
    public T set(WrmlObject wrmlObject, T value);

}
