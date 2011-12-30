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

import org.wrml.Model;

final class FieldAccessor {

    public enum AccessType {
        GET,
        SET,
    }

    private final String _FieldName;
    private final AccessType _AccessType;

    public FieldAccessor(final String fieldName, final AccessType accessType) {
        _FieldName = fieldName;
        _AccessType = accessType;
    }

    public Object accessField(final Model model, final Object newFieldValue) {

        Object oldFieldValue = null;

        switch (_AccessType) {
        case GET:
            oldFieldValue = model.getFieldValue(_FieldName);
            break;
        case SET:
            oldFieldValue = model.setFieldValue(_FieldName, newFieldValue);
            break;
        }

        return oldFieldValue;
    }

    public String getFieldName() {
        return _FieldName;
    }

    public AccessType getAccessType() {
        return _AccessType;
    }
}