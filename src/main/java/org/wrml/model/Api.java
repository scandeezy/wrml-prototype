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

package org.wrml.model;

import java.net.URI;

/**
 * This interface holds the REST API's metadata and the docroot of the
 * API's resource model.
 */

// Generated from a Web Resource Schema
public interface Api extends Named, Titled, Document {

    // Note: Added to Field
    //     Name: name 
    //     Constraints: TextSyntax - Mixed-Upper Case

    public ResourceTemplate getDocroot();

    public URI getDocrootId();

    public Collection<LinkTemplate> getLinkTemplates();

    public URI getLinkTemplatesCollectionId();
}
