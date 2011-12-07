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

import org.wrml.model.schema.Prototype;
import org.wrml.model.schema.PrototypeField;
import org.wrml.model.schema.Schema;
import org.wrml.util.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
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
 * <p>
 * API Management solution vendor lock-in Lack consistent (standard) interface
 * features to allow client control over details such as collection pagination,
 * partial response, object composition, and expansion of hypermedia links into
 * an embedded object. Leveraging their proprietary (non-standard) API
 * enhancement features may lead to vendor lock-in
 * </p>
 * 
 * 
 */
public class RuntimeModel extends Identifiable<URI> implements Model {

    private static final long serialVersionUID = -7696720779481780624L;

    // TODO: Make this an enum?
    public static final String ID_FIELD_NAME = "id";
    public static final String READ_ONLY_FIELD_NAME = "readOnly";

    private final URI _SchemaId;
    private final List<URI> _EmbeddedLinkRelationIds;

    private transient Context _Context;

    private ObservableMap<String, Object> _FieldMap;
    private ObservableMap<URI, Link> _LinkMap;

    private transient List<ModelEventListener> _EventListeners;
    private transient Map<String, List<FieldEventListener<?>>> _FieldEventListeners;

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

    public void addFieldEventListener(String fieldName, FieldEventListener<?> listener) {
        if (_FieldEventListeners == null) {
            _FieldEventListeners = new HashMap<String, List<FieldEventListener<?>>>();
        }

        List<FieldEventListener<?>> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        if (fieldEventListenerList == null) {
            fieldEventListenerList = new CopyOnWriteArrayList<FieldEventListener<?>>();
            _FieldEventListeners.put(fieldName, fieldEventListenerList);
        }

        fieldEventListenerList.add(listener);
    }

    @Override
    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    public Context getContext() {
        return _Context;
    }

    public List<URI> getEmbeddedLinkRelationIds() {
        return _EmbeddedLinkRelationIds;
    }

    public Object getFieldValue(String fieldName) {
        return isFieldValueSet(fieldName) ? _FieldMap.get(fieldName) : null;
    }

    @Override
    public URI getId() {
        return (URI) getFieldValue(ID_FIELD_NAME);
    }

    public Link getLink(URI linkRelationId) {

        Link link;

        if (_LinkMap == null) {
            initLinkMap();
        }
        else {
            link = _LinkMap.get(linkRelationId);
            if (link != null) {
                return link;
            }
        }

        link = new Link(this, linkRelationId);
        _LinkMap.put(linkRelationId, link);

        return link;
    }

    public Prototype getPrototype() {
        return getContext().getPrototype(getSchemaId(), this);
    }

    public Schema getSchema() {
        return getContext().getSchema(getSchemaId(), this);
    }

    public URI getSchemaId() {
        return _SchemaId;
    }

    @Override
    public final int hashCode() {
        return super.hashCode();
    }

    public boolean isFieldValueSet(String fieldName) {
        return (_FieldMap != null) && _FieldMap.containsKey(fieldName);
    }

    public boolean isReadOnly() {
        return ((Boolean) getFieldValue(READ_ONLY_FIELD_NAME)).booleanValue();
    }

    public void removeEventListener(ModelEventListener listener) {
        if (_EventListeners == null) {
            return;
        }

        _EventListeners.remove(listener);
    }

    public void removeFieldEventListener(String fieldName, FieldEventListener<?> listener) {
        if (_FieldEventListeners == null) {
            return;
        }

        final List<FieldEventListener<?>> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        if (fieldEventListenerList == null) {
            return;
        }

        fieldEventListenerList.remove(listener);
    }

    public void setAllFieldsToDefaultValue() {

        final Prototype prototype = getPrototype();
        final Map<String, PrototypeField<?>> prototypeFields = prototype.getPrototypeFields();
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

    public void setFieldToDefaultValue(String fieldName) {

        final Prototype prototype = getPrototype();
        final Map<String, PrototypeField<?>> prototypeFields = prototype.getPrototypeFields();

        if ((prototypeFields != null) && prototypeFields.containsKey(fieldName)) {
            setFieldValue(fieldName, prototypeFields.get(fieldName).getDefaultValue());
        }
    }

    public Object setFieldValue(String fieldName, Object fieldValue) {

        if (_FieldMap == null) {
            initFieldMap();
        }

        return _FieldMap.put(fieldName, fieldValue);
    }

    @Override
    public URI setId(URI id) {
        return (URI) setFieldValue(ID_FIELD_NAME, id);
    }

    protected ObservableMap<String, Object> getFieldMap() {
        return _FieldMap;
    }

    protected ObservableMap<URI, Link> getLinkMap() {
        return _LinkMap;
    }

    private void fireConstraintViolated(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener<?>> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (final FieldEventListener<?> fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }

            fieldEventListener.constraintViolated(event);
        }
    }

    private void fireValueChanged(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener<?>> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (final FieldEventListener<?> fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }

            fieldEventListener.valueChanged(event);
        }

    }

    private void fireValueInitialized(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener<?>> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (final FieldEventListener<?> fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }

            fieldEventListener.valueInitialized(event);
        }
    }

    /**
     * Called to initialize the Model.
     */
    private void init() {
        setAllFieldsToDefaultValue();
    }

    private void initFieldMap() {
        _FieldMap = Observables.observableMap(new HashMap<String, Object>());
        _FieldMapEventListener = new FieldMapEventListener();
        _FieldMap.addEventListener(_FieldMapEventListener);
    }

    private void initLinkMap() {
        _LinkMap = Observables.observableMap(new HashMap<URI, Link>());
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
