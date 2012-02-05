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

package org.wrml.core.runtime.event;

import java.util.EventObject;

import org.wrml.core.Model;
import org.wrml.core.util.observable.MapEvent;

/**
 * An event that communicates some activity associated with a specific
 * Model instance's specific field.
 */
public class FieldEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    private final String _FieldName;
    private final MapEvent _SourceEvent;
    private FieldConstraintEvent _FieldConstraintEvent;

    public FieldEvent(Model source, String fieldName, MapEvent sourceEvent) {
        super(source);
        _FieldName = fieldName;
        _SourceEvent = sourceEvent;
    }

    public FieldConstraintEvent getConstraintEvent() {
        return _FieldConstraintEvent;
    }

    public String getFieldName() {
        return _FieldName;
    }

    public MapEvent getSourceEvent() {
        return _SourceEvent;
    }

    public Model getSourceModel() {
        return (Model) getSource();
    }

    public void setConstraintEvent(FieldConstraintEvent fieldConstraintEvent) {
        _FieldConstraintEvent = fieldConstraintEvent;
    }

}
