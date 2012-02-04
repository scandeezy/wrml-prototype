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

import org.wrml.core.model.Collection;
import org.wrml.core.model.Document;
import org.wrml.core.model.Named;
import org.wrml.core.model.Titled;

/**
 * A REST API.
 */
public interface Api extends Named, Titled, Document {

    /**
     * Get the {@link ResourceTemplate} that is the root of the Api's
     * hierarchical resource model tree. Conventionally, the docroot resource
     * has an empty path segment value, meaning that its full URI path value is
     * simply "/".
     * 
     * @return the ResourceTemplate that describes this Api's docroot
     * 
     * @see ResourceTemplate#getPathSegment()
     */
    public ResourceTemplate getDocroot();

    /**
     * Get the id of the {@link ResourceTemplate} that is the root of this Api.
     * 
     * @return the docroot ResourceTemplate's id
     */
    public URI getDocrootId();

    /**
     * Get the Api's {@link Collection} of {@link LinkTemplate}s, which, when
     * laid on top of the Api's {@link ResourceTemplate} tree, form a hypermedia
     * graph of interrelated ResourceTemplates.
     * 
     * @return the list of LinkTemplates that describe all of the
     *         relationships between the Api's ResourceTemplates.
     */
    public Collection<LinkTemplate> getLinkTemplates();

    /**
     * Get the id of the {@link Collection} which lists the Api's
     * {@link LinkTemplate}s.
     * 
     * @return the id of the LinkTemplate Collection.
     */
    public URI getLinkTemplatesCollectionId();
}
