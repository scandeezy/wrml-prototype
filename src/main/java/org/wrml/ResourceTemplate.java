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
 * A single "node" in a REST API's path-based Web resource model's hierarchical usage. 
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public interface ResourceTemplate {

    public Bag<String, LinkTemplate> getDestinationLinkTemplates();

    public Bag<Schema, Bag<String, FieldDefault<?>>> getFieldDefaults();

    public String getName();

    public ResourceTemplate getParent();

    public String getPath();

    public ResourceArchetype getResourceArchetype();

    public Bag<String, LinkTemplate> getSourceLinkTemplates();

    public UriTemplate getUriTemplate();

    /*
     * public Bag<String, MediaType> getRequestMediaTypes();
     * 
     * public Bag<String, MediaType> getResponseMediaTypes();
     * 
     * public Bag<String, Schema> getRequestSchemas();
     * 
     * public Bag<String, Schema> getResponseSchemas();
     * 
     * public Bag<String, Format> getRequestFormats();
     * 
     * public Bag<String, Format> getResponseFormats();
     * 
     * public Bag<String, FieldTemplate<?>> getFieldTemplates();
     * 
     * public Bag<String, LinkTemplate> getLinkTemplates();
     */
}
