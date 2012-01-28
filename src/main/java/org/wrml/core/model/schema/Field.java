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

import org.wrml.core.model.Descriptive;
import org.wrml.core.model.Named;
import org.wrml.core.model.Titled;

/**
 * A web resource schema's field. Conceptually a field is a typed data slot,
 * like a field in a Java object or a field on a web form. Instances of this
 * class group the metadata associated with a specific schema's field.
 * 
 */
// Generated from a Web Resource Schema
public interface Field extends Named, Titled, Descriptive, Typed, SchemaMember<Field> {

    public Object getDefaultValue();

    public boolean isHidden();

    public boolean isLocal();

    public boolean isReadOnly();

    public boolean isRequired();

    public Object setDefaultValue(Object defaultValue);

    public boolean setHidden(boolean hidden);

    public boolean setLocal(boolean local);

    public boolean setReadOnly(boolean readOnly);

    public boolean setRequired(boolean required);
}
