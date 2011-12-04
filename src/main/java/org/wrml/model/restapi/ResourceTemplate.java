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
package org.wrml.model.restapi;

import java.net.URI;

import org.wrml.model.Collection;
import org.wrml.model.Descriptive;
import org.wrml.model.Document;
import org.wrml.model.ResourceArchetype;
import org.wrml.model.Versioned;
import org.wrml.model.schema.FieldDefault;
import org.wrml.util.ObservableList;
import org.wrml.util.ObservableMap;

/**
 * A single "node" in a REST API's path-based Web resource model's hierarchical
 * usage.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
// Generated from a Web Resource Schema
public interface ResourceTemplate extends Versioned, Descriptive, Document {

    // Generated from Field
    //     Name: restApiTemplateId 
    //     Value: Text[URI] 
    //     Flags: Required, ReadOnly
    public URI getRestApiTemplateId();

    public ResourceArchetype getResourceArchetype();

    public ResourceArchetype setResourceArchetype(ResourceArchetype resourceArchetype);

    public String getPathSegment();

    public String setPathSegment(String pathSegment);

    public URI getParentResourceTemplateId();

    public URI setParentResourceTemplateId(URI parentResourceTemplateId);

    public ResourceTemplate getParentResourceTemplate();

    // Generated from Field
    //     Name: childResourceTemplatesId
    //     Value: Text[URI]
    //     Constraints: TextSyntax - URI
    //     Flags: ReadOnly
    public URI getChildResourceTemplatesId();

    // Generated from Link
    //     Relation: childResourceTemplates
    //         Methods: GET 
    //         ResponseSchema: Collection[ResourceTemplate]
    //     EnabledFormula: childResourceTemplatesId != null
    //     DestinationUriTemplate: {childResourceTemplatesId} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["childResourceTemplatesId"]]
    //     Href: <childResourceTemplatesId>
    public Collection<ResourceTemplate> getChildResourceTemplates();

    public ObservableMap<URI, ObservableList<FieldDefault<?>>> getSchemaFieldDefaultsMap();

    public ObservableMap<URI, URI> getHereToThereLinkRelationIdToLinkTemplateIdMap();

    public ObservableList<URI> getThereToHereLinkTemplateIds();
}
