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

package org.wrml.core.model.schema;

import java.net.URI;

import org.wrml.core.Model;
import org.wrml.core.model.api.ResourceTemplate;
import org.wrml.core.util.observable.ObservableList;
import org.wrml.core.www.MediaType;

/**
 * A description of a hypermedia-graph-based relationship between two web
 * resources.
 * 
 * A {@link Schema}'s Link is sort of like an abstract function. A Link
 * describes a function that is callable at runtime, where the Schema has been
 * instantiated as a {@link Model}, which also has a reference to a
 * {@link ResourceTemplate} that helps guide it (as the referrer) toward the
 * Link's referenced resource.
 */
public interface Link extends SchemaMember<Link> {

    /**
     * Get the Link's default href value, or <code>null</code> if there is no
     * default.
     * 
     * @return the default href
     */
    public URI getHref();

    /**
     * Get the {@link LinkRelation} that uniquely identifies this Link within
     * the context of its {@link Schema} owner.
     * 
     * @return the LinkRelation that helps explain the meaning of this Link.
     */
    public LinkRelation getRel();

    /**
     * Get the id of the {@link LinkRelation} that lends semantics to this Link.
     * 
     * @return the id of the LinkRelation
     */
    public URI getRelId();

    /**
     * The list of {@link MediaType}s that this Link is known to accept as
     * "Content Types" in the *request* message's body.
     * 
     * By default this list is <code>null</code> because the Link's associated
     * {@link LinkRelation} already may have captured the information in
     * {@link LinkRelation#getRequestTypes()}.
     * 
     * Given the nature of HTTP, this list only applies to Link's that
     * describe a PUT or POST-based relationship. Link instances using
     * other methods will return <code>null</code>.
     * 
     * @return the list of content types allowed in request bodies associated
     *         with clicks using this Link. Returns <code>null</code> in some
     *         cases (see above).
     */
    public ObservableList<MediaType> getRequestTypes();

    /**
     * The list of {@link MediaType}s that this Link is known to return as
     * "Content Types" in the *response* message's body.
     * 
     * By default this list is <code>null</code> because the Link's associated
     * {@link LinkRelation} already may have captured the information in
     * {@link LinkRelation#getResponseTypes()}.
     * 
     * Given the nature of HTTP, this list only applies to Link's that describe
     * an interaction which is expected to result in a response message
     * containing some content in its body. Link instances which describe
     * interactions that are expected to result in response messages with zero
     * byte bodies will return <code>null</code>.
     * 
     * @return the list of content types allowed in request bodies associated
     *         with clicks using this Link. Returns <code>null</code> in some
     *         cases (see above).
     */
    public ObservableList<MediaType> getResponseTypes();

    /**
     * Set the default href, assuming that it can be known to identify some
     * meaningful resource.
     * 
     * @param href
     *            the default value for this Link's target href.
     * 
     * @return the previously held value.
     */
    public URI setHref(URI href);

}
