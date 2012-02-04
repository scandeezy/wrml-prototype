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
 * A LinkTemplate exists within a specific {@link Api}'s design metadata. An
 * Api's ResourceTemplates are the nodes in a web-like graph and the
 * LinkTemplates are the silk that holds the API's hyperlinked resource graph
 * together.
 * 
 * A LinkTemplate captures the metadata associated with the design-time linking
 * of two {@link ResourceTemplate}s. Visually, two ResourceTemplates, R and E,
 * are the two ends of the LinkTemplate's ASCII arrow representation R---->E,
 * where R is the referrer and E is the end-point.
 * 
 * As a webbed graph, each resource node has links pointing to
 * it (with its id in an href) and also may be capable of generating
 * representational documents which may contain references, using links to
 * other nodes. From a LinkTemplate's point of view, it is connecting a
 * referrer to his end point.
 * 
 * Conceptually, a "link" starts from a resource because it is embedded in
 * some specific document model that is associated/connected with that resource.
 * This connection is often because the link's associated document model is a
 * representation of the resource's state. This starting model is the link's
 * referrer.
 * 
 * A link ends with its pointy-end pointing to the end point. This end point is
 * the resource that the referrer resource was referencing with its link.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
// Generated from a Web Resource Schema
public interface LinkTemplate extends Versioned, Document {

    /**
     * LinkTemplate instances exist within the scope of a single {@link Api}.
     * 
     * @return the REST API that this LinkTemplate was designed for.
     */
    public Api getApi();

    /**
     * LinkTemplate instances exist within the scope of a single {@link Api}.
     * 
     * @return the id of the REST API that this LinkTemplate was designed for.
     */
    public URI getApiId();

    /**
     * If this LinkTemplate refers to an end point with a variable/templated
     * URI, then this method may return a list of models which describe how the
     * variable values may be filled in by default. Otherwise this method will
     * return <code>null</code>.
     * 
     * In many cases, the URI template's parameters cannot be filled in with any
     * logical default values, in such cases this method will return
     * <code>null</code>.
     * 
     * @return either the default means to make an HREF from a URI Template or
     *         <code>null</code>.
     */
    public ObservableList<UriTemplateParameter> getDefaultEndPointParams();

    /**
     * If this LinkTemplate describes R---->E, then this method returns E.
     * 
     * @return E
     */
    public ResourceTemplate getEndPoint();

    /**
     * If this LinkTemplate describes R---->E, then this method returns E's id.
     * 
     * @return E's id
     */
    public URI getEndPointId();

    /**
     * The ASCII art needs an additional annotation to introduce the
     * {@link LinkRelation}. So now R---->E, must become a little bit more
     * descriptive: R--{ rel : "parent"}-->E, do denote that R is referring to E
     * because of a "parent" relationship.
     * 
     * @return the reason that R is linking to E.
     */
    public LinkRelation getLinkRelation();

    /**
     * Get the id associated with the {@link LinkRelation}, which describes the
     * relationship between our two {@link ResourceTemplate}s.
     * 
     * @return the id of the LinkRelation.
     */
    public URI getLinkRelationId();

    /**
     * If this LinkTemplate describes R---->E, then this method returns R.
     * 
     * @return R
     */
    public ResourceTemplate getReferrer();

    /**
     * If this LinkTemplate describes R---->E, then this method returns R's id.
     * 
     * @return R's id
     */
    public URI getReferrerId();

    /**
     * If this LinkTemplate refers to an end point with a variable/templated
     * URI, then this method may return a mapping of schema id to list of models
     * which describe how the variable values may be filled in by models of the
     * schema's form. Otherwise this method will return <code>null</code>.
     * 
     * How can the set of possible schema ids determined/defaulted at design
     * time? This question is important because the WRML runtime needs a way
     * to use LinkTemplate metadata to generate fully-qualified "href" values
     * for all possible representational permutations of the relationship
     * between R and E.
     * 
     * First, determine the set of {@link MediaType}s that can be generated from
     * other LinkTemplates with end points pointing at this LinkTemplate's
     * referrer {@link ResourceTemplate}.
     * 
     * Second, recognize that {@link MediaType}s may be WRML media types
     * (application/wrml) with a Schema ids in the media type's "schema" param
     * and inspect the list of media types to determine the set of all schema
     * ids that can be generated from other LinkTemplates with end points
     * pointing at this LinkTemplate's referrer ResourceTemplate.
     * 
     * @return a mapping of schema id to list of parameters that model's will
     *         find helpful to click through their links.
     */
    public ObservableMap<URI, ObservableList<UriTemplateParameter>> getReferrerSchemaToEndPointParams();

    /**
     * The list of {@link MediaType}s that a link with this LinkTemplate is
     * known
     * to accept as "Content Types" in the *request* message's body.
     * 
     * By default this list is <code>null</code> because the LinkTemplate's
     * associated {@link LinkRelation} already may have captured the information
     * in {@link LinkRelation#getRequestMediaTypes()}.
     * 
     * Given the nature of HTTP, this list only applies to LinkTemplates's that
     * describe a PUT or POST-based relationship. LinkTemplate instances using
     * other methods will return <code>null</code>.
     * 
     * @return the list of content types allowed in request bodies associated
     *         with clicks using this LinkTemplate. Returns <code>null</code> in
     *         some cases (see above).
     */
    public ObservableList<MediaType> getRequestMediaTypes();

    /**
     * The list of {@link MediaType}s that a link with this LinkTemplate is
     * known
     * to return as "Content Types" in the *response* message's body.
     * 
     * By default this list is <code>null</code> because the LinkTemplate's
     * associated {@link LinkRelation} already may have captured the information
     * in {@link LinkRelation#getResponseMediaTypes()}.
     * 
     * Given the nature of HTTP, this list only applies to LinkTemplate's that
     * describe an interaction which is expected to result in a response message
     * containing some content in its body. LinkTemplate instances which
     * describe interactions that are expected to result in response messages
     * with zero byte bodies will return <code>null</code>.
     * 
     * @return the list of content types allowed in request bodies associated
     *         with clicks using this LinkTemplate. Returns <code>null</code> in
     *         some cases (see above).
     */
    public ObservableList<MediaType> getResponseMediaTypes();

    /**
     * If this LinkTemplate describes R---->E, then this method sets E's id.
     * 
     * @param endPointId
     *            the id of our end point {@link ResourceTemplate}, R.
     * @return E's previous id
     */
    public URI setEndPointId(URI endPointId);

    /**
     * Set the id associated with the {@link LinkRelation}, which describes the
     * relationship between our two {@link ResourceTemplate}s.
     * 
     * @return the id of the LinkRelation.
     */
    public URI setLinkRelationId(URI linkRelationId);

    /**
     * If this LinkTemplate describes R---->E, then this method sets R's id.
     * 
     * @param referrerId
     *            the id of our referrer (or starting point)
     *            {@link ResourceTemplate}, R.
     * @return R's previous id
     */
    public URI setReferrerId(URI referrerId);

}
