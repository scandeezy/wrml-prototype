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
import java.util.Map;

import org.wrml.Context;
import org.wrml.Model;
import org.wrml.util.Factory;
import org.wrml.util.UriTransformer;

/**
 * This is the (still-evolving) core "backend connection" CRUD interface that is
 * intended to "recursively" and "uniformly" satisfy the communication needs on
 * both the client and server-side of the WRML framework.
 * 
 * @param <T>
 *            the Model subtype the is serviced here.
 */
public interface Service extends Factory<URI, Model>, Map<URI, Model> {

    public Context getContext();

    public Model create(Model requestor);

    public Model create(URI documentId, Model requestor);

    public Model get(URI documentId, Model requestor);

    public UriTransformer<?> getIdTransformer();

    public Model put(URI documentId, Model document, Model requestor);

    public Model remove(URI documentId, Model requestor);

    // TODO: Support non-model input/output with I/O Streams?

    // TODO: Support model search? Fuzzy matching? Query by example?

    // TODO: Support getting Sets of models from Sets of URIs?

    // TODO: Support getting Sets of models from Sets of URIs?
}
