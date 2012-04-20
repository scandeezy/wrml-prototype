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

package org.wrml.core.runtime;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.wrml.core.Model;
import org.wrml.core.io.JsonParserModelGraphReader;
import org.wrml.core.io.ModelGraphReader;
import org.wrml.core.model.Container;
import org.wrml.core.model.Document;
import org.wrml.core.model.config.Config;
import org.wrml.core.model.schema.Field;
import org.wrml.core.model.schema.Schema;
import org.wrml.core.runtime.bootstrap.ServiceConfig;
import org.wrml.core.runtime.system.Prototype;
import org.wrml.core.runtime.system.SystemSchemaService;
import org.wrml.core.runtime.system.transformer.SystemTransformers;
import org.wrml.core.service.CachingService;
import org.wrml.core.service.Service;
import org.wrml.core.transformer.CachingTransformer;
import org.wrml.core.transformer.Transformer;
import org.wrml.core.transformer.Transformers;
import org.wrml.core.transformer.tostring.MediaTypeToStringTransformer;
import org.wrml.core.transformer.tostring.UriToStringTransformer;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.util.observable.Observables;
import org.wrml.core.www.MediaType;
import org.wrml.core.www.WebClient;

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

    private final ServiceConfig _Config;

    private final ModelHeap _ModelHeap;

    /*
     * TODO: I would like to replace this simple map with a regex mapping that
     * allows media types to be specified as regex patterns, so that a single
     * call can be made to map a set of media types to a single Service.
     */
    private final ObservableMap<MediaType, Service> _Services;

    private final SystemSchemaService _SystemSchemaService;

    private Service _DefaultService;

    private final ObservableMap<URI, HypermediaEngine> _HypermediaEngines;

    private final Transformers<String> _StringTransformers;
    private final SystemTransformers _SystemTransformers;
    private final TypeSystem _TypeSystem;

    private final Service _WWW;

    public Context(final ServiceConfig config) {
        this(config, getSystemClassLoader());
    }

    public Context(final ServiceConfig config, final ClassLoader parent) {
        super(parent);

        _Config = config;

        _ModelHeap = new ModelHeap(this);

        _HypermediaEngines = Observables.observableMap(new HashMap<URI, HypermediaEngine>());
        _Services = Observables.observableMap(new HashMap<MediaType, Service>());

        _StringTransformers = new Transformers<String>(this);

        final Transformer<URI, String> uriToStringTransformer = CachingTransformer.create(new UriToStringTransformer(),
                new WeakHashMap<URI, String>(), new WeakHashMap<String, URI>());

        _StringTransformers.setTransformer(URI.class, uriToStringTransformer);

        final Transformer<MediaType, String> mediaTypeToStringTransformer = CachingTransformer.create(
                new MediaTypeToStringTransformer(), new WeakHashMap<MediaType, String>(),
                new WeakHashMap<String, MediaType>());

        _StringTransformers.setTransformer(MediaType.class, mediaTypeToStringTransformer);

        _SystemTransformers = new SystemTransformers(this);
        _TypeSystem = new TypeSystem(this);

        _WWW = new WebClient(this);
        setDefaultService(_WWW);

        _SystemSchemaService = new SystemSchemaService(this, _WWW);
        setSchemaService(_SystemSchemaService);

        initTransformers();

        initServices();

        //bootstrap();

        // TODO: Do something interesting with the config...like loading WRML REST API definitions
    }

    public ModelGraphReader createModelReader(MediaType mediaType, InputStream inputStream) throws Exception {

        // TODO: Do not hardcode this, determine this based on the Media Type (and configured readers)

        final JsonParserModelGraphReader reader = new JsonParserModelGraphReader();
        reader.open(inputStream);
        return reader;
    }

    public final void fetchAllDocuments(Container<? extends Document> documents, List<? extends Document> allDocuments) {
        // TODO: Don't hardcode the default max 
        fetchAllDocuments(documents, allDocuments, 1000);
    }

    public final void fetchAllDocuments(Container<? extends Document> documents, List<? extends Document> allDocuments,
            int limit) {
        // TODO: Page through container filling allDocuments

    }

    public ServiceConfig getConfig() {
        return _Config;
    }

    public Service getDefaultService() {
        return _DefaultService;
    }

    public final HypermediaEngine getHypermediaEngine(URI apiId) {
        if (!_HypermediaEngines.containsKey(apiId)) {
            final HypermediaEngine hypermediaEngine = new HypermediaEngine(this, apiId);
            _HypermediaEngines.put(apiId, hypermediaEngine);
        }

        return _HypermediaEngines.get(apiId);
    }

    public ModelHeap getModelHeap() {
        return _ModelHeap;
    }

    public final Prototype getPrototype(java.lang.reflect.Type staticInterfaceType) {
        return _SystemSchemaService.getPrototype(staticInterfaceType);
    }

    public final Schema getSchema(URI schemaId) {
        final MediaType schemaMediaType = getSystemTransformers().getMediaTypeToNativeTypeTransformer().bToA(
                Schema.class);
        final Service schemaService = getService(schemaMediaType);
        return (Schema) ((Model) schemaService.get(schemaId, null, schemaMediaType, null)).getStaticInterface();
    }

    public final Service getService(Class<?> schemaInterfaceType) {
        final URI schemaId = getSystemTransformers().getClassToSchemaIdTransformer().aToB(schemaInterfaceType);
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

    /*
     * public final StringTransformer<?>
     * getStringTransformer(TextSyntaxConstraint textSyntaxConstraint) {
     * // TODO: Implement this mapping with configuration-based mapping falling
     * back to code on demand
     * return null;
     * 
     * }
     */

    public final Service getService(String className) {
        final URI schemaId = getSystemTransformers().getSchemaIdToFullNameTransformer().bToA(className);
        return getService(schemaId);
    }

    public final Service getService(URI schemaId) {
        return getService(getSystemTransformers().getMediaTypeToSchemaIdTransformer().bToA(schemaId));
    }

    public final ObservableMap<MediaType, Service> getServices() {
        return _Services;
    }

    public Transformers<String> getStringTransformers() {
        return _StringTransformers;
    }

    public SystemTransformers getSystemTransformers() {
        return _SystemTransformers;
    }

    public TypeSystem getTypeSystem() {
        return _TypeSystem;
    }

    @SuppressWarnings("unchecked")
    public <T> void mapFieldsByFieldName(final Map<T, Field> fieldMap, final List<Field> fieldList,
            final String fieldName) {
        for (final Field schemaField : fieldList) {
            fieldMap.put((T) schemaField.getFieldValue(fieldName), schemaField);
        }
    }

    public final void setDefaultService(Service defaultService) {
        _DefaultService = defaultService;
    }

    public final void setSchemaService(Service schemaService) {

        if (!(schemaService instanceof CachingService)) {
            schemaService = _ModelHeap.getShard(Schema.class).newModelCachingService(schemaService);
        }
        final MediaType schemaMediaType = getSystemTransformers().getMediaTypeToNativeTypeTransformer().bToA(
                Schema.class);
        _Services.put(schemaMediaType, schemaService);

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

    /*
     * TODO: Note that this method is currently unused since this sequence now
     * automatically happens whenever the runtime's first Model is read.
     * Consider removing it or changing its purpose.
     */
    private void bootstrap() {

        final SystemTransformers systemTransformers = getSystemTransformers();

        // Get the media type: application/wrml; schema="http://.../org/wrml/core/model/schema/Schema"
        final MediaType schemaMediaType = systemTransformers.getMediaTypeToNativeTypeTransformer().bToA(Schema.class);

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

        final Transformer<MediaType, URI> mediaTypeToSchemaIdTransformer = systemTransformers
                .getMediaTypeToSchemaIdTransformer();

        /*
         * If we want to get the Schema that represents the form of all WRML
         * models, then we are looking for the MetaSchema, which has an id value
         * of "http://.../org/wrml/core/model/schema/Schema", which we know is
         * part of the media type that we used to get the service. So we already
         * have what we need to get the URI in the media type but we can use a
         * pre-existing transformer to handle this conversion for us.
         * 
         * Using the right transformer, we can get the URI id of a given schema
         * by starting with a WRML media type (with a schema param).
         */

        final URI schemaSchemaId = mediaTypeToSchemaIdTransformer.aToB(schemaMediaType);

        // Use the Service's overloaded get method to request the MetaSchema.
        final Model dynamicMetaSchemaModel = (Model) schemaService.get(schemaSchemaId, null, schemaMediaType, null);

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

        final Schema staticMetaSchemaModel = (Schema) dynamicMetaSchemaModel.getStaticInterface();

    }

}
