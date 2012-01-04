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

import org.wrml.Model;
import org.wrml.model.Container;
import org.wrml.model.Document;
import org.wrml.model.schema.Schema;
import org.wrml.runtime.service.SystemSchemaService;
import org.wrml.service.CachingService;
import org.wrml.service.Service;
import org.wrml.service.WebClient;
import org.wrml.util.MediaType;
import org.wrml.util.observable.DelegatingObservableMap;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;
import org.wrml.util.transformer.CachingTransformer;
import org.wrml.util.transformer.MediaTypeToClassTransformer;
import org.wrml.util.transformer.MediaTypeToStringTransformer;
import org.wrml.util.transformer.SchemaIdToClassNameTransformer;
import org.wrml.util.transformer.SchemaIdToClassTransformer;
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
 * TODO: Eventually need to figure out how to make LinkRelation and other
 * core types extend Model. This is the dog food that WRML must eat in
 * order to use itself to make itself (bootstrap).
 * 
 * This will allow WRML to treat these core type instance "loadings" just
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
    public static final String FIELD_NAME_ID = "id";

    private final ObservableMap<MediaType, Service> _Services;
    private Transformer<MediaType, String> _MediaTypeToStringTransformer;
    private Transformer<MediaType, Class<?>> _MediaTypeToClassTransformer;

    private Transformer<URI, MediaType> _SchemaIdToMediaTypeTransformer;
    private Transformer<URI, Class<?>> _SchemaIdToClassTransformer;
    private Transformer<URI, String> _SchemaIdToClassNameTransformer;
    private Transformer<URI, String> _UriToStringTransformer;

    private Service _DefaultService;

    private final ObservableMap<URI, Prototype> _Prototypes;
    private final ObservableMap<URI, HypermediaEngine> _HypermediaEngines;

    public Context() {
        this(getSystemClassLoader());
    }

    public Context(ClassLoader parent) {
        super(parent);

        _Services = Observables.observableMap(new HashMap<MediaType, Service>());

        _Prototypes = Observables.observableMap(new HashMap<URI, Prototype>());
        _HypermediaEngines = Observables.observableMap(new HashMap<URI, HypermediaEngine>());

        Transformer<URI, String> uriToStringTransformer = new CachingTransformer<URI, String, Transformer<URI, String>>(
                new UriToStringTransformer());

        setUriToStringTransformer(uriToStringTransformer);

        Transformer<MediaType, String> mediaTypeToStringTransformer = new CachingTransformer<MediaType, String, Transformer<MediaType, String>>(
                new MediaTypeToStringTransformer(this));
        setMediaTypeToStringTransformer(mediaTypeToStringTransformer);

        Transformer<MediaType, Class<?>> mediaTypeToClassTransformer = new CachingTransformer<MediaType, Class<?>, Transformer<MediaType, Class<?>>>(
                new MediaTypeToClassTransformer(this));

        setMediaTypeToClassTransformer(mediaTypeToClassTransformer);

        Transformer<URI, MediaType> schemaIdToMediaTypeTransformer = new CachingTransformer<URI, MediaType, Transformer<URI, MediaType>>(
                new SchemaIdToMediaTypeTransformer(this));

        setSchemaIdToMediaTypeTransformer(schemaIdToMediaTypeTransformer);

        Transformer<URI, String> schemaIdToClassNameTransformer = new CachingTransformer<URI, String, Transformer<URI, String>>(
                new SchemaIdToClassNameTransformer(this, DEFAULT_SCHEMA_API_DOCROOT));

        setSchemaIdToClassNameTransformer(schemaIdToClassNameTransformer);

        Transformer<URI, Class<?>> schemaIdToClassTransformer = new CachingTransformer<URI, Class<?>, Transformer<URI, Class<?>>>(
                new SchemaIdToClassTransformer(this));

        setSchemaIdToClassTransformer(schemaIdToClassTransformer);

        Service www = new WebClient(this);

        Service defaultService = instantiateCachingService(www);
        setDefaultService(defaultService);

        Service schemaService = new SystemSchemaService(this, www);
        setSchemaService(schemaService);
    }

    public final void fetchAllDocuments(Container<? extends Document> documents, List<? extends Document> allDocuments) {
        // TODO: Page through container filling allDocuments

    }

    /*
     * TODO: Consider moving all of this voodoo to the SchemaService interface.
     * Keep this class more universally useful to all apss - not just the
     * bootstrapping of the WRML runtime "app".
     */

    public Service getDefaultService() {
        return _DefaultService;
    }

    public final HypermediaEngine getHypermediaEngine(URI apiId) {
        if (!_HypermediaEngines.containsKey(apiId)) {
            HypermediaEngine hypermediaEngine = new HypermediaEngine(this, apiId);
            _HypermediaEngines.put(apiId, hypermediaEngine);
        }

        return _HypermediaEngines.get(apiId);
    }

    public Transformer<MediaType, Class<?>> getMediaTypeToClassTransformer() {
        return _MediaTypeToClassTransformer;
    }

    public Transformer<MediaType, String> getMediaTypeToStringTransformer() {
        return _MediaTypeToStringTransformer;
    }

    public final Prototype getPrototype(URI blueprintSchemaId) {

        if (!_Prototypes.containsKey(blueprintSchemaId)) {
            Prototype prototype = new Prototype(this, blueprintSchemaId);
            _Prototypes.put(blueprintSchemaId, prototype);
        }

        return _Prototypes.get(blueprintSchemaId);
    }

    public final Schema getSchema(URI schemaId) {
        MediaType schemaMediaType = getMediaTypeToClassTransformer().bToA(Schema.class);
        Service schemaService = getService(schemaMediaType);
        return (Schema) ((Model) schemaService.get(schemaId, schemaMediaType, null)).getStaticInterface();
    }

    public Transformer<URI, String> getSchemaIdToClassNameTransformer() {
        return _SchemaIdToClassNameTransformer;
    }

    public Transformer<URI, Class<?>> getSchemaIdToClassTransformer() {
        return _SchemaIdToClassTransformer;
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

    public Transformer<URI, MediaType> getSchemaIdToMediaTypeTransformer() {
        return _SchemaIdToMediaTypeTransformer;
    }

    public final Service getService(Class<?> clazz) {
        URI schemaId = getSchemaIdToClassTransformer().bToA(clazz);
        return getService(schemaId);
    }

    public final Service getService(MediaType mediaType) {

        /*
         * TODO: Allow for more complex mapping of schemas to services
         * For example, allow for a base schema to be registered for
         * sub-services or a uri pattern match.
         */

        Service service = _Services.get(mediaType);

        if (service != null) {
            return service;
        }

        return getDefaultService();
    }

    public final Service getService(String className) {
        URI schemaId = getSchemaIdToClassNameTransformer().bToA(className);
        return getService(schemaId);
    }

    public final Service getService(URI schemaId) {
        return getService(getSchemaIdToMediaTypeTransformer().aToB(schemaId));
    }

    public ObservableMap<MediaType, Service> getServices() {
        return _Services;
    }

    public Transformer<URI, String> getUriToStringTransformer() {
        return _UriToStringTransformer;
    }

    public final CachingService instantiateCachingService(Service originService) {
        final Map<URI, Object> rawModelMap = new HashMap<URI, Object>();
        final ObservableMap<URI, Object> observableModelCache = new DelegatingObservableMap<URI, Object>(rawModelMap);
        return new CachingService(this, originService, observableModelCache);
    }

    public final Model instantiateModel(Class<?> schemaClass, URI resourceTemplateId, URI resourceId) {
        return instantiateModel(getSchemaIdToClassTransformer().bToA(schemaClass), resourceTemplateId, resourceId);
    }

    public final Model instantiateModel(MediaType schemaMediaType, URI resourceTemplateId, URI resourceId) {
        return instantiateModel(getSchemaIdToMediaTypeTransformer().bToA(schemaMediaType), resourceTemplateId,
                resourceId);
    }

    public final Model instantiateModel(URI schemaId, URI resourceTemplateId, URI resourceId) {
        RuntimeModel model = new RuntimeModel(this, schemaId, resourceTemplateId);
        if (resourceId != null) {
            model.setFieldValue(FIELD_NAME_ID, resourceId);
        }
        return model;
    }

    public void setDefaultService(Service defaultService) {
        _DefaultService = defaultService;
    }

    public void setMediaTypeToClassTransformer(Transformer<MediaType, Class<?>> mediaTypeToClassTransformer) {
        _MediaTypeToClassTransformer = mediaTypeToClassTransformer;
    }

    public void setMediaTypeToStringTransformer(Transformer<MediaType, String> mediaTypeToStringTransformer) {
        _MediaTypeToStringTransformer = mediaTypeToStringTransformer;
    }

    public void setSchemaIdToClassNameTransformer(Transformer<URI, String> schemaIdToClassNameTransformer) {
        _SchemaIdToClassNameTransformer = schemaIdToClassNameTransformer;
    }

    public void setSchemaIdToClassTransformer(Transformer<URI, Class<?>> schemaIdToClassTransformer) {
        _SchemaIdToClassTransformer = schemaIdToClassTransformer;
    }

    public void setSchemaIdToMediaTypeTransformer(Transformer<URI, MediaType> schemaIdToMediaTypeTransformer) {
        _SchemaIdToMediaTypeTransformer = schemaIdToMediaTypeTransformer;
    }

    public final void setSchemaService(Service schemaService) {

        if (!(schemaService instanceof CachingService)) {
            schemaService = instantiateCachingService(schemaService);
        }
        MediaType schemaMediaType = getMediaTypeToClassTransformer().bToA(Schema.class);
        _Services.put(schemaMediaType, schemaService);
        
    }

    public void setUriToStringTransformer(Transformer<URI, String> uriToStringTransformer) {
        _UriToStringTransformer = uriToStringTransformer;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // TODO Auto-generated method stub
        return super.findClass(name);
    }

}
