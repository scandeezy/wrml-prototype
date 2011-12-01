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

import org.wrml.util.CancelableEvent;
import org.wrml.util.MapEvent;

/**
 * An event that communicates some activity associated with a specific
 * WRMLObject instance's specific field.
 * 
 * @param <T>
 *            The field type
 */
public final class FieldEvent<T> extends CancelableEvent {

    private static final long serialVersionUID = -1277427529297982437L;

    private final String _FieldName;
    private final MapEvent<String, Object> _SourceEvent;
    private ConstraintEvent<T> _ConstraintEvent;

    public FieldEvent(WrmlObject source, boolean cancelable, String fieldName, MapEvent<String, Object> sourceEvent) {
        super(source, cancelable);
        _FieldName = fieldName;
        _SourceEvent = sourceEvent;
    }

    public ConstraintEvent<T> getConstraintEvent() {
        return _ConstraintEvent;
    }

    public String getFieldName() {
        return _FieldName;
    }

    public MapEvent<String, Object> getSourceEvent() {
        return _SourceEvent;
    }

    public void setConstraintEvent(ConstraintEvent<T> constraintEvent) {
        _ConstraintEvent = constraintEvent;
    }

}
