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

package org.wrml.api;

import java.net.URI;
import java.util.List;

import org.wrml.util.Identifiable;
import org.wrml.util.ObservableList;
import org.wrml.util.ObservableMap;

/**
 * A single "node" in a REST API's path-based Web resource model's hierarchical
 * usage.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public final class ResourceTemplate extends Identifiable<URI> {

    private static final long serialVersionUID = -5567896015312023454L;

    private final URI _ApiTemplateId;

    private ResourceArchetype _ResourceArchetype;

    private String _PathSegment;

    private URI _ParentResourceTemplateId;

    private ObservableList<URI> _ChildResourceTemplateIds;

    private ObservableMap<URI /* schemaId */, ObservableMap<String, FieldDefault<?>> /* fieldNameToDefaultsMap */> _SchemaFieldDefaultsMap;

    private ObservableMap<URI /* hereToThereLinkRelationId */, URI /* linkTemplateId */> _HereToThereLinkRelationIdToLinkTemplateIdMap;

    private ObservableList<URI /* linkTemplateIds */> _ThereToHereLinkTemplateIds;

    public ResourceTemplate(URI apiTemplateId) {
        _ApiTemplateId = apiTemplateId;
    }

    public URI getApiTemplateId() {
        return _ApiTemplateId;
    }

    public ObservableList<URI> getChildResourceTemplateIds() {
        return _ChildResourceTemplateIds;
    }

    public ObservableMap<String, FieldDefault<?>> getFieldDefaults(URI schemaId) {
        return _SchemaFieldDefaultsMap.get(schemaId);
    }

    public ObservableMap<URI, URI> getHereToThereLinkRelationIdToLinkTemplateIdMap() {
        return _HereToThereLinkRelationIdToLinkTemplateIdMap;
    }

    public URI getHereToThereLinkTemplateId(URI hereToThereLinkRelationId) {
        return _HereToThereLinkRelationIdToLinkTemplateIdMap.get(hereToThereLinkRelationId);
    }

    public URI getParentResourceTemplateId() {
        return _ParentResourceTemplateId;
    }

    public String getPathSegment() {
        return _PathSegment;
    }

    public ResourceArchetype getResourceArchetype() {
        return _ResourceArchetype;
    }

    public ObservableMap<URI, ObservableMap<String, FieldDefault<?>>> getSchemaFieldDefaultsMap() {
        return _SchemaFieldDefaultsMap;
    }

    public List<URI> getThereToHereLinkTemplateIds() {
        return _ThereToHereLinkTemplateIds;
    }

    public void setChildResourceTemplateIds(ObservableList<URI> childResourceTemplateIds) {
        _ChildResourceTemplateIds = childResourceTemplateIds;
    }

    public void setFieldDefaults(URI schemaId, ObservableMap<String, FieldDefault<?>> fieldDefaults) {
        _SchemaFieldDefaultsMap.put(schemaId, fieldDefaults);
    }

    public void setHereToThereLinkRelationIdToLinkTemplateIdMap(
            ObservableMap<URI, URI> hereToThereLinkRelationIdToLinkTemplateIdMap) {
        _HereToThereLinkRelationIdToLinkTemplateIdMap = hereToThereLinkRelationIdToLinkTemplateIdMap;
    }

    public void setParentResourceTemplateId(URI parentResourceTemplateId) {
        _ParentResourceTemplateId = parentResourceTemplateId;
    }

    public void setPathSegment(String pathSegment) {
        _PathSegment = pathSegment;
    }

    public void setResourceArchetype(ResourceArchetype resourceArchetype) {
        _ResourceArchetype = resourceArchetype;
    }

    public void setSchemaFieldDefaultsMap(
            ObservableMap<URI, ObservableMap<String, FieldDefault<?>>> schemaFieldDefaultsMap) {
        _SchemaFieldDefaultsMap = schemaFieldDefaultsMap;
    }

    public void setThereToHereLinkTemplateIds(ObservableList<URI> thereToHereLinkTemplateIds) {
        _ThereToHereLinkTemplateIds = thereToHereLinkTemplateIds;
    }

}
