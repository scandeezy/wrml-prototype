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

/**
 * A Link template exists within a specific API's design metadata. It is part of
 * a Resource Template which is specific to a design-time linking of two REST
 * API resource templates.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public interface LinkTemplate {

    public ResourceTemplate getDestination();

    public Bag<String, Field<?>> getDestinationUriTemplateFields(Schema schema);

    public LinkRelation getRelation();

    public Bag<String, MediaType> getRequestMediaTypes();

    public Bag<String, MediaType> getResponseMediaTypes();

    public ResourceTemplate getSource();

}
