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

package org.wrml.core;

import java.net.URI;
import java.util.Map;

import org.wrml.core.model.api.Api;
import org.wrml.core.model.api.LinkTemplate;
import org.wrml.core.model.schema.Link;
import org.wrml.core.model.schema.LinkRelation;
import org.wrml.core.model.schema.Schema;
import org.wrml.core.runtime.event.LinkEventListener;

/**
 * An "embedded" instance of a link that is singularly "owned" by a specific
 * {@link Model} instance, which is known as the referrer because it uses a
 * hyperlink (reference) to refer to another resource end-point within a
 * web-oriented graph.
 */
public interface Hyperlink {

    /**
     * Add the specified {@link LinkEventListener}.
     * 
     * @param listener
     *            the LinkEventListener that will receive event-based
     *            notifications.
     */
    public void addEventListener(LinkEventListener listener);

    /**
     * Click the link to invoke its underlying "function".
     * 
     * @param nativeReturnType
     *            the caller's requested Java return type.
     * @param requestEntity
     *            an optional entity that, if present, will be set as the
     *            content of the click's underlying HTTP request message.
     * @param hrefParams
     *            TODO
     * @return the result of clicking this Hyperlink.
     */
    public Object click(java.lang.reflect.Type nativeReturnType, Object requestEntity, Map<String, String> hrefParams);

    /**
     * The fully qualified URI that will be used by
     * {@link #click(java.lang.reflect.Type, Object, Map)}.
     * 
     * @return a clickable URI
     */
    public URI getHref();

    /**
     * Get the {@link Link} that resides within the {@link Schema} of our
     * referrer {@link Model}.
     * 
     * @return the schematic metadata of the Link.
     * 
     * @see Hyperlink#getReferrer()
     * @see Model#getSchema()
     * @see Schema#getLinks()
     */
    public Link getLink();

    /**
     * Get the {@link LinkRelation} that lends much-needed semantics to this
     * Hyperlink.
     * 
     * @return the all-important LinkRelation
     */
    public LinkRelation getLinkRelation();

    /**
     * Get the id of the Hyperlink's shared {@link LinkRelation}.
     * 
     * @return the LinkRelation's "Universal Document Identifier".
     */
    public URI getLinkRelationId();

    /**
     * Get the {@link LinkTemplate} that binds this Hyperlink within the scope
     * of a specific {@link Api} in order to power the hypermedia engine.
     * 
     * @return the {@link LinkTemplate} associated with this Hyperlink
     */
    public LinkTemplate getLinkTemplate();

    /**
     * Get the referrer, or "starting point" of this Hyperlink reference.
     * 
     * @return the Model that contains this Hyperlink reference.
     */
    public Model getReferrer();

    /**
     * Is this Hyperlink currently enabled? Call me to find out.
     * 
     * @return <code>true</code> if this Hyperlink can be clicked
     */
    public boolean isEnabled();

    /**
     * Remove the specified {@link LinkEventListener}.
     * 
     * @param listener
     *            the quitter.
     */
    public void removeEventListener(LinkEventListener listener);

}
