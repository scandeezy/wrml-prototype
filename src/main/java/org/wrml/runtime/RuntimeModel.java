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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.wrml.Model;
import org.wrml.event.FieldEvent;
import org.wrml.event.FieldEventListener;
import org.wrml.event.LinkEventListener;
import org.wrml.event.ModelEventListener;
import org.wrml.model.api.LinkTemplate;
import org.wrml.model.api.ResourceTemplate;
import org.wrml.model.communication.MediaType;
import org.wrml.model.relation.LinkRelation;
import org.wrml.model.schema.Constraint;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Link;
import org.wrml.model.schema.Schema;
import org.wrml.model.schema.Type;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.util.DelegatingInvocationHandler;
import org.wrml.util.observable.MapEvent;
import org.wrml.util.observable.MapEventListener;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;
import org.wrml.util.transformer.Transformer;

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
final class RuntimeModel implements Model {

    private Model _StaticInterface;

    private static final long serialVersionUID = -7696720779481780624L;

    private final transient Context _Context;

    private final URI _SchemaId;
    private final URI _ResourceTemplateId;

    private final List<URI> _EmbeddedLinkRelationIds;

    private ObservableMap<String, Object> _FieldMap;
    private ObservableMap<URI, LinkInstance> _LinkMap;

    private transient List<ModelEventListener> _EventListeners;
    private transient Map<String, List<FieldEventListener>> _FieldEventListeners;

    private transient FieldMapEventListener _FieldMapEventListener;

    public RuntimeModel(Context context, URI schemaId) {
        this(context, schemaId, null);
    }

    public RuntimeModel(Context context, URI schemaId, URI resourceTemplateId) {
        this(context, schemaId, resourceTemplateId, null);
    }

    public RuntimeModel(Context context, URI schemaId, URI resourceTemplateId, List<URI> embeddedLinkRelationIds) {
        _Context = context;
        _SchemaId = schemaId;
        _ResourceTemplateId = resourceTemplateId;
        _EmbeddedLinkRelationIds = embeddedLinkRelationIds;
        init();
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
     * // TODO: Do we need to vanish here or can an event trigger
     * // that from somewhere else?
     * }
     */

    public void addLinkEventListener(URI linkRelationId, LinkEventListener listener) {
        // TODO Auto-generated method stub

    }

    public Object clickLink(URI rel, MediaType responseType, Object requestEntity, Map<String, String> hrefParams) {
        LinkInstance linkInstance = getLinkInstance(rel);
        return linkInstance.click(responseType, requestEntity, hrefParams);
    }

    public void die() {
        // TODO: Fire an event about vanishing

        // TODO: Unregister all listeners

        // TODO: Clear caches or do caches auto-clear from an 
        // vanish event notification?

    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void extend(Model modelToExtend, Model... additionalModelsToExtend) {

        // TODO: Set all fields (copy from extends)

    }

    public final Context getContext() {
        return _Context;
    }

    public final List<URI> getEmbeddedLinkRelationIds() {
        return _EmbeddedLinkRelationIds;
    }

    public final Object getFieldValue(String fieldName) {

        Object rawValue = isFieldValueSet(fieldName) ? _FieldMap.get(fieldName) : null;
        Object fieldValue = rawValue;

        Prototype prototype = getPrototype();

        ObservableMap<String, Field> prototypeFields = prototype.getFields();
        Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;

        if (prototypeField != null) {
            Type type = prototypeField.getType();
            if (type == Type.Boolean) {
                Boolean booleanFieldValue = (Boolean) fieldValue;
                fieldValue = (booleanFieldValue != null) ? booleanFieldValue.booleanValue() : false;
            }
        }

        return fieldValue;

    }

    public final HypermediaEngine getHypermediaEngine() {
        ResourceTemplate resourceTemplate = getResourceTemplate();
        if (resourceTemplate == null) {
            return null;
        }

        URI apiId = resourceTemplate.getRoot().getId();
        return getContext().getHypermediaEngine(apiId);
    }

    public Service getMyOriginService() {

        Service myService = getMyService();
        while ((myService != null) && (myService instanceof ProxyService)) {
            myService = ((ProxyService) myService).getOriginService();
        }

        return myService;
    }

    public Service getMyService() {
        final URI schemaId = getSchemaId();
        final Context context = getContext();
        final Service myService = context.getService(schemaId);
        return myService;
    }

    public final Prototype getPrototype() {
        return getContext().getPrototype(getSchemaId());
    }

    public final Resource getResource() {
        return getHypermediaEngine().getResource(getResourceTemplateId());
    }

    public final ResourceTemplate getResourceTemplate() {
        URI id = getResourceTemplateId();
        if (id == null) {
            return null;
        }

        Service resourceTemplateService = getContext().getService(ResourceTemplate.class);
        return (ResourceTemplate) resourceTemplateService.get(id);
    }

    public final URI getResourceTemplateId() {
        return _ResourceTemplateId;
    }

    public final Schema getSchema() {
        return getContext().getSchema(getSchemaId());
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

    public final URI getSchemaId() {
        return _SchemaId;
    }

    public final Model getStaticInterface() {

        if (Proxy.isProxyClass(this.getClass())) {
            return this;
        }

        if (_StaticInterface != null) {
            return _StaticInterface;
        }

        URI schemaId = getSchemaId();
        Context context = getContext();

        String className = context.getClassName(schemaId);
        System.out.println("Loading schema class: " + className);
        ClassLoader schemaInterfaceLoader = context.getSchemaService().getSchemaInterfaceLoader();
        Class<?> schemaInterface = null;
        try {

            schemaInterface = schemaInterfaceLoader.loadClass(className);
        }
        catch (Throwable t) {
            // TODO Auto-generated catch block
            t.printStackTrace();
        }

        Class<?>[] schemaInterfaceArray = new Class<?>[] { schemaInterface };
        StaticModelHandler invocationHandler = new StaticModelHandler(this);

        _StaticInterface = (Model) Proxy.newProxyInstance(schemaInterfaceLoader, schemaInterfaceArray,
                invocationHandler);

        return _StaticInterface;
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    public final boolean isFieldValueSet(String fieldName) {
        return (_FieldMap != null) && _FieldMap.containsKey(fieldName);
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
    public void removeEventListener(ModelEventListener listener) {
        if (_EventListeners == null) {
            return;
        }

        _EventListeners.remove(listener);
    }

    public void removeFieldEventListener(String fieldName, FieldEventListener listener) {
        if (_FieldEventListeners == null) {
            return;
        }

        final List<FieldEventListener> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        if (fieldEventListenerList == null) {
            return;
        }

        fieldEventListenerList.remove(listener);
    }

    public void removeLinkEventListener(URI linkRelationId, LinkEventListener listener) {
        // TODO Auto-generated method stub

    }

    public final void setAllFieldsToDefaultValue() {

        final Prototype prototype = getPrototype();

        final Map<String, Field> prototypeFields = prototype.getFields();
        if (prototypeFields != null) {
            final Set<String> fieldNames = prototypeFields.keySet();
            for (final String fieldName : fieldNames) {
                setFieldToDefaultValue(fieldName);
            }
        }
    }

    public final void setFieldToDefaultValue(String fieldName) {

        final Prototype prototype = getPrototype();
        final Map<String, Field> prototypeFields = prototype.getFields();

        if ((prototypeFields != null) && prototypeFields.containsKey(fieldName)) {
            setFieldValue(fieldName, prototypeFields.get(fieldName).getDefaultValue());
        }
    }

    public final Object setFieldValue(String fieldName, Object fieldValue) {

        Prototype prototype = getPrototype();
        ObservableMap<String, Field> prototypeFields = prototype.getFields();

        Field prototypeField = (prototypeFields != null) ? prototypeFields.get(fieldName) : null;
        if (prototypeField != null) {
            if (prototypeField.isReadOnly()) {
                throw new IllegalAccessError("Field \"" + fieldName + "\" is read only in \""
                        + prototype.getBlueprintSchemaId() + "\"");
            }

            if (prototypeField.isRequired() && fieldValue == null) {
                throw new NullPointerException("Field \"" + fieldName + "\" is requires a value in \""
                        + prototype.getBlueprintSchemaId() + "\"");
            }

            Type type = prototypeField.getType();
            if (type == Type.Boolean) {
                Boolean booleanFieldValue = (Boolean) fieldValue;
                fieldValue = (booleanFieldValue != null && booleanFieldValue.booleanValue() ? Boolean.TRUE
                        : Boolean.FALSE);
            }

            ObservableList<Constraint> constraints = prototypeField.getConstraints();
            if (constraints != null && constraints.size() > 0) {
                // MSMTODO: Validate constraints
            }

        }

        if (_FieldMap == null) {
            initFieldMap();
        }

        Object oldValue = getFieldValue(fieldName);

        _FieldMap.put(fieldName, fieldValue);

        return oldValue;
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

    private LinkInstance getLinkInstance(URI rel) {

        if (_LinkMap == null) {
            initLinkMap();
        }

        if (!_LinkMap.containsKey(rel)) {
            LinkInstance link = new LinkInstance(rel);
            _LinkMap.put(rel, link);
        }

        return _LinkMap.get(rel);
    }

    private Class<?> getMediaTypeClass(MediaType responseType) {
        // TODO Auto-generated method stub
        return null;
    }

    private void initFieldMap() {
        _FieldMap = Observables.observableMap(new HashMap<String, Object>());
        _FieldMapEventListener = new FieldMapEventListener();
        _FieldMap.addMapEventListener(_FieldMapEventListener);
    }

    private void initLinkMap() {
        _LinkMap = Observables.observableMap(new HashMap<URI, LinkInstance>());
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

    /**
     * A Model instance's Link. This class represents a link "instance", that is
     * a
     * link with a fully qualified href URI value that can be used to interact.
     * 
     * A link is enabled if the Model's schema's associated link formula
     * evaluates
     * to true. Instances of this class are responsible for managing their
     * enabled
     * state changes by listening to events from the fields that their
     * LinkFormula
     * relies upon.
     */
    private class LinkInstance implements Serializable {

        private static final long serialVersionUID = -6235652220661484935L;

        private final URI _Rel;
        private URI _Href;
        private boolean _Enabled;

        public LinkInstance(URI rel) {
            _Rel = rel;
        }

        public void addEventListener(LinkEventListener listener) {
            // TODO Add an event listener
        }

        // TODO: All Service methods need to take a URI responseModelSchemaId param, it is needed to determine the requested model response type 
        public Object click(MediaType responseType, Object requestEntity, Map<String, String> hrefParams) {

            // TODO: Fire the pre-Click Event

            /*
             * Autogenerated subclasses of Model can have an generated method
             * for
             * each combination of link relation and request/response Schema.
             * Sort
             * of like Hibernate for the Web.
             * 
             * For example, this approach would imply that subclasses that
             * "implement" WRML's Document schema would have a getSelf() method
             * in
             * Java, which under the covers would click a link to return the
             * latest
             * version of the object.
             * ...Okay perhaps and odd example to start with.
             * 
             * This method is made possible because of the metadata provided by
             * the
             * LinkRelation, which details the name, the possible return types
             * (as
             * schema ids within response media types), and optional request
             * types
             * (as schema ids within request media types). This information can
             * be
             * used at class generation time to produce methods that have names
             * like
             * save or save(Story story) and getAuthor(). JavaBean method names
             * are
             * generated from concat of link rel's method, name, and return
             * schema
             * (if needed to disambiguate). For example WRML's:
             * 
             * "GET author com/example/Author"
             * 
             * turns into Java's:
             * 
             * "public Author getAuthor()".
             * 
             * 
             * Other methods might look like:
             * 
             * 
             * The LinkRelation specified two (or more) response schema types,
             * "Writer" being one of them. Java doesn't allow overloading the
             * return
             * type of methods, so we need to alter the name too.
             * 
             * public Writer getAuthorAsWriter();
             * 
             * See bad first example above public Story getSelf();
             * 
             * This could be an interesting way of representing Web collections
             * ...
             * 
             * public org.wrml.Collection<Story> getParent();
             * 
             * In cases where the LinkRelation accepts a request schema type
             * that
             * this schema instance implements, the code generation should
             * provide a
             * no-arg version of the method that internally passes this Model
             * (the
             * Link's owner). This would allow the JavaBean interface to have
             * methods like save() which internally "PUT" the Model to its
             * corresponding service and return the origin's version for model
             * syncing.
             * 
             * public Story save();
             * 
             * Is it possible to determine the URI template vars names that are
             * left
             * over after the source schema fills in params with field values?
             * It
             * would be cool if they could be used to generate clean params
             * names
             * for Java methods that need to result in a client-controlled
             * resource
             * name (with initial PUT)
             * 
             * public Story save(String name);
             * 
             * A self destruct button
             * 
             * public void delete();
             * 
             * An "unsafe" action (aka controller) method that was generated
             * from a
             * link rel that uses POST.
             * 
             * public void makeRocketGoNow();
             * 
             * These methods further extend the data available to clients using
             * the
             * JavaBean interface of Models by providing access to linked data
             * via
             * simple get methods. This simplified access to the resource model
             * would be really slick in a dynamic language like groovy talking
             * directly to a REST API via WRML, with object caching in the JVM
             * of
             * course.
             * 
             * At Link.click time, under the covers, these generated methods
             * traverse the linkage using the very same metadata to make the
             * appropriate request to the appropriate service. This is RESTful.
             * 
             * Finally, the role of the service is to be the beginning of both
             * the
             * client-side API and the Service interface. By making the
             * services available for look-up based on schema id, context
             * implementations can choose to register one service per type or
             * services that support several different types. This design allows
             * for
             * the same context interface to work for both client and server
             * side
             * uses of this class. On the client side, the context is perhaps an
             * abstraction over some HTTP client making REST API calls. On the
             * server side, the context may talk to a storage subsystem to CRUD
             * wrml
             * objects (backend connection). Its WRML's equivalent of the Web's
             * uniform interface.
             */

            // Check to see if this link is currently enabled before proceeding
            if (!isEnabled()) {
                // Null represents a disconnected or disabled link
                return null;
            }

            /*
             * TODO: Handle non-WRML model Links to exchange raw input/output
             * streams. Use the MediaType to look up a stream handler that is
             * configured in the Context (like services or possibly just use
             * services)
             * 
             * MediaType requestMediaType = null;
             * 
             * if (requestModel != null) { // TODO: // Determine default media
             * type
             * by looking at the link template and // then the link relation. //
             * Go
             * through each list comparing the requestModel's schema URI to //
             * the
             * wrml media type's schema parameter, or if a non wrml media //
             * type is
             * used then look up a Format using the "raw" media type // (e.g.
             * application/json maps to the json Format) }
             */

            final RuntimeModel model = getModel();
            final Context context = model.getContext();

            if (responseType != null && !isGeneratableResponseType(responseType)) {
                // TODO: Preemptively throw "406 Not Acceptable" exception

                // TODO: Give the alert an ID?
                //Alert alert = context.createModel(Alert.class, null, );

                return null;
            }

            MediaType requestType = null;

            if (requestEntity != null) {

                Transformer<MediaType, Class<?>> mediaTypeToClassTransformer = context.getMediaTypeToClassTransformer();
                requestType = mediaTypeToClassTransformer.bToA(requestEntity.getClass());

                if (requestType != null && !isSupportedRequestType(requestType)) {
                    // TODO: Preemptively throw "415 Unsupported Media Type" exception
                    return null;
                }
            }

            Class<?> responseTypeClass = getMediaTypeClass(responseType);
            Service responseTypeService = context.getService(responseTypeClass);

            // TODO: The last minute hrefParams is a possibly half-baked way to fill
            // in any remaining URI Template params, such as the client-assigned
            // "name" of a first time stored (PUT) resource.
            final URI href = getHref(hrefParams);

            Object responseEntity = null;

            final LinkRelation rel = getLinkRelation();
            final org.wrml.model.communication.http.Method method = rel.getMethod();
            switch (method) {

            // TODO: Handle collections here?

            case GET:
                responseEntity = responseTypeService.get(href, model);
                break;

            case PUT:
                responseEntity = responseTypeService.put(href, requestEntity, model);
                break;

            case DELETE:
                responseEntity = responseTypeService.remove(href, model);
                break;

            case POST:
                // TODO: Create or Execute? 
                // Look at params to decide? Look at endpoint's resource archetype?

                break;

            case HEAD:
                // TODO:
                responseEntity = responseTypeService.get(href, model);
                break;

            case OPTIONS:
                // TODO:
                responseEntity = responseTypeService.get(href, model);
                break;

            default:

                /*
                 * TODO: Throw "405 Method Not Allowed"
                 */

                break;
            }

            // TODO: Fire the post-Click Event

            return responseEntity;
        }

        public void fireHrefChangedEvent() {

        }

        public URI getHref() {
            return _Href;
        }

        public Link getLink() {
            final RuntimeModel model = getModel();
            final Prototype prototype = model.getPrototype();
            ObservableMap<URI, Link> links = prototype.getLinksByRel();
            URI rel = getLinkRelationId();
            return (links != null && links.containsKey(rel)) ? links.get(rel) : null;
        }

        public LinkRelation getLinkRelation() {
            final Model model = getModel();
            Context context = model.getContext();
            Service service = context.getService(LinkRelation.class);
            return (LinkRelation) service.get(getLinkRelationId(), model);
        }

        public URI getLinkRelationId() {
            return _Rel;
        }

        public LinkTemplate getLinkTemplate() {

            return null;

            // TODO

            //        final Model owner = getOwner();
            //
            //        if (!(owner instanceof Document)) {
            //            return null;
            //        }
            //
            //        final Document document = (Document) owner;
            //        final ResourceTemplate resourceTemplate = document.getResourceTemplate();
            //
            //        final ObservableMap<URI, URI> hereToThereLinkRelationIdToLinkTemplateIdMap = resourceTemplate
            //                .getEndPointLinkRelationIdToLinkTemplateIdMap();
            //
            //        final URI linkTemplateId = hereToThereLinkRelationIdToLinkTemplateIdMap.get(getLinkRelationId());
            //
            //        Context context = owner.getContext();
            //        Service service = context.getService(LinkTemplate.class);
            //        return (LinkTemplate) service.get(linkTemplateId, document);

        }

        public RuntimeModel getModel() {
            return RuntimeModel.this;
        }

        public boolean isEnabled() {
            return _Enabled;
        }

        /**
         * 
         * @param hrefParams
         *            The unfinished URI template params for HTTP methods like
         *            PUT
         *            which have path segments that are known only during save
         *            operations
         * @return
         */
        private URI getHref(Map<String, String> hrefParams) {
            // TODO: Get HREF
            return null;
        }

        private boolean isGeneratableResponseType(MediaType responseType) {
            // TODO Auto-generated method stub
            return false;
        }

        private boolean isSupportedRequestType(MediaType requestType) {
            // TODO Auto-generated method stub
            return false;
        }

        /*
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

    private static class StaticModelHandler extends DelegatingInvocationHandler {

        public StaticModelHandler(RuntimeModel delegate) {
            super(delegate);
        }

        @Override
        protected Object subInvoke(Object proxy, Method method, Object[] args) throws Throwable {

            Class<?> declaringClass = method.getDeclaringClass();

            // If the method was declared in Model or one of its ancestors, delegate the call.
            if (declaringClass.isAssignableFrom(Model.class)) {
                return super.subInvoke(proxy, method, args);
            }

            RuntimeModel model = getDyanmicModel();

            Prototype prototype = model.getPrototype();

            String methodKey = getMethodKey(method);
            String methodName = method.getName();

            FieldAccessor fieldAccessor = prototype.getFieldAccessor(methodKey, methodName);
            if (fieldAccessor != null) {
                Object fieldValue = null;
                if (fieldAccessor.getAccessType() == FieldAccessor.AccessType.SET && args != null && args.length > 0) {
                    fieldValue = args[0];
                }
                return fieldAccessor.accessField(model, fieldValue);
            }

            LinkClicker linkClicker = prototype.getLinkClicker(methodKey, methodName, method.getReturnType());
            if (linkClicker != null) {

                Object requestEntity = (args != null && args.length > 0) ? args[0] : null;

                // TODO: Deal with additional params
                @SuppressWarnings("unchecked")
                Map<String, String> hrefArgs = (Map<String, String>) ((args != null && args.length > 1) ? args[1]
                        : null);

                return linkClicker.clickLink(model, requestEntity, hrefArgs);
            }

            // TODO: Support "native" methods by delgating to some other class that is mapped from a Schema ID?

            return null;
        }

        private RuntimeModel getDyanmicModel() {
            return (RuntimeModel) getDelegate();
        }

    }

}
