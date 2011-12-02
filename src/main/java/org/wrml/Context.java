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

import org.wrml.api.ApiTemplate;
import org.wrml.api.LinkTemplate;
import org.wrml.api.ResourceTemplate;
import org.wrml.communication.LinkRelation;
import org.wrml.communication.MediaType;
import org.wrml.schema.Constraint;
import org.wrml.schema.Schema;

public interface Context {

    // TODO Expose a set of service-oriented interfaces here

    LinkRelation getLinkRelation(URI id);

    Schema getSchema(URI id);

    MediaType getMediaType(String mediaType);

    ResourceTemplate getResourceTemplate(URI id);

    ApiTemplate getApiTemplate(URI id);

    LinkTemplate getLinkTemplate(URI id);

    Constraint<?> getConstraint(URI id);

}
