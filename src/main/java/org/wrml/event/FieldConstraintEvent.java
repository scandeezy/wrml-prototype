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

package org.wrml.event;

import java.util.EventObject;

import org.wrml.Model;
import org.wrml.util.enforcer.EnforcementResult;
import org.wrml.util.enforcer.FieldConstraintEnforcer;

public class FieldConstraintEvent extends EventObject {

    private static final long serialVersionUID = 8426522526071747315L;

    private final FieldConstraintEnforcer _FieldConstraintEnforcer;
    private final EnforcementResult _EnforcementResult;

    public FieldConstraintEvent(Model source, FieldConstraintEnforcer fieldConstraintEnforcer, EnforcementResult enforcementResult) {
        super(source);
        _FieldConstraintEnforcer = fieldConstraintEnforcer;
        _EnforcementResult = enforcementResult;
    }

    public EnforcementResult getValidationResult() {
        return _EnforcementResult;
    }

    public FieldConstraintEnforcer getValidator() {
        return _FieldConstraintEnforcer;
    }

}
