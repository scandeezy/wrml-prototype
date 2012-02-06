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

package org.wrml.core.event;

import java.util.EventObject;
import java.util.UUID;

import org.wrml.core.util.Unique;

public class Event<S extends EventSource<?>> extends EventObject implements Unique<UUID> {

    private static final long serialVersionUID = 1L;

    private final S _Source;

    private final UUID _Id;
    private int _HashCode;

    public Event(S source) {
        super(source);
        _Source = source;
        _Id = UUID.randomUUID();
        _HashCode = -1;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        final Event<S> other = (Event<S>) obj;
        if (_Id == null) {
            if (other._Id != null) {
                return false;
            }
        }
        else if (!_Id.equals(other._Id)) {
            return false;
        }

        return true;
    }

    public UUID getId() {
        return _Id;
    }

    @Override
    public S getSource() {
        return _Source;
    }

    @Override
    public int hashCode() {

        if (_HashCode < 0) {
            _HashCode = _Id.hashCode();
        }
        return _HashCode;
    }
}