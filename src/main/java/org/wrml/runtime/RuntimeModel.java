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

package org.wrml.runtime;

import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.wrml.Model;
import org.wrml.bootstrap.FieldNames;
import org.wrml.event.FieldEvent;
import org.wrml.event.FieldEventListener;
import org.wrml.event.LinkEventListener;
import org.wrml.event.ModelEventListener;
import org.wrml.model.Document;
import org.wrml.model.DocumentMetadata;
import org.wrml.model.DocumentOptions;
import org.wrml.model.api.ResourceTemplate;
import org.wrml.model.schema.Schema;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.util.FieldMap;
import org.wrml.util.MediaType;
import org.wrml.util.observable.MapEvent;
import org.wrml.util.observable.MapEventListener;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;

/**
 * <pre>
 * NOTE: This comment's text is a work-in-progress.
 * </pre>
 * 
 * <h1>Greetings Program!</h1>
 * 
 * <p>
 * The base class for all web resource schema instances. It provides a set of
 * features intended to attract developers wanting to work with an easy-to-use
 * REST-based application framework.
 * </p>
 * 
 * <h1>Feature 1</h1>
 * 
 * <h2>Summary</h2>
 * 
 * <p>
 * Language-independent web resource schema metadata definitions yield
 * auto-generated subclasses that provide a simple POJO-oriented/bean API of
 * setters and getters for accessing Field values.
 * </p>
 * 
 * <h2>Introduction</h2>
 * 
 * <p>
 * The first feature of this class is that it exists as a key component within a
 * framework which leverages dynamic code generation. In fact, it is the direct
 * base class of all auto-generated subclasses. The subclasses inherit this
 * class's dynamic map-oriented API but also expose the JavaBean-style property
 * access methods, a.k.a. the "getters" and "setters". These methods are of
 * course auto-generated from the metadata provided by the object's web resource
 * schema. As a convenient alternative form of "introspection", this base
 * class's API provides a simple, more dynamic Map-based API for accessing field
 * values based on the (String) field name. Note that their underlying property
 * access is done via a quick calls to the same Map of field names to values.
 * </p>
 * 
 * <h2>The JavaBean Trio</h2>
 * 
 * <p>
 * From a single schema document, the WRML Java framework auto-generates the
 * triplet of: JavaBean-style interfaces and classes (impls), along with an
 * accompanying BeanInfo class which carries the schema's own metadata (e.g.
 * Human-friendly Titles and Descriptions). This class is the base class of the
 * impls - the auto-generated classes.
 * </p>
 * 
 * <p>
 * These three types represent the schema in Java and any of its related dynamic
 * scripting/templating languages that run on a JVM and interoperate with its
 * type system. This feature means that JavaBean impls, interfaces, and
 * documentation no longer needs to be designed or written by programmers at
 * all. More on that later...
 * </p>
 * 
 * <h1>Feature 2</h1>
 * 
 * <h2>Summary</h2>
 * 
 * <p>
 * This class is part of a framework that intends to go beyond Java and be
 * universal to demonstrate the Web's potential to enable global programmatic
 * interoperability.
 * </p>
 * 
 * <h2>Schema REST API</h2>
 * 
 * <p>
 * The framework's inclusion of REST APIs as a core component means that Web
 * schemas are expected to be available, and perhaps even openly shared and
 * standardized, World Wide. The source code for the Schema REST API will be
 * made freely available so that it can be highly-mirrored (HA/DR) all across
 * the globe.
 * </p>
 * 
 * <p>
 * In the Java rendition of WRML, the REST API dedicated to serving the web
 * resource schema documents is positioned metaphorically within a ClassLoader.
 * Lazily the SchemaLoader must interact with the origin Schema REST API in
 * order to load in new type definitions as demanded by the ClassLoader. The
 * ClassLoader itself bridges the JVM's idea of types (Bean interfaces, impls,
 * and infos) with WRML's notion of schemas.
 * </p>
 * 
 * 
 * <h2>Developer Productivity</h2>
 * 
 * <p>
 * For statically typed languages like Java, one can imagine extending this idea
 * further to support development-time tools so that these types can be
 * statically referenced, shared, and checked at compile-time (e.g. with hooks
 * for: Maven, Eclipse, IntelliJ, javadoc, javac, etc). This way Java-based
 * applications can truly be coded against Web-based resources.
 * </p>
 * 
 * <h2>Universal</h2>
 * 
 * <p>
 * But even more than that, WRML exposes these shared
 * "data structure contracts", as Web-based resources within a globally
 * available programming platform. We can store our type definitions "in the
 * cloud" so that we can also easily share them across the Web to other clients
 * and servers.
 * </p>
 * 
 * <p>
 * A web resource schema positioned in the cloud can be used within a
 * Java-based, WRML-compliant REST API server just as well as it can be used by
 * a program written with: JavaScript, Objective-C, Java, HTML, Ruby, PHP,
 * Python, PHP, C#, C++, Perl, Scheme, Prolog, PostScript etc. It is up to the
 * WRML-compliant frameworks in each language/platform to make the most out of
 * the idea of a shared type system.
 * </p>
 * 
 * <p>
 * This class is part of the framework that brings the idea to Java programs.
 * </p>
 * 
 * <h1>Feature 3</h1>
 * 
 * <h2>Summary</h2>
 * 
 * <p>
 * This class implements the event registration and firing logic needed to
 * notify registered listeners when a field's value is initialized, changed, or
 * has a constraint violation.
 * </p>
 * 
 * <h2>Observable Models</h2>
 * 
 * <p>
 * This class enables subclasses to be observed by interested parties. In a Java
 * standard manner, this class enables registration of event listeners and
 * delivers notifications via events. Note that these events are fired
 * regardless of interface used to set the field (dynamic map access versus
 * subclass's bean property accessor).
 * </p>
 * 
 * <p>
 * This is a very handy feature that is even used internally to continually
 * automate HATEOAS and, optionally via REST's code-on-demand, validate
 * constraints with violation checks whenever field values are set. Features
 * like these are especially useful when building web form-based editing views
 * with a menu of actions associated with the active data models.
 * </p>
 * 
 * <p>
 * Note that the lazy creation of the event dispatchers means that there is near
 * zero overhead if no views are attached.
 * </p>
 * 
 * <h1>Feature 4</h1>
 * 
 * <h2>Summary</h2>
 * 
 * <p>
 * TODO
 * </p>
 * 
 * <h2>Context Aware</h2>
 * 
 * <p>
 * TODO Runtime Context Awareness Aware of own Schema - Constraints, ReadOnly
 * Aware of the API's metadata (interlinking of resources)
 * </p>
 * 
 * <h1>Feature 5</h1>
 * 
 * <h2>Summary</h2>
 * 
 * <p>
 * The Model Java API will automagically manage the boolean enabled state and
 * the href value of Links, which together represent the object’s available,
 * Field-sensitive interaction options.
 * </p>
 * 
 * <h2>Automated HATEOAS</h2>
 * 
 * <p>
 * TODO
 * </p>
 * 
 * 
 * 
 * <h2>Fill in the Blanks</h2>
 * 
 * <p>
 * TODO
 * </p>
 * 
 * <h1>Feature 6</h1>
 * 
 * <h2>Summary</h2>
 * 
 * <p>
 * TODO
 * </p>
 * 
 * <h2>Client/Tool Friendly</h2>
 * 
 * <p>
 * TODO Codify a standard REST API design as a reusable Web Resource Server
 * Engine with a consistent step-wise request handling approach and a rich set
 * of client-friendly features (e.g. partial response, pagination, JSONP/CORS
 * support, and OAuth-based resource protection).
 * </p>
 * 
 * <p>
 * This component will free up our server developers to focus on the real meat
 * of their problem, which is how to adapt a backend system’s specific data and
 * functions to the REST API’s flexible front-end data model. WRML leaves this
 * up to developers to implement as the Backend Connection Interface.
 * </p>
 * 
 * <p>
 * REST APIs and client implementations depend too heavily on the format of
 * response body content Pluggable Formats
 * </p>
 * 
 * <p>
 * Mobile - one schema per screen Visual design tools
 * </p>
 * 
 * <p>
 * RESTful web services rarely use HATEOAS. Modern so-called RESTful web
 * services feature web APIs that make little effort to achieve achieve level
 * three (hypermedia) maturity on Leonard Richardson’s scale.
 * </p>
 * 
 * <p>
 * HATEOAS is made easy and "hidden" by the framework (LinkFormulas,
 * LinkRelations etc).
 * </p>
 * 
 * <p>
 * Useful, generic commercial web services lock-in Like the web platform's
 * common data structures Clients of S3 and similar web services would benefit
 * from the existence of a shared, open standard "Document Store REST API"
 * definition with different vendors offering different implementations of the
 * same interface, similar to modern CDN market.
 * </p>
 * 
 */
public final class RuntimeModel implements Model {

    private Model _StaticInterface;

    private static final long serialVersionUID = -7696720779481780624L;

    private final ObservableMap<String, Object> _Fields;
    private final ObservableMap<URI, HyperLink> _Links;

    private transient FieldMapEventListener _FieldMapEventListener;

    private final transient Context _Context;

    private final URI _SchemaId;

    private URI _ResourceTemplateId;

    private final List<URI> _EmbeddedLinkRelationIds;
    private transient List<ModelEventListener> _EventListeners;
    private transient Map<String, List<FieldEventListener>> _FieldEventListeners;

    RuntimeModel(Context context, URI schemaId, List<URI> embeddedLinkRelationIds, FieldMap fieldMap,
            Map<URI, HyperLink> linkMap) {

        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        if (schemaId == null) {
            throw new NullPointerException("Schema ID cannot be null");
        }

        _Context = context;
        _SchemaId = schemaId;
        _EmbeddedLinkRelationIds = embeddedLinkRelationIds;

        _Fields = Observables.observableMap(fieldMap);
        _FieldMapEventListener = new FieldMapEventListener();
        _Fields.addMapEventListener(_FieldMapEventListener);

        _Links = Observables.observableMap(linkMap);

        init();

        // TODO: Self-register for ID changes so that we can figure out where we are in the API.
        // Use the Context to gain access to the configured APIs which will then tell you which 
        // API you match.

        // Look up the API and initialize the hypermedia engine.

        // This should be helpful on both client and server-side.

    }

    public void addEventListener(ModelEventListener listener) {
        if (_EventListeners == null) {
            _EventListeners = new CopyOnWriteArrayList<ModelEventListener>();
        }

        _EventListeners.add(listener);
    }

    public void addFieldEventListener(String fieldName, FieldEventListener listener) {
        if (_FieldEventListeners == null) {
            _FieldEventListeners = new HashMap<String, List<FieldEventListener>>();
        }

        List<FieldEventListener> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        if (fieldEventListenerList == null) {
            fieldEventListenerList = new CopyOnWriteArrayList<FieldEventListener>();
            _FieldEventListeners.put(fieldName, fieldEventListenerList);
        }

        fieldEventListenerList.add(listener);
    }

    /*
     * public void delete() {
     * final URI id = getId();
     * if (id == null) {
     * return;
     * }
     * 
     * // Go all the way to the origin to force a hard delete
     * final Service myOriginService = getMyOriginService();
     * final Model newThis = myOriginService.remove(id, this);
     * 
     * // TODO: Do we need to die here or can an event trigger
     * // that from somewhere else?
     * }
     */

    public void addLinkEventListener(URI linkRelationId, LinkEventListener listener) {
        // TODO Auto-generated method stub

    }

    public Object clickLink(URI rel, MediaType responseType, Object requestEntity, Map<String, String> hrefParams) {
        final HyperLink hyperLink = getHyperLink(rel);

        if (hyperLink == null) {
            // TODO: Error here instead?
            return null;
        }

        return hyperLink.click(responseType, requestEntity, hrefParams);
    }

    public void die() {
        // TODO: Fire an event about vanishing

        // TODO: Unregister all listeners

        // TODO: Clear caches or do caches auto-clear from an 
        // vanish event notification?

    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void extend(Model modelToExtend, Model... additionalModelsToExtend) {

        final RuntimeModel modelToExtendDynamicInterface = (RuntimeModel) modelToExtend.getDynamicInterface();
        _Fields.putAll(modelToExtendDynamicInterface._Fields);

        for (final Model additionalModelToExtend : additionalModelsToExtend) {

            final RuntimeModel additionalModelToExtendDynamicInterface = (RuntimeModel) additionalModelToExtend
                    .getDynamicInterface();
            _Fields.putAll(additionalModelToExtendDynamicInterface._Fields);

            // Copy any other state?
        }

    }

    public final Context getContext() {
        return _Context;
    }

    public Model getDynamicInterface() {
        return this;
    }

    public final List<URI> getEmbeddedLinkRelationIds() {
        return _EmbeddedLinkRelationIds;
    }

    @SuppressWarnings("unchecked")
    public <V> V getFieldValue(String fieldName) {
        return (V) _Fields.get(fieldName);
    }

    public final HypermediaEngine getHypermediaEngine() {
        final ResourceTemplate resourceTemplate = getResourceTemplate();
        if (resourceTemplate == null) {
            return null;
        }

        final URI apiId = resourceTemplate.getRoot().getId();
        return getContext().getHypermediaEngine(apiId);
    }

    public ObservableMap<URI, HyperLink> getLinkMap() {
        return _Links;
    }

    public DocumentMetadata getMetadata() {
        // TODO Auto-generated method stub
        return null;
    }

    public final Service getMyOriginService() {

        Service myService = getMyService();
        while ((myService != null) && (myService instanceof ProxyService)) {
            myService = ((ProxyService) myService).getOriginService();
        }

        return myService;
    }

    public final Service getMyService() {
        final URI schemaId = getSchemaId();
        final Context context = getContext();
        final Service myService = context.getService(schemaId);
        return myService;
    }

    public DocumentOptions getOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    public Document getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    public final Resource getResource() {
        return getHypermediaEngine().getResource(getResourceTemplateId());
    }

    public final ResourceTemplate getResourceTemplate() {
        final URI id = getResourceTemplateId();
        if (id == null) {
            return null;
        }

        final Service resourceTemplateService = getContext().getService(ResourceTemplate.class);
        return (ResourceTemplate) resourceTemplateService.get(id);
    }

    public final URI getResourceTemplateId() {
        return _ResourceTemplateId;
    }

    public final Schema getSchema() {
        final Context context = getContext();
        final URI schemaId = getSchemaId();
        return context.getSchema(schemaId);
    }

    public final URI getSchemaId() {
        return _SchemaId;
    }

    @SuppressWarnings("unchecked")
    public <M extends Model> M getStaticInterface() {

        if (Proxy.isProxyClass(this.getClass())) {

            return (M) this;
        }

        if (_StaticInterface != null) {
            return (M) _StaticInterface;
        }

        final URI schemaId = getSchemaId();
        final Context context = getContext();

        final Class<?> schemaInterface = context.getSchemaIdToClassTransformer().aToB(schemaId);
        //System.err.println("====== getStaticInterface() schemaId : \"" + schemaId + "\" schemaInterface : \""  + schemaInterface + "\" (" + hashCode() + ")");

        if (schemaInterface.isInstance(this)) {

            _StaticInterface = (Model) schemaInterface.cast(this);

            //System.err.println("====== NOTE: " + this + " was ALREADY static: " + _StaticInterface);
        }
        else {
            final Class<?>[] schemaInterfaceArray = new Class<?>[] { schemaInterface };
            final StaticInterfaceFacade facade = new StaticInterfaceFacade(this);

            _StaticInterface = (Model) Proxy.newProxyInstance(context, schemaInterfaceArray, facade);
        }

        return (M) _StaticInterface;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public boolean isFieldValueSet(String fieldName) {
        return (_Fields != null) && _Fields.containsKey(fieldName);
    }

    public final void removeEventListener(ModelEventListener listener) {
        if (_EventListeners == null) {
            return;
        }

        _EventListeners.remove(listener);
    }

    public final void removeFieldEventListener(String fieldName, FieldEventListener listener) {
        if (_FieldEventListeners == null) {
            return;
        }

        final List<FieldEventListener> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        if (fieldEventListenerList == null) {
            return;
        }

        fieldEventListenerList.remove(listener);
    }

    /*
     * public void save() {
     * 
     * final Service myService = getMyService();
     * myService.put(getId(), this, this);
     * 
     * // TODO: Assume write through caches on self-save (on all saves)?
     * 
     * // TODO: Assume that model update events will trigger any refresh that is
     * needed?
     * }
     */

    public final void removeLinkEventListener(URI linkRelationId, LinkEventListener listener) {
        // TODO Auto-generated method stub

    }

    public void setAllFieldsToDefaultValue() {

        // TODO: Setting default values doesn't mean clearing the fields (check the schema defaults instead).
        //_Fields.clear();

        // TODO: Consider prototype default values somewhere
    }

    public void setFieldToDefaultValue(String fieldName) {

        // TODO: Consider prototype default value somewhere

        _Fields.put(fieldName, null);

    }

    /*
     * public void refresh(boolean atomic) {
     * final URI id = getId();
     * if (id == null) {
     * return;
     * }
     * 
     * // Go all the way to the origin to force a re-GET of "self"
     * final Service myOriginService = getMyOriginService();
     * 
     * final Model newThis = myOriginService.get(id, this);
     * become(newThis, atomic);
     * }
     */

    @SuppressWarnings("unchecked")
    public <V> V setFieldValue(String fieldName, V fieldValue) {
        return (V) _Fields.put(fieldName, fieldValue);
    }

    @Override
    public String toString() {

        final String idKey = FieldNames.Document.id.toString();
        final String nameKey = FieldNames.Named.name.toString();

        final Set<String> fieldNames = _Fields.keySet();

        Object id = null;
        if (fieldNames.contains(idKey)) {
            id = _Fields.get(idKey);
        }

        Object name = null;
        if (fieldNames.contains(nameKey)) {
            name = _Fields.get(nameKey);
        }

        final StringBuilder sb = new StringBuilder();
        sb.append("\n\nModel : {\n");
        sb.append("    hashCode : " + hashCode() + ",\n");
        sb.append("    schemaId : " + _SchemaId + ",\n");
        sb.append("    id : " + id + ",\n");
        sb.append("    name : " + name + "\n");

        sb.append("    fieldNames : [\n");
        sb.append("   ");
        for (final String fieldName : fieldNames) {
            sb.append(fieldName);
            sb.append(", ");
        }
        sb.append("\n    ],\n");

        sb.append("    fields : {\n");
        sb.append("   ");
        sb.append(_Fields);
        sb.append("\n    }\n");

        sb.append("}\n");

        return sb.toString();

    }

    /**
     * Called to initialize the Model.
     */
    protected void init() {
        setAllFieldsToDefaultValue();
    }

    private void fireConstraintViolated(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (final FieldEventListener fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }

            fieldEventListener.constraintViolated(event);
        }
    }

    private void fireValueChanged(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (final FieldEventListener fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }

            fieldEventListener.valueChanged(event);
        }

    }

    private void fireValueInitialized(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (final FieldEventListener fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }

            fieldEventListener.valueInitialized(event);
        }
    }

    private HyperLink getHyperLink(URI rel) {

        if (!_Links.containsKey(rel)) {
            final HyperLink link = new HyperLink(this, rel);
            _Links.put(rel, link);
        }

        return _Links.get(rel);
    }

    private class FieldMapEventListener implements MapEventListener<String, Object> {

        // TODO Run constraint validators

        // TODO Consider the read only status of this object

        // TODO Consider the read only status of the schema's field

        // TODO Fire field events

        public void cleared(MapEvent<String, Object> event) {
            // TODO Auto-generated method stub

        }

        public void clearing(MapEvent<String, Object> event) {
            // TODO Auto-generated method stub

        }

        public void entryInserted(MapEvent<String, Object> event) {
            // TODO Auto-generated method stub

        }

        public void entryRemoved(MapEvent<String, Object> event) {
            // TODO Auto-generated method stub

        }

        public void entryUpdated(MapEvent<String, Object> event) {
            // TODO Auto-generated method stub

        }

        public void insertingEntry(MapEvent<String, Object> event) {
            // TODO Auto-generated method stub

        }

        public void removingEntry(MapEvent<String, Object> event) {
            // TODO Auto-generated method stub

        }

        public void updatingEntry(MapEvent<String, Object> event) {
            // TODO Auto-generated method stub

        }

    }

}
