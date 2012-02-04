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

package org.wrml.core.model.schema;

import org.wrml.core.Model;
import org.wrml.core.model.Descriptive;
import org.wrml.core.model.Named;
import org.wrml.core.model.Titled;

/**
 * A web resource schema's field. Conceptually a field is a typed data slot,
 * like a field in a Java object or a field on a web form. Instances of this
 * class hold the metadata associated with a specific schema's field.
 * 
 */
// Generated from a Web Resource Schema
public interface Field extends Named, Titled, Descriptive, Typed, SchemaMember<Field> {

    /**
     * Get the default value for this Field.
     * 
     * @return the Field's default value
     * 
     * @see Model#getFieldValue(String)
     * @see Model#setFieldToDefaultValue(String)
     * @see Model#setFieldValue(String, Object)
     */
    public Object getDefaultValue();

    /**
     * Get a hint regarding the visible nature of this Field in the context of a
     * Model viewer or editor.
     * 
     * @return <code>true</code> if this Field should be generally hidden out of
     *         site from people.
     */
    public boolean isHidden();

    /**
     * Get the "local" flag's value in order to determine if this Field's value
     * should remain in the local runtime and not be passed over the wire. This
     * is flag can be thought of as the inverse of a Java field's "isTransient"
     * flag.
     * 
     * @return <code>true</code> if this Field's value should not leave its
     *         local runtime environment
     */
    public boolean isLocal();

    /**
     * Get the flag value that determines if the Field's value may be
     * set (or not).
     * 
     * @return true if the Field is read-only and may *not* have its value
     *         changed.
     * 
     * @see Model#setFieldValue(String, Object)
     */
    public boolean isReadOnly();

    /**
     * Get the flag value that determines if the Field's value may be
     * <code>null</code> (or not). This flag's semantics are enforced during
     * attempts to update a {@link Model} instance or just set one of its
     * individual field values to <code>null</code>.
     * 
     * @return true if the Field value may *not* be <code>null</code>.
     * 
     * @see Model#setFieldValue(String, Object)
     * 
     */
    public boolean isRequired();

    /**
     * Set the default value for this Field.
     * 
     * @param defaultValue
     *            the default value
     * 
     * @return the previously held value
     * 
     * @see Field#getDefaultValue()
     * @see Model#setFieldToDefaultValue(String)
     * @see Model#setFieldValue(String, Object)
     */
    public Object setDefaultValue(Object defaultValue);

    /**
     * Set the "hidden" flag value of this Field.
     * 
     * @param hidden
     *            the flag value
     * 
     * @return the previously held value
     * 
     * @see Field#isHidden()
     */
    public boolean setHidden(boolean hidden);

    /**
     * Set the "local" flag value of this Field.
     * 
     * @param local
     *            the flag value
     * 
     * @return the previously held value
     * 
     * @see Field#isLocal()
     */
    public boolean setLocal(boolean local);

    /**
     * Set the "readOnly" flag value of this Field.
     * 
     * @param readOnly
     *            the flag value
     * 
     * @return the previously held value
     * 
     * @see Field#isReadOnly()
     */
    public boolean setReadOnly(boolean readOnly);

    /**
     * Set the "required" flag value of this Field.
     * 
     * @param required
     *            the flag value
     * 
     * @return the previously held value
     * 
     * @see Field#isRequired()
     */
    public boolean setRequired(boolean required);
}
