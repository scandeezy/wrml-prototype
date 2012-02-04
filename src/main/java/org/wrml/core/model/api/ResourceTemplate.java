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

package org.wrml.core.model.api;

import org.wrml.core.model.Descriptive;
import org.wrml.core.model.Document;
import org.wrml.core.model.ResourceArchetype;
import org.wrml.core.model.TreeNode;
import org.wrml.core.model.Versioned;

/**
 * A single "node" in a REST API's path-based Web resource model's hierarchical
 * usage.
 * 
 * The possibility of templated URI path segments (using the popular UriTemplate
 * '{' syntax) applies to this class's path segment, which is where the
 * "template" part of its name comes from.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
// Generated from a Web Resource Schema
public interface ResourceTemplate extends Versioned, Descriptive, TreeNode<Api, ResourceTemplate, ResourceTemplate>,
        Document {

    /**
     * Get the path segment {@link String} value, which can be either a simple,
     * static word like, "movies", or a URI template parameter
     * (fill-in-the-blank) like, "{movieId}".
     * 
     * @return the path segment String value.
     */
    public String getPathSegment();

    /**
     * Get the {@link ResourceArchetype}, which classifies this ResourceTemplate
     * by its characteristic HTTP-based interactions.
     * 
     * @return the ResourceArchetype that classifies this ResourceTemplate
     */
    public ResourceArchetype getResourceArchetype();

    /**
     * Set the path segment {@link String} value.
     * 
     * @param pathSegment
     *            the path segment String value.
     * @return the previously held value
     */
    public String setPathSegment(String pathSegment);

    /**
     * Set the {@link ResourceArchetype}.
     * 
     * @param resourceArchetype
     *            the ResourceArchetype that classifies this ResourceTemplate.
     * 
     * @return the ResourceArchetype that this ResourceTemplate previously
     *         resembled
     */
    public ResourceArchetype setResourceArchetype(ResourceArchetype resourceArchetype);

    // TODO: The (half-baked) idea here was to allow resource tree nodes to override model defaults...
    // public ObservableMap<URI, ObservableList<RuntimePrototypeField>> getSchemaFieldDefaultsMap();

}
