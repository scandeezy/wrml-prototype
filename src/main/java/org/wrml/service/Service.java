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

import org.wrml.Model;
import org.wrml.runtime.Context;
import org.wrml.util.MediaType;
import org.wrml.util.transformer.Transformer;

/**
 * This is the (still-evolving) core "backend connection" CRUD interface that is
 * intended to "recursively" and "uniformly" satisfy the communication needs on
 * both the client and server-side of the WRML framework.
 * 
 * @param <T>
 *            the Model subtype the is serviced here.
 */
public interface Service extends Map<URI, Object> {

    public Object create(URI collectionId, Object requestEntity, MediaType responseType, Model referrer);

    public Object get(URI resourceId, Object cachedEntity, MediaType responseType, Model referrer);

    public Context getContext();

    public Transformer<URI, ?> getIdTransformer();

    public Object put(URI resourceId, Object requestEntity, MediaType responseType, Model referrer);

    public Object remove(URI resourceId, MediaType responseType, Model referrer);

    // TODO: Provide "first class" support for Container resources in the Service Java API
    // Need to figure out how to pass pagination and query by example params 
    //public Container<Document> getContainer(URI containerId, MediaType responseType, Model referrer, Options options);

    // TODO: Support non-model input/output with I/O Streams?

    // TODO: Support model search? Fuzzy matching? Query by example?

    // TODO: Support getting Sets of models from Sets of URIs?

    // TODO: Support getting Sets of models from Sets of URIs?
}
