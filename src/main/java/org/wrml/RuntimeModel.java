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

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.wrml.model.Document;
import org.wrml.model.LinkTemplate;
import org.wrml.model.relation.LinkRelation;
import org.wrml.model.runtime.Prototype;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Schema;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.util.DelegatingInvocationHandler;
import org.wrml.util.MapEvent;
import org.wrml.util.MapEventListener;
import org.wrml.util.ObservableMap;
import org.wrml.util.Observables;

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
public class RuntimeModel implements Model {

    private Model _StaticInterface;

    private static final long serialVersionUID = -7696720779481780624L;

    private final URI _SchemaId;
    private final List<URI> _EmbeddedLinkRelationIds;

    private transient Context _Context;

    private ObservableMap<String, Object> _FieldMap;
    private ObservableMap<URI, LinkInstance> _LinkMap;

    private transient List<ModelEventListener> _EventListeners;
    private transient Map<String, List<FieldEventListener>> _FieldEventListeners;

    private transient FieldMapEventListener _FieldMapEventListener;

    public RuntimeModel(URI schemaId, Context context) {
        this(schemaId, context, null);
    }

    public RuntimeModel(URI schemaId, Context context, List<URI> embeddedLinkRelationIds) {
        _SchemaId = schemaId;
        _Context = context;
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

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final Context getContext() {
        return _Context;
    }

    public final List<URI> getEmbeddedLinkRelationIds() {
        return _EmbeddedLinkRelationIds;
    }

    public final Object getFieldValue(String fieldName) {
        return isFieldValueSet(fieldName) ? _FieldMap.get(fieldName) : null;
    }

    private LinkInstance getLinkInstance(URI linkRelationId) {

        LinkInstance link;

        if (_LinkMap == null) {
            initLinkMap();
        }
        else {
            link = _LinkMap.get(linkRelationId);
            if (link != null) {
                return link;
            }
        }

        link = new LinkInstance(this, linkRelationId);
        _LinkMap.put(linkRelationId, link);

        return link;
    }

    public Service getMyOriginService() {

        // Go all the way to the origin to force a re-GET of "self"
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

        // TODO: Is there a better way to avoid stack overflow here?
        if (this instanceof Prototype || this instanceof Field) {
            return null;
        }

        return getContext().getPrototype(getSchemaId());
    }

    public final Schema getSchema() {
        return getContext().getSchema(getSchemaId());
    }

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

    public final void setAllFieldsToDefaultValue() {

        final Prototype prototype = getPrototype();
        if (prototype == null) {
            return;
        }

        final Map<String, Field> prototypeFields = prototype.getFields();
        if (prototypeFields != null) {
            final Set<String> fieldNames = prototypeFields.keySet();
            for (final String fieldName : fieldNames) {
                setFieldToDefaultValue(fieldName);
            }
        }
    }

    // TODO: Add public clickLink methods to the Model interface

    /*
     * public Object clickLink(URI linkRelationId) { return
     * clickLink(getLink(linkRelationId), null, null); }
     * 
     * public Object clickLink(URI linkRelationId, URI requestSchemaId) { return
     * clickLink(getLink(linkRelationId), requestSchemaId, null); }
     * 
     * public Object clickLink(URI linkRelationId, URI requestSchemaId, URI
     * requestFormatId) { return clickLink(getLink(linkRelationId),
     * requestSchemaId, requestFormatId); }
     * 
     * public Object clickLink(Link link, URI requestSchemaId, URI
     * requestFormatId) {
     * 
     * // TODO: // If the requestSchemaId != null { // Need to default null
     * value of the format ID to some API-specified // default value. Look up
     * this value using the LinkTemplate? // }
     * 
     * return clickLink(link, getContext().getWrmlMediaType(requestSchemaId,
     * requestFormatId)); }
     * 
     * public Object clickLink(URI linkRelationId, MediaType requestMediaType) {
     * return clickLink(getLink(linkRelationId), requestMediaType); }
     * 
     * protected Object clickLink(Link link) { return link.click(); }
     * 
     * protected Object clickLink(Link link, Object requestModel, MediaType
     * requestMediaType) { return link.click(requestModel, requestMediaType); }
     */

    public final void setFieldToDefaultValue(String fieldName) {

        final Prototype prototype = getPrototype();
        final Map<String, Field> prototypeFields = prototype.getFields();

        if ((prototypeFields != null) && prototypeFields.containsKey(fieldName)) {
            setFieldValue(fieldName, prototypeFields.get(fieldName).getDefaultValue());
        }
    }

    public final Object setFieldValue(String fieldName, Object fieldValue) {

        if (_FieldMap == null) {
            initFieldMap();
        }

        return _FieldMap.put(fieldName, fieldValue);
    }

    protected final boolean getBooleanFieldValue(String fieldName) {
        Boolean booleanFieldValue = (Boolean) getFieldValue(fieldName);
        return (booleanFieldValue != null) ? booleanFieldValue.booleanValue() : false;
    }

    protected final ObservableMap<String, Object> getFieldMap() {
        return _FieldMap;
    }

    protected final ObservableMap<URI, LinkInstance> getLinkMap() {
        return _LinkMap;
    }

    /**
     * Called to initialize the Model.
     */
    protected void init() {
        setAllFieldsToDefaultValue();
    }

    protected final boolean setBooleanFieldValue(String fieldName, boolean fieldValue) {
        boolean oldValue = getBooleanFieldValue(fieldName);
        if (fieldValue != oldValue) {
            setFieldValue(fieldName, (fieldValue ? Boolean.TRUE : Boolean.FALSE));
        }

        return oldValue;

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

            final String methodName = method.getName();

            // TODO: This can all be optimized

            String fieldName = null;
            Object fieldValue = null;

            if (methodName.startsWith("get")) {
                fieldName = methodName.substring(3);
            }
            else
                if (methodName.startsWith("is")) {
                    fieldName = methodName.substring(2);
                }

            if (fieldName != null) {
                fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);

                // TODO: Refactor this to Handle booleans (see how ReadOnly is done in RuntimeModel)

                fieldValue = getDyanmicModel().getFieldValue(fieldName);
                if (fieldValue != null) {
                    return fieldValue;
                }

                // TODO: Handle Links that look like "get" methods
            }
            else
                if (methodName.startsWith("set")) {
                    fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                    fieldValue = getDyanmicModel().setFieldValue(fieldName, args[0]);
                    if (fieldValue != null) {
                        return fieldValue;
                    }

                    // TODO: Handle Links that look like "set" methods
                }

            return null;
        }

        private RuntimeModel getDyanmicModel() {
            return (RuntimeModel) getDelegate();
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

        private final Model _Model;
        private final URI _LinkRelationId;
        private URI _Href;
        private boolean _Enabled;

        public LinkInstance(Model owner, URI linkRelationId) {
            _Model = owner;
            _LinkRelationId = linkRelationId;
        }

        public void addEventListener(LinkEventListener listener) {
            // TODO Add an event listener
        }

        public Model click() {
            return click(null);
        }

        public Model click(URI responseModelSchemaId) {
            return click(responseModelSchemaId, null);
        }

        public Model click(URI responseModelSchemaId, Model requestModel) {
            return click(responseModelSchemaId, requestModel, null);
        }

        // TODO: All Service methods need to take a URI responseModelSchemaId param, it is needed to determine the requested model response type 
        public Model click(URI responseModelSchemaId, Model requestModel, List<String> hrefParams) {

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
                // TODO: Throw an exception of some sort
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

            final Model owner = getOwner();
            final Context context = owner.getContext();
            final LinkRelation rel = getLinkRelation();

            if (responseModelSchemaId != null && !isGeneratableResponseSchema(rel, responseModelSchemaId)) {
                // TODO: Preemptively throw "406 Not Acceptable" exception

                // TODO: Give the alert an ID?
                //Alert alert = context.createModel(Alert.class, null, );

                return null;
            }

            URI requestModelSchemaId = null;

            if (requestModel != null) {
                requestModelSchemaId = requestModel.getSchemaId();

                if (requestModelSchemaId != null && !isSupportedRequestSchema(rel, requestModelSchemaId)) {
                    // TODO: Preemptively throw "415 Unsupported Media Type" exception
                    return null;
                }
            }

            // TODO: The last minute hrefParams is a possibly half-baked way to fill
            // in any remaining URI Template params, such as the client-assigned
            // "name" of a first time stored (PUT) resource.
            final URI href = getHref(hrefParams);
            final org.wrml.model.communication.http.Method method = rel.getMethod();

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
             * Service responseModelService =
             * owner.getContext().getService(owner,
             * responseModelSchemaId);
             * responseModel = responseModelService.get(href,
             * responseModelSchemaId);
             * break;
             * 
             * case PUT:
             * Service requestModelService =
             * owner.getContext().getService(owner,
             * requestModelSchemaId);
             * responseModel = requestModelService.save(href, requestModel);
             * break;
             * 
             * default:
             * // TODO: Preemptively throw "405 Method Not Allowed"
             * 
             * // OPTIONS("OPTIONS", false, true), GET("GET", true, true),
             * // HEAD("HEAD", true, true), POST("POST", false, false), PUT(
             * "PUT",
             * // false, true), DELETE("DELETE", false, true), TRACE("TRACE",
             * // false, true), CONNECT("CONNECT", false, false);
             * 
             * }
             */

            // TODO

            // TODO: Fire the post-Click Event

            return responseModel;

        }

        public void fireHrefChangedEvent() {

        }

        public Schema getDeclaredSchema() {
            // TODO Auto-generated method stub
            return null;
        }

        public URI getDeclaredSchemaId() {
            // TODO Auto-generated method stub
            return null;
        }

        public URI getHref() {
            return _Href;
        }

        public LinkRelation getLinkRelation() {
            final Model owner = getOwner();
            Context context = owner.getContext();
            Service service = context.getService(LinkRelation.class);
            return (LinkRelation) service.get(getLinkRelationId(), owner);
        }

        /*
         * TODO: Should click (or alternative method) support clicking on links
         * that
         * deal with non-wrml media type data (e.g. JPEGs)? I am inclined to
         * think
         * that this needs to be supported somehow. If WRML is going to take
         * over
         * the web server it shouldn't be limited to serving "schemafied" data.
         * 
         * Consider refactoring to be more generic.
         */

        public URI getLinkRelationId() {
            return _LinkRelationId;
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

        public Model getModel() {
            return _Model;
        }

        public Document getOwner() {
            // TODO Auto-generated method stub
            return null;
        }

        public URI getOwnerId() {
            // TODO Auto-generated method stub
            return null;
        }

        public String getStateExpression() {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean isEnabled() {
            return _Enabled;
        }

        public void removeEventListener(LinkEventListener listener) {
            // TODO: Remove the event listener
        }

        public String setStateExpression(String stateExpression) {
            // TODO Auto-generated method stub
            return null;
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
        private URI getHref(List<String> hrefParams) {
            // TODO: Get HREF
            return null;
        }

        private boolean isGeneratableResponseSchema(LinkRelation rel, URI responseModelSchemaId) {
            // TODO Auto-generated method stub
            return false;
        }

        private boolean isSupportedRequestSchema(LinkRelation rel, URI requestModelSchemaId) {
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

    public void addLinkEventListener(URI linkRelationId, LinkEventListener listener) {
        // TODO Auto-generated method stub

    }

    public void removeLinkEventListener(URI linkRelationId, LinkEventListener listener) {
        // TODO Auto-generated method stub

    }

    public void extend(boolean atomic, Model modelToExtend, Model... additionalModelsToExtend) {

        // TODO: "Pause" listeners if atomic is true

        // TODO: Set all fields (copy from extends)

        // TODO: Create and fire some sort of refresh event

    }

    public void die() {
        // TODO: Fire an event about vanishing

        // TODO: Unregister all listeners

        // TODO: Clear caches or do caches auto-clear from an 
        // vanish event notification?

    }

}
