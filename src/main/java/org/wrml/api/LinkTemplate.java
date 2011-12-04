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

import org.wrml.MediaType;
import org.wrml.util.Identifiable;
import org.wrml.util.ObservableList;
import org.wrml.util.ObservableMap;

/**
 * A Link template exists within a specific API's design metadata. It is part of
 * a Resource Template which is specific to a design-time linking of two REST
 * API resource templates.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public final class LinkTemplate extends Identifiable<URI> {

    private static final long serialVersionUID = -8450128794176748903L;

    private final URI _ApiTemplateId;
    private URI _SourceResourceTemplateId;
    private URI _DestinationResourceTemplateId;
    private URI _LinkRelationId;
    private ObservableList<MediaType> _RequestMediaTypes;
    private ObservableList<MediaType> _ResponseMediaTypes;

    private ObservableMap<URI /* schemaId */, ObservableList<String> /* fieldNames */> _DestinationUriTemplateFields;

    public LinkTemplate(URI apiTemplateId) {
        _ApiTemplateId = apiTemplateId;
    }

    public URI getApiTemplateId() {
        return _ApiTemplateId;
    }

    public URI getDestinationResourceTemplateId() {
        return _DestinationResourceTemplateId;
    }

    public ObservableMap<URI, ObservableList<String>> getDestinationUriTemplateFields() {
        return _DestinationUriTemplateFields;
    }

    public URI getLinkRelationId() {
        return _LinkRelationId;
    }

    public ObservableList<MediaType> getRequestMediaTypes() {
        return _RequestMediaTypes;
    }

    public ObservableList<MediaType> getResponseMediaTypes() {
        return _ResponseMediaTypes;
    }

    public URI getSourceResourceTemplateId() {
        return _SourceResourceTemplateId;
    }

    public void setDestinationResourceTemplateId(URI destinationResourceTemplateId) {
        _DestinationResourceTemplateId = destinationResourceTemplateId;
    }

    public void setDestinationUriTemplateFields(ObservableMap<URI, ObservableList<String>> destinationUriTemplateFields) {
        _DestinationUriTemplateFields = destinationUriTemplateFields;
    }

    public void setLinkRelationId(URI linkRelationId) {
        _LinkRelationId = linkRelationId;
    }

    public void setRequestMediaTypes(ObservableList<MediaType> requestMediaTypes) {
        _RequestMediaTypes = requestMediaTypes;
    }

    public void setResponseMediaTypes(ObservableList<MediaType> responseMediaTypes) {
        _ResponseMediaTypes = responseMediaTypes;
    }

    public void setSourceResourceTemplateId(URI sourceResourceTemplateId) {
        _SourceResourceTemplateId = sourceResourceTemplateId;
    }

}
