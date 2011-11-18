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

/**
 * A WRMLObject instance's Link. This class represents a link "instance", that
 * is a link with a fully qualified href URI value that can be used to interact.
 * 
 * A link is enabled if the WrmlObject's schema's associated link formula
 * evaluates to true. Instances of this class are responsible for managing their
 * enabled state changes by listening to events from the fields that their
 * LinkFormula relies upon.
 */
public interface Link extends Member, Comparable<Link> {

    public void addEventListener(LinkEventListener listener);

    public URI getHref();

    // public Bag<String, Field<?>> getDestinationUriTemplateFields();

    public LinkTemplate getLinkTemplate();

    public WrmlObject getOwner();

    public boolean isEnabled();

    public void removeEventListener(LinkEventListener listener);
}
