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

import org.wrml.model.Descriptive;
import org.wrml.model.Named;
import org.wrml.model.Titled;
import org.wrml.util.observable.ObservableList;

/**
 * A web resource schema's field. Conceptually a field is a typed data slot,
 * like a field in a Java object or a field on a web form. Instances of this
 * class group the metadata associated with a specific schema's field.
 * 
 */
// Generated from a Web Resource Schema
public interface Field extends Typed, Named, Titled, Descriptive, Member {

    // Added to Field
    //     Name: name 
    //     Constraints: TextSyntax - Mixed-Lower Case

    public ObservableList<Constraint> getConstraints();

    // TODO: We need to figure out how to represent Object in WRML.
    // Should it be a Type or should we use a "marker" Schema (Native)?
    public Object getDefaultValue();

    public boolean isHidden();

    public boolean isReadOnly();

    public boolean isRequired();

    public boolean isTransient();

    public Object setDefaultValue(Object defaultValue);

    public boolean setHidden(boolean hidden);

    public boolean setReadOnly(boolean readOnly);

    public boolean setRequired(boolean required);

    // Note: Will need to be careful when generating names based on WRML data. 
    // There may be keyword collisions (e.g. param named "transient" changed to "transientFlag") 
    public boolean setTransient(boolean transientFlag);
}
