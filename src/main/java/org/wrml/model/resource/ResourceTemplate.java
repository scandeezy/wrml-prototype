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

package org.wrml.model.resource;

import java.net.URI;

import org.wrml.model.Descriptive;
import org.wrml.model.Versioned;
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

    // Generated from Link
    //     Relation: childResourceTemplates
    //         Methods: GET 
    //         ResponseSchema: Collection[ResourceTemplate]
    //     EnabledFormula: childResourceTemplatesId != null
    //     DestinationUriTemplate: {childResourceTemplatesId} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["childResourceTemplatesId"]]
    //     Href: <childResourceTemplatesId>
    public Collection<ResourceTemplate> getChildResourceTemplates();

    // Generated from Field
    //     Name: childResourceTemplatesId
    //     Value: Text[URI]
    //     Constraints: TextSyntax - URI
    //     Flags: ReadOnly
    public URI getChildResourceTemplatesId();

    public ObservableMap<URI, URI> getEndPointLinkRelationIdToLinkTemplateIdMap();

    public ResourceTemplate getParentResourceTemplate();

    public URI getParentResourceTemplateId();

    public String getPathSegment();

    public ResourceArchetype getResourceArchetype();

    // Generated from Field
    //     Name: restApiTemplateId 
    //     Value: Text[URI] 
    //     Flags: Required, ReadOnly
    public URI getRestApiTemplateId();

    // TODO: The (half-baked) idea here was to allow resource tree nodes to override model defaults...
    // public ObservableMap<URI, ObservableList<PrototypeField>> getSchemaFieldDefaultsMap();

    public ObservableList<URI> getStartingPointLinkTemplateIds();

    public URI setParentResourceTemplateId(URI parentResourceTemplateId);

    public String setPathSegment(String pathSegment);

    public ResourceArchetype setResourceArchetype(ResourceArchetype resourceArchetype);
}
