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

import org.wrml.core.Model;
import org.wrml.core.util.Cancelable;
import org.wrml.core.util.observable.MapEvent;

public class CancelableFieldEvent extends FieldEvent implements Cancelable {

    private static final long serialVersionUID = 1L;

    private boolean _Cancelled;

    public CancelableFieldEvent(Model source, String fieldName, MapEvent sourceEvent) {
        super(source, fieldName, sourceEvent);
    }

    public boolean isCancelled() {
        return _Cancelled;
    }

    public void setCancelled(boolean cancelled) {
        _Cancelled = cancelled;
    }

}
