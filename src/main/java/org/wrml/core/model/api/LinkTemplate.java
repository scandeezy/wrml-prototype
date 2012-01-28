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

import java.net.URI;

import org.wrml.core.model.Document;
import org.wrml.core.model.UriTemplateParameter;
import org.wrml.core.model.Versioned;
import org.wrml.core.model.schema.LinkRelation;
import org.wrml.core.util.observable.ObservableList;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.www.MediaType;

/**
 * A Link template exists within a specific API's design metadata. It is part of
 * a Resource Template which is specific to a design-time linking of two REST
 * API resource templates.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
// Generated from a Web Resource Schema
public interface LinkTemplate extends Versioned, Document {

    public ResourceTemplate getEndPoint();

    public URI getEndPointId();

    public ObservableList<UriTemplateParameter> getEndPointParamDefaults();

    public ObservableMap<URI, ObservableList<UriTemplateParameter>> getEndPointParams();

    public LinkRelation getLinkRelation();

    public URI getLinkRelationId();

    public ResourceTemplate getReferrer();

    public URI getReferrerId();

    public ObservableList<MediaType> getRequestTypes();

    public ObservableList<MediaType> getResponseTypes();

    public Api getRestApiTemplate();

    public URI getRestApiTemplateId();

    public URI setEndPointId(URI endPointId);

    public URI setLinkRelationId(URI linkRelationId);

    public URI setReferrerId(URI startingPointId);

}
