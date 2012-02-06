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
import org.wrml.core.event.Event;
import org.wrml.core.runtime.ModelHeapShard;

public class ModelHeapShardEvent extends Event<ModelHeapShard> {

    private static final long serialVersionUID = 1L;

    private Model _Model;

    public ModelHeapShardEvent(ModelHeapShard source) {
        super(source);
    }

    public Model getModel() {
        return _Model;
    }

    public void setModel(Model model) {
        _Model = model;
    }

}
