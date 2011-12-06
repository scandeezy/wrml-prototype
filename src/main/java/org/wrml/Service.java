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

import java.net.URI;

/**
 * This is still-evolving core "backend connection" CRUD interface that is
 * intended to "recursively" and "uniformly" satisfy the communication needs on
 * both the client and server-side of the WRML framework.
 * 
 * @param <T>
 */
public interface Service<T extends Model> {

    // For models clients

    /**
     * Gets a Model of type T based on the specified model ID. The optional
     * requestor may be used to influence the outcome.
     * 
     * @param id
     *            The ID of the model to request.
     * 
     * @return The identified model or null if the request could not be
     *         resolved.
     */
    T get(URI id);

    /**
     * Gets a Model of type T based on the specified model ID. The optional
     * requestor may be used to influence the outcome.
     * 
     * @param id
     *            The ID of the model to request.
     * @param requestor
     *            The (optional) model requesting this service to get the other
     *            model.
     * 
     * @return The identified model or null if the request could not be
     *         resolved.
     */
    T get(URI id, Model requestor);

    /**
     * Saves a Model of type T based on the specified model ID. The optional
     * requestor may be used to influence the outcome.
     * 
     * @param id
     *            The ID of the model to save.
     * 
     * @return The identified model or null if the request could not be
     *         resolved.
     */
    T save(URI id, T modelToSave);

    /**
     * Saves a Model of type T based on the specified model ID. The optional
     * requestor may be used to influence the outcome.
     * 
     * @param id
     *            The ID of the model to save.
     * @param requestor
     *            The (optional) model requesting this service to save the other
     *            model.
     * 
     * @return The identified model or null if the request could not be
     *         resolved.
     */
    T save(URI id, T modelToSave, Model requestor);

    // Collection getMany(List<URI> ids, URI responseModelSchemaId);

}
