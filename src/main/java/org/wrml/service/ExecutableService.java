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

package org.wrml.service;

import java.net.URI;

import org.wrml.Model;
import org.wrml.model.resource.Action;

public interface ExecutableService<K, M extends Model> extends Service<K, M> {

    // EXECUTE

    // TODO: Need to package up an Action as an input 
    // that references a model and some other things?

    public Model execute(URI id);

    public Model execute(URI id, Action action);

    public Model execute(URI id, Action action, Model requestor);

    public Model execute(URI id, Model requestor);
}
