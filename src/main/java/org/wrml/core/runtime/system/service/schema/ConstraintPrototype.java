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

package org.wrml.core.runtime.system.service.schema;

import org.wrml.core.model.schema.Constrainable;
import org.wrml.core.model.schema.Constraint;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.RuntimeObject;

/**
 * The ability to add Constraints to Schemas can have serve a few different
 * functions.
 * 
 * 1. SchemaConstraints can be added to generate parameterized types. For
 * example a SchemaConstraint named "T" could be used in a WRML schema like
 * "Container" to generate "Container<T extends Model>" in Java. The use of
 * one or more named SchemaConstraints at the Schema level has the effect of
 * creating named schema "slots" whenever these types are referenced in WRML
 * "builder" GUI tools.
 * 
 * @return
 */

public final class ConstraintPrototype<T extends Constrainable<T>> extends RuntimeObject {

    private final Constraint<T> _Constraint;

    public ConstraintPrototype(Context context, Constraint<T> constraint) {
        super(context);
        _Constraint = constraint;
    }

    public Constraint<T> getConstraint() {
        return _Constraint;
    }

}
