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

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;

import org.wrml.Model;
import org.wrml.model.Container;
import org.wrml.model.Document;
import org.wrml.model.config.Config;
import org.wrml.model.format.Format;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Schema;
import org.wrml.service.CachingService;
import org.wrml.service.Service;
import org.wrml.service.WebClient;
import org.wrml.util.FieldMap;
import org.wrml.util.MediaType;
import org.wrml.util.observable.DelegatingObservableMap;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;
import org.wrml.util.transformer.CachingTransformer;
import org.wrml.util.transformer.ClassToStringTransformer;
import org.wrml.util.transformer.FormatIdToMediaTypeTransformer;
import org.wrml.util.transformer.MediaTypeToClassTransformer;
import org.wrml.util.transformer.MediaTypeToFormatTransformer;
import org.wrml.util.transformer.MediaTypeToStringTransformer;
import org.wrml.util.transformer.SchemaIdToClassTransformer;
import org.wrml.util.transformer.SchemaIdToFullNameTransformer;
import org.wrml.util.transformer.SchemaIdToMediaTypeTransformer;
import org.wrml.util.transformer.Transformer;
import org.wrml.util.transformer.UriToStringTransformer;

/**
 * 
 * Thread Dimension (Runtime Thread Local)
 * 
 * TODO: Consider making Context have a global static AND a thread local
 * instance, the latter of which takes hierarchically "scoped" precedence
 * over the former.
 * 
 * This will allow for the Context to be read "globally" within a scope that
 * makes logical sense in multithreaded programs, like an web resource
 * server.
 * 
 * That being said, by handing each Model its own Context there is the
 * opportunity to create some very fine-grained scopes using context
 * implementations, meaning that the thread local scope may be different but
 * "parented" by the global context.
 * 
 * Reference Dimension (Design-time Composition)
 * 
 * Parenting means making contexts hierarchical using object references,
 * meaning that a context could have a parent and children. On the server
 * side this might be used relate the thread local context to the parent
 * context, even to the point of the child delegating (cascading) to the
 * parent reference for a default value or behavior that doesn't need to be
 * overridden locally.
 * 
 * This hierarchy can also be a useful abstraction when building
 * applications such as user interfaces, which may have nested contexts for
 * data and associated actions (e.g. plug-ins). In these situations the
 * thread local may not be an effective slot to achieve context locality.
 * 
 * Polymorphism Dimension (Design-time Inheritance)
 * 
 * Context subclasses can alter the default behavior and values by
 * overriding certain methods (and obviously by implementing the absract
 * ones).
 * 
 * LinkRelation and other core types extend Model. This is the dog food
 * that WRML must eat in order to use itself to make itself (bootstrap).
 * 
 * This allows WRML to treat these core type instance "loadings" just
 * like any other application using WRML. Meaning that there is an
 * associated service class for each core type. This association can be
 * formed at program start-up (via config/wiring) which results in a
 * registration by schema within the Context. The mapping can be changed as
 * needed at runtime. The type registration can be repeated for the same
 * service and may also include simple URI wildcard patterns to allow for a
 * single service to handle many schema types. For example an API that
 * stores schema instances in a flexible document store (e.g. mongoDB) could
 * handle requests for many different schema types and CRUD them with ease.
 * 
 * For the Java implementation of WRML to leverage itself (bootstrap), for
 * its own core APIs and tools, all core WRML classes must be stripped down
 * to interfaces, with all of their implementation responsibilities shifted
 * to the framework. This is what it will take to be a truly web resource
 * model-oriented, metadata-driven application framework.
 * 
 * For starters we could use Java interface definitions as placeholders for
 * these core types schema's being accessible via the schema REST API. Hand
 * coding the interface with a mindset of auto-generation, meaning simple
 * repeatable patterns for method signatures and that reference types that
 * can themselves be easily generated and fetched. This will allow these
 * interfaces to later be generated by code that is built into the runtime
 * and compile time environments to allow the eventual web based schemas to
 * be coded against directly.
 * 
 * The impl subclasses could be instantiated using a Proxy class that calls
 * the Model's getFieldValue or Link.click. This drives all
 * implementation concerns down into the framework's Model, its Links,
 * and the supporting Context. With the Context providing hooks for
 * application code.
 * 
 * This Proxy approach may prove to be the final solution or we may decide
 * to auto-generate impl classes via our own code generation.
 */
public class Context extends ClassLoader {

    public static final URI DEFAULT_SCHEMA_API_DOCROOT = URI.create("http://api.schemas.wrml.org/");
    public static final URI DEFAULT_FORMAT_API_DOCROOT = URI.create("http://api.formats.wrml.org/");

    private final ObservableMap<MediaType, Service> _Services;

    private Transformer<MediaType, String> _MediaTypeToStringTransformer;
    private Transformer<MediaType, Class<?>> _MediaTypeToClassTransformer;
    private Transformer<URI, MediaType> _SchemaIdToMediaTypeTransformer;
    private Transformer<URI, Class<?>> _SchemaIdToClassTransformer;
    private Transformer<URI, String> _SchemaIdToFullNameTransformer;
    private Transformer<URI, String> _UriToStringTransformer;
    private Transformer<URI, MediaType> _FormatIdToMediaTypeTransformer;
    private Transformer<MediaType, Format> _MediaTypeToFormatTransformer;

    private final Config _Config;
    private Service _DefaultService;

    private final ObservableMap<URI, HypermediaEngine> _HypermediaEngines;
    private SystemSchemaService _SystemSchemaService;
    private Transformer<Class<?>, String> _ClassToStringTransformer;

    public Context(final Config config) {
        this(config, getSystemClassLoader());
    }

    public Context(final Config config, final ClassLoader parent) {
        super(parent);

        _Config = config;
        _HypermediaEngines = Observables.observableMap(new HashMap<URI, HypermediaEngine>());
        _Services = Observables.observableMap(new HashMap<MediaType, Service>());

        initSystemTransformers();

        initTransformers();

        initSystemServices();

        initServices();

        bootstrap();
        
        // TODO: Do something interesting with the config...like loading WRML REST API definitions
    }

    private void bootstrap() {

        // Get the media type: application/wrml; schema="http://.../org/wrml/model/schema/Schema"
        final MediaType schemaMediaType = getMediaTypeToClassTransformer().bToA(Schema.class);

        /*
         * Get the Context-configured Service that is able to "service" requests
         * for this media type.
         * 
         * In this instance we are testing a special case where the service
         * is responsible for facilitating interactions with Schema resources.
         */
        final Service schemaService = getService(schemaMediaType);

        /*
         * Like all WRML Services, the Schema Service is a Map that is keyed on
         * URI. If we want to get a Schema model (value) from the Schema Service
         * (Map) then we need to know the Schema's id, its URI (key).
         */

        Transformer<URI, MediaType> schemaIdToMediaTypeTransformer = getSchemaIdToMediaTypeTransformer();

        // Get the media type: application/wrml; schema="http://.../org/wrml/model/schema/Field"
        final MediaType fieldMediaType = getMediaTypeToClassTransformer().bToA(Field.class);
        final URI fieldSchemaId = schemaIdToMediaTypeTransformer.bToA(fieldMediaType);

        // Use the Service's overloaded get method to request the Field Schema.
        final Model dynamicFieldSchemaModel = (Model) schemaService.get(fieldSchemaId, null, schemaMediaType, null);


        /*
         * Using the model instance's dynamic (Map-like) Java API we can get
         * field values (Objects) names based on their names (Strings).
         * 
         * Generally speaking accessing the fields of a model dynamically is
         * appropriate in cases where the static type information is not
         * necessary.
         */
        
        System.out.println("Dynamic name: " + dynamicFieldSchemaModel.getFieldValue("name"));
        System.out.println("Dynamic id: " + dynamicFieldSchemaModel.getFieldValue("id"));
        System.out.println("Dynamic description: " + dynamicFieldSchemaModel.getFieldValue("description"));
        System.out.println("Dynamic baseSchemaIds: " + dynamicFieldSchemaModel.getFieldValue("baseSchemaIds"));
        //System.out.println("Dynamic fields: " + dynamicMetaSchemaModel.getFieldValue("fields"));
        
        /*
         * In the Java implementation of WRML, models have two APIs.
         * 
         * First, as demonstrated above, there is the inner more "dynamic"
         * interface with the getFieldValue, setFieldValue, and clickLink
         * methods.
         * 
         * Second, there is a reflection proxy-based static interface that
         * allows Java programs to address WRML models by their auto-generated
         * Java interface. The interface reference that is returned is a thin
         * "facade" over the model's dynamic interface, which ensures that
         * behavior will be consistent between the two APIs.
         * 
         * In this example, we have the model that represents the schema for
         * Schemas (like WRML's Class<Class<?>>).
         */
        final Schema staticFieldSchemaModel = (Schema) dynamicFieldSchemaModel.getStaticInterface();
        
        System.out.println("Static name: " + staticFieldSchemaModel.getName());
        System.out.println("Static id: " + staticFieldSchemaModel.getId());
        System.out.println("Static description: " + staticFieldSchemaModel.getDescription());
        System.out.println("Static baseSchemaIds: " + staticFieldSchemaModel.getBaseSchemaIds());
        
        /*
         * If we want to get the Schema that represents the form of all WRML
         * models, then we are looking for the MetaSchema, which has an id value
         * of "http://.../org/wrml/model/schema/Schema", which we know is part
         * of the media type that we used to get the service. So we already have
         * what we need to get the URI in the media type but we can use a
         * pre-existing transformer to handle this conversion for us.
         * 
         * Using the right transformer, we can get the URI id of a given schema
         * by starting with a WRML media type (with a schema param).
         */
        
        final URI schemaSchemaId = schemaIdToMediaTypeTransformer.bToA(schemaMediaType);

        // Use the Service's overloaded get method to request the MetaSchema.
        final Model dynamicMetaSchemaModel = (Model) schemaService.get(schemaSchemaId, null, schemaMediaType, null);


        System.out.println("Dynamic name: " + dynamicMetaSchemaModel.getFieldValue("name"));
        System.out.println("Dynamic id: " + dynamicMetaSchemaModel.getFieldValue("id"));
        System.out.println("Dynamic description: " + dynamicMetaSchemaModel.getFieldValue("description"));
        System.out.println("Dynamic baseSchemaIds: " + dynamicMetaSchemaModel.getFieldValue("baseSchemaIds"));
        //System.out.println("Dynamic fields: " + dynamicMetaSchemaModel.getFieldValue("fields"));

        final Schema staticMetaSchemaModel = (Schema) dynamicMetaSchemaModel.getStaticInterface();

        System.out.println("Static name: " + staticMetaSchemaModel.getName());
        System.out.println("Static id: " + staticMetaSchemaModel.getId());
        System.out.println("Static description: " + staticMetaSchemaModel.getDescription());
        System.out.println("Static baseSchemaIds: " + staticMetaSchemaModel.getBaseSchemaIds());
        //System.out.println("Static fields: " + staticModel.getFields());

    }

    public final void fetchAllDocuments(Container<? extends Document> documents, List<? extends Document> allDocuments) {
        // TODO: Don't hardcode the default max 
        fetchAllDocuments(documents, allDocuments, 1000);
    }

    public final void fetchAllDocuments(Container<? extends Document> documents, List<? extends Document> allDocuments,
            int limit) {
        // TODO: Page through container filling allDocuments

    }

    public final Transformer<Class<?>, String> getClassToStringTransformer() {
        return _ClassToStringTransformer;
    }

    public Config getConfig() {
        return _Config;
    }

    public Service getDefaultService() {
        return _DefaultService;
    }

    public Transformer<URI, MediaType> getFormatIdToMediaTypeTransformer() {
        return _FormatIdToMediaTypeTransformer;
    }

    /*
     * TODO: Consider moving all of this voodoo to the SchemaService interface.
     * Keep this class more universally useful to all apss - not just the
     * bootstrapping of the WRML runtime "app".
     */

    public final HypermediaEngine getHypermediaEngine(URI apiId) {
        if (!_HypermediaEngines.containsKey(apiId)) {
            final HypermediaEngine hypermediaEngine = new HypermediaEngine(this, apiId);
            _HypermediaEngines.put(apiId, hypermediaEngine);
        }

        return _HypermediaEngines.get(apiId);
    }

    public Transformer<MediaType, Class<?>> getMediaTypeToClassTransformer() {
        return _MediaTypeToClassTransformer;
    }

    public Transformer<MediaType, Format> getMediaTypeToFormatTransformer() {
        return _MediaTypeToFormatTransformer;
    }

    public Transformer<MediaType, String> getMediaTypeToStringTransformer() {
        return _MediaTypeToStringTransformer;
    }

    public final URI getMetaSchemaId() {
        return getSchemaIdToClassTransformer().bToA(Schema.class);
    }

    public final Prototype getPrototype(MediaType mediaType) {
        return _SystemSchemaService.getPrototype(mediaType);
    }

    public final Schema getSchema(URI schemaId) {
        final MediaType schemaMediaType = getMediaTypeToClassTransformer().bToA(Schema.class);
        final Service schemaService = getService(schemaMediaType);
        return (Schema) ((Model) schemaService.get(schemaId, null, schemaMediaType, null)).getStaticInterface();
    }

    /*
     * private Prototype getMetaPrototype() {
     * 
     * if (_MetaPrototype == null) {
     * _MetaPrototype = new Prototype(this, getMetaSchemaId(), true);
     * }
     * 
     * return _MetaPrototype;
     * }
     */

    public final Transformer<URI, Class<?>> getSchemaIdToClassTransformer() {
        return _SchemaIdToClassTransformer;
    }

    public final Transformer<URI, String> getSchemaIdToFullNameTransformer() {
        return _SchemaIdToFullNameTransformer;
    }

    @SuppressWarnings("unchecked")
    public <T> void mapFieldsByFieldName(final Map<T, Field> fieldMap, final List<Field> fieldList,
            final String fieldName) {
        for (Field schemaField : fieldList) {
            fieldMap.put((T) schemaField.getFieldValue(fieldName), schemaField);
        }
    }

    public MediaType normalize(MediaType other) {
        if (!other.isWrml()) {
            return other;
        }

        ObservableMap<String, String> parameters = other.getParameters();

        if (parameters != null && parameters.containsKey(MediaType.PARAMETER_NAME_FORMAT)) {

            SortedMap<String, String> normalizedParameters = new TreeMap<String, String>(parameters);
            normalizedParameters.remove(MediaType.PARAMETER_NAME_FORMAT);

            String normalizedMediaTypeString = MediaType.createWrmlMediaTypeString(
                    parameters.get(MediaType.PARAMETER_NAME_SCHEMA), normalizedParameters);

            return getMediaTypeToStringTransformer().bToA(normalizedMediaTypeString);
        }

        return other;

    }

    public boolean schematicallyEquals(MediaType a, MediaType b) {
        return normalize(a).equals(normalize(b));
    }

    /*
     * public final StringTransformer<?>
     * getStringTransformer(TextSyntaxConstraint textSyntaxConstraint) {
     * // TODO: Implement this mapping with configuration-based mapping falling
     * back to code on demand
     * return null;
     * 
     * }
     */

    public final Transformer<URI, MediaType> getSchemaIdToMediaTypeTransformer() {
        return _SchemaIdToMediaTypeTransformer;
    }

    public final Service getService(Class<?> clazz) {
        final URI schemaId = getSchemaIdToClassTransformer().bToA(clazz);
        return getService(schemaId);
    }

    public final Service getService(MediaType mediaType) {

        /*
         * TODO: Allow for more complex mapping of schemas to services
         * For example, allow for a base schema to be registered for
         * sub-services or a uri pattern match.
         */

        final Service service = _Services.get(mediaType);

        if (service != null) {
            return service;
        }

        // By default, the default service is the WWW.
        return getDefaultService();
    }

    public final Service getService(String className) {
        final URI schemaId = getSchemaIdToFullNameTransformer().bToA(className);
        return getService(schemaId);
    }

    public final Service getService(URI schemaId) {
        return getService(getSchemaIdToMediaTypeTransformer().aToB(schemaId));
    }

    public final ObservableMap<MediaType, Service> getServices() {
        return _Services;
    }

    public final Transformer<URI, String> getUriToStringTransformer() {
        return _UriToStringTransformer;
    }

    public final CachingService instantiateCachingService(Service originService) {
        final Map<URI, Object> rawModelMap = new HashMap<URI, Object>();
        final ObservableMap<URI, Object> observableModelCache = new DelegatingObservableMap<URI, Object>(rawModelMap);
        return new CachingService(this, originService, observableModelCache);
    }

    public final Model instantiateModel(Class<?> schemaClass) {
        return instantiateModel(getMediaTypeToClassTransformer().bToA(schemaClass));
    }

    public final Model instantiateModel(URI schemaId) {
        return instantiateModel(getSchemaIdToMediaTypeTransformer().aToB(schemaId));
    }

    public final Model instantiateModel(MediaType mediaType) {

        final Map<String, Object> fieldBackingMap = new TreeMap<String, Object>();
        final Map<URI, HyperLink> linkBackingMap = new HashMap<URI, HyperLink>();

        final ModelFieldMap modelFieldMap = new ModelFieldMap(fieldBackingMap);

        // TODO: Handle server-side link clicking
        final List<URI> embeddedLinkRelationIds = null;

        final Model model = instantiateModel(mediaType, embeddedLinkRelationIds, modelFieldMap, linkBackingMap);
        modelFieldMap.setModel(model);

        return model;
    }

    public final Model instantiateModel(MediaType mediaType, List<URI> embeddedLinkRelationIds, FieldMap fieldMap,
            Map<URI, HyperLink> linkMap) {

        final RuntimeModel model = new RuntimeModel(this, mediaType, embeddedLinkRelationIds, fieldMap, linkMap);

        return model;
    }

    public final void setClassToStringTransformer(Transformer<Class<?>, String> classToStringTransformer) {
        _ClassToStringTransformer = classToStringTransformer;
    }

    public final void setDefaultService(Service defaultService) {
        _DefaultService = defaultService;
    }

    public final void setFormatIdToMediaTypeTransformer(Transformer<URI, MediaType> formatIdToMediaTypeTransformer) {
        _FormatIdToMediaTypeTransformer = formatIdToMediaTypeTransformer;
    }

    public final void setMediaTypeToClassTransformer(Transformer<MediaType, Class<?>> mediaTypeToClassTransformer) {
        _MediaTypeToClassTransformer = mediaTypeToClassTransformer;
    }

    public final void setMediaTypeToFormatTransformer(Transformer<MediaType, Format> mediaTypeToFormatTransformer) {
        _MediaTypeToFormatTransformer = mediaTypeToFormatTransformer;
    }

    public final void setMediaTypeToStringTransformer(Transformer<MediaType, String> mediaTypeToStringTransformer) {
        _MediaTypeToStringTransformer = mediaTypeToStringTransformer;
    }

    public final void setSchemaIdToClassTransformer(Transformer<URI, Class<?>> schemaIdToClassTransformer) {
        _SchemaIdToClassTransformer = schemaIdToClassTransformer;
    }

    public final void setSchemaIdToFullNameTransformer(Transformer<URI, String> schemaIdToFullNameTransformer) {
        _SchemaIdToFullNameTransformer = schemaIdToFullNameTransformer;
    }

    public final void setSchemaIdToMediaTypeTransformer(Transformer<URI, MediaType> schemaIdToMediaTypeTransformer) {
        _SchemaIdToMediaTypeTransformer = schemaIdToMediaTypeTransformer;
    }

    public final void setSchemaService(Service schemaService) {

        if (!(schemaService instanceof CachingService)) {
            schemaService = instantiateCachingService(schemaService);
        }
        final MediaType schemaMediaType = getMediaTypeToClassTransformer().bToA(Schema.class);
        _Services.put(schemaMediaType, schemaService);

    }

    public final void setUriToStringTransformer(Transformer<URI, String> uriToStringTransformer) {
        _UriToStringTransformer = uriToStringTransformer;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // TODO Auto-generated method stub
        return super.findClass(name);
    }

    protected void initServices() {
        // For subclasses
    }

    protected void initTransformers() {
        // For subclasses
    }

    private void initSystemServices() {
        final Service www = new WebClient(this);
        final Service defaultService = instantiateCachingService(www);
        setDefaultService(defaultService);

        _SystemSchemaService = new SystemSchemaService(this, www);
        setSchemaService(_SystemSchemaService);

    }

    private void initSystemTransformers() {

        final Transformer<URI, String> uriToStringTransformer = CachingTransformer.create(new UriToStringTransformer(),
                new WeakHashMap<URI, String>(), new WeakHashMap<String, URI>());
        setUriToStringTransformer(uriToStringTransformer);

        final Transformer<Class<?>, String> classToStringTransformer = CachingTransformer.create(
                new ClassToStringTransformer(), new WeakHashMap<Class<?>, String>(),
                new WeakHashMap<String, Class<?>>());
        setClassToStringTransformer(classToStringTransformer);

        setMediaTypeToStringTransformer(CachingTransformer.create(new MediaTypeToStringTransformer()));
        setMediaTypeToClassTransformer(CachingTransformer.create(new MediaTypeToClassTransformer(this)));
        setSchemaIdToMediaTypeTransformer(CachingTransformer.create(new SchemaIdToMediaTypeTransformer(this)));
        setSchemaIdToFullNameTransformer(CachingTransformer.create(new SchemaIdToFullNameTransformer(
                DEFAULT_SCHEMA_API_DOCROOT)));
        setFormatIdToMediaTypeTransformer(CachingTransformer.create(new FormatIdToMediaTypeTransformer(this,
                DEFAULT_FORMAT_API_DOCROOT)));
        setSchemaIdToClassTransformer(CachingTransformer.create(new SchemaIdToClassTransformer(this)));
        setMediaTypeToFormatTransformer(CachingTransformer.create(new MediaTypeToFormatTransformer(this)));
    }

}
