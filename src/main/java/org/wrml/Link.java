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
import java.util.List;

import org.wrml.model.communication.http.Method;
import org.wrml.model.relation.LinkRelation;
import org.wrml.model.restapi.Document;
import org.wrml.model.restapi.LinkTemplate;
import org.wrml.model.restapi.ResourceTemplate;
import org.wrml.util.ObservableMap;

/**
 * A Model instance's Link. This class represents a link "instance", that is a
 * link with a fully qualified href URI value that can be used to interact.
 * 
 * A link is enabled if the Model's schema's associated link formula evaluates
 * to true. Instances of this class are responsible for managing their enabled
 * state changes by listening to events from the fields that their LinkFormula
 * relies upon.
 */
public final class Link {

    private static final long serialVersionUID = -6235652220661484935L;

    private final Model _Owner;
    private final URI _LinkRelationId;
    private URI _Href;
    private boolean _Enabled;

    public Link(Model owner, URI linkRelationId) {
        _Owner = owner;
        _LinkRelationId = linkRelationId;
    }

    public void addEventListener(LinkEventListener listener) {
        // TODO
    }

    public Model click() {
        return click(null);
    }

    public Model click(URI responseModelSchemaId) {
        return click(responseModelSchemaId, null);
    }

    public Model click(URI responseModelSchemaId, Model requestModel) {

        /*
         * TODO // Check to see if this link is currently enabled before
         * proceeding if (!isEnabled()) { // TODO: Throw an exception of some
         * sort return null; }
         * 
         * MediaType requestMediaType = null;
         * 
         * if (requestModel != null) { // TODO: // Determine default media type
         * by looking at the link template and // then the link relation. // Go
         * through each list comparing the requestModel's schema URI to // the
         * wrml media type's schema parameter, or if a non wrml media // type is
         * used then look up a Format using the "raw" media type // (e.g.
         * application/json maps to the json Format) }
         */

        return click(responseModelSchemaId, requestModel, null);
    }

    public Model click(URI responseModelSchemaId, Model requestModel, List<String> hrefParams) {

        // Check to see if this link is currently enabled before proceeding
        if (!isEnabled()) {
            // TODO: Throw an exception of some sort
            return null;
        }

        final LinkRelation linkRelation = getLinkRelation();

        // TODO
        /*
         * if (responseModelSchemaId != null &&
         * !linkRelation.isGeneratableResponseSchema(responseModelSchemaId)) {
         * // TODO: Preemptively throw "406 Not Acceptable" exception
         * return null;
         * }
         * 
         * URI requestModelSchemaId = null;
         * if (requestModel != null) {
         * requestModelSchemaId = requestModel.getSchemaId();
         * 
         * if (requestModelSchemaId != null &&
         * !linkRelation.isSupportedRequestSchema(requestModelSchemaId)) {
         * // TODO: Preemptively throw "415 Unsupported Media Type"
         * // exception
         * return null;
         * }
         * }
         */

        final Model owner = getOwner();

        // TODO: The last minute hrefParams is a possibly half-baked way to fill
        // in any remaining URI Template params, such as the client-assigned
        // "name" of a first time stored (PUT) resource.
        final URI href = getHref(hrefParams);
        final Method method = linkRelation.getMethod();

        final Model responseModel = null;

        // TODO: Is it weird to be doing this in this class? Should this logic
        // be more pluggable in some way? In some ways it is nice for this magic
        // to take place inside the Link since metaphorically that's not far
        // off. Also, it codifies these semantic method mapping rules
        // permanently, which may be a good thing.
        //
        // Bottom line, consider refactoring.

        // TODO
        /*
         * switch (method) {
         * 
         * case GET:
         * Service responseModelService = owner.getContext().getService(owner,
         * responseModelSchemaId);
         * responseModel = responseModelService.get(href,
         * responseModelSchemaId);
         * break;
         * 
         * case PUT:
         * Service requestModelService = owner.getContext().getService(owner,
         * requestModelSchemaId);
         * responseModel = requestModelService.save(href, requestModel);
         * break;
         * 
         * default:
         * // TODO: Preemptively throw "405 Method Not Allowed"
         * 
         * // OPTIONS("OPTIONS", false, true), GET("GET", true, true),
         * // HEAD("HEAD", true, true), POST("POST", false, false), PUT( "PUT",
         * // false, true), DELETE("DELETE", false, true), TRACE("TRACE",
         * // false, true), CONNECT("CONNECT", false, false);
         * 
         * }
         */

        // TODO

        return responseModel;

    }

    public void fireHrefChangedEvent() {

    }

    public URI getHref() {
        return _Href;
    }

    /*
     * TODO: Brian Jackson, please review this design idea...
     * 
     * I am starting to believe that autogenerated subclasses of Model can have
     * an generated method for each combination of link relation and
     * request/response Schema. Sort of like Hibernate for the Web.
     * 
     * For example, this approach would imply that subclasses that "implement"
     * WRML's Document schema would have a getSelf() method in Java, which under
     * the covers would click a link to return the latest version of the object.
     * ...Okay perhaps and odd example to start with.
     * 
     * This method is made possible because of the metadata provided by the
     * LinkRelation, which details the name, the possible return types (as
     * schema ids within response media types), and optional request types (as
     * schema ids within request media types). This information can be used at
     * class generation time to produce methods that have names like save or
     * save(Story story) and getAuthor(). JavaBean method names are generated
     * from concat of link rel's method, name, and return schema (if needed to
     * disambiguate). For example WRML's "GET author com/example/Author" turns
     * into Java's "public Author getAuthor()".
     * 
     * 
     * Other methods might look like:
     * 
     * 
     * The LinkRelation specified two (or more) response schema types, "Writer"
     * being one of them. Java doesn't allow overloading the return type of
     * methods, so we need to alter the name too.
     * 
     * public Writer getAuthorAsWriter();
     * 
     * See bad first example above public Story getSelf();
     * 
     * This could be an interesting way of representing Web collections ...
     * 
     * public org.wrml.Collection<Story> getParent();
     * 
     * In cases where the LinkRelation accepts a request schema type that this
     * schema instance implements, the code generation should provide a no-arg
     * version of the method that internally passes this Model (the Link's
     * owner). This would allow the JavaBean interface to have methods like
     * save() which internally "PUT" the Model to its corresponding service and
     * return the origin's version for model syncing.
     * 
     * public Story save();
     * 
     * Is it possible to determine the URI template vars names that are left
     * over after the source schema fills in params with field values? It would
     * be cool if they could be used to generate clean params names for Java
     * methods that need to result in a client-controlled resource name (with
     * initial PUT)
     * 
     * public Story save(String name);
     * 
     * A self destruct button
     * 
     * public void delete();
     * 
     * An "unsafe" action (aka controller) method that was generated from a link
     * rel that uses POST.
     * 
     * public void makeRocketGoNow();
     * 
     * These methods further extend the data available to clients using the
     * JavaBean interface of Models by providing access to linked data via
     * simple get methods. This simplified access to the resource model would be
     * really slick in a dynamic language like groovy talking directly to a REST
     * API via WRML, with object caching in the JVM of course.
     * 
     * At Link.click time, under the covers, these generated methods traverse
     * the linkage using the very same metadata to make the appropriate request
     * to the appropriate service. This is RESTful.
     * 
     * Finally, the role of the service is to be the beginning of both the
     * client-side API and the "backend connection" interface. By making the
     * services available for look-up based on schema id, context
     * implementations can choose to register one service per type or services
     * that support several different types. This design allows for the same
     * context interface to work for both client and server side uses of this
     * class. On the client side, the context is perhaps an abstraction over
     * some HTTP client making REST API calls. On the server side, the context
     * may talk to a storage subsystem to CRUD wrml objects (backend
     * connection). Its WRML's equivalent of the Web's uniform interface.
     */

    public LinkRelation getLinkRelation() {
        final Model owner = getOwner();
        return (LinkRelation) owner.getContext().getModel(LinkRelation.class, getLinkRelationId(), owner);
    }

    /*
     * TODO: Should click (or alternative method) support clicking on links that
     * deal with non-wrml media type data (e.g. JPEGs)? I am inclined to think
     * that this needs to be supported somehow. If WRML is going to take over
     * the web server it shouldn't be limited to serving "schemafied" data.
     * 
     * Consider refactoring to be more generic.
     */

    public URI getLinkRelationId() {
        return _LinkRelationId;
    }

    public LinkTemplate getLinkTemplate() {
        final Model owner = getOwner();

        if (!(owner instanceof Document)) {
            return null;
        }

        final Document document = (Document) owner;
        final ResourceTemplate resourceTemplate = document.getResourceTemplate();

        final ObservableMap<URI, URI> hereToThereLinkRelationIdToLinkTemplateIdMap = resourceTemplate
                .getHereToThereLinkRelationIdToLinkTemplateIdMap();

        final URI linkTemplateId = hereToThereLinkRelationIdToLinkTemplateIdMap.get(getLinkRelationId());

        return (LinkTemplate) document.getContext().getModel(LinkTemplate.class, linkTemplateId, document);
    }

    public Model getOwner() {
        return _Owner;
    }

    public boolean isEnabled() {
        return _Enabled;
    }

    public void removeEventListener(LinkEventListener listener) {
        // TODO
    }

    /**
     * 
     * @param hrefParams
     *            The unfinished URI template params for HTTP methods like PUT
     *            which have path segments that are known only during save
     *            operations
     * @return
     */
    private URI getHref(List<String> hrefParams) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * 
     * public void hrefFieldValueChanged(FieldEvent event) { updateHref(); }
     * public void setHref (String href) { if (_Href !.equal href) { final _
     * Href = href; fireHrefChangedEvent(); } }
     * 
     * public void updateHref() { final LinkTemplate linkTemplate =
     * getLinkTemplate(); final ResourceTemplate destination =
     * linkTemplate.getDestination(); final UriTemplate uriTemplate =
     * destination.getUriTemplate(); setHref(uriTemplate.execute(this)); }
     */

}
