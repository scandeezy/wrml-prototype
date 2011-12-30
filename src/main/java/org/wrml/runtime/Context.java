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

import java.lang.reflect.TypeVariable;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wrml.Model;
import org.wrml.model.Container;
import org.wrml.model.Document;
import org.wrml.model.communication.MediaType;
import org.wrml.model.schema.Schema;
import org.wrml.model.schema.TextSyntaxConstraint;
import org.wrml.runtime.service.SchemaService;
import org.wrml.runtime.service.SystemSchemaService;
import org.wrml.service.CachingService;
import org.wrml.service.Service;
import org.wrml.service.WebClient;
import org.wrml.util.observable.DelegatingObservableMap;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;
import org.wrml.util.transformer.CachingTransformer;
import org.wrml.util.transformer.StringTransformer;
import org.wrml.util.transformer.Transformer;
import org.wrml.util.transformer.UriTransformer;

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
public class Context {

    private static final String TYPE_APPLICATION = "application";
    private static final String SUBTYPE_WRML = "wrml";

    private static final String MEDIA_TYPE_STRING_WRML = TYPE_APPLICATION + '/' + SUBTYPE_WRML;

    private static final String MEDIA_TYPE_REGEX_STRING = "^" + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)/"
            + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)( +" + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)=\"([^\"]*)\";)*$";

    private static final Pattern MEDIA_TYPE_REGEX_PATTERN = Pattern.compile(MEDIA_TYPE_REGEX_STRING);

    private final ObservableMap<URI, Service> _ServiceMap;
    private final ObservableMap<URI, Prototype> _Prototypes;
    private final ObservableMap<URI, HypermediaEngine> _HypermediaEngines;

    private SchemaService _SchemaService;
    private CachingService _SchemaCachingService;

    private Transformer<MediaType, String> _MediaTypeToStringTransformer;
    private Transformer<MediaType, Class<?>> _MediaTypeToClassTransformer;
    private Transformer<MediaType, URI> _MediaTypeToSchemaIdTransformer;

    public Context() {
        _ServiceMap = Observables.observableMap(new HashMap<URI, Service>());
        _Prototypes = Observables.observableMap(new HashMap<URI, Prototype>());
        _HypermediaEngines = Observables.observableMap(new HashMap<URI, HypermediaEngine>());

        SchemaService schemaService = new SystemSchemaService(this, new WebClient(this));
        setSchemaService(schemaService);

        Transformer<MediaType, String> mediaTypeToStringTransformer = new CachingTransformer<MediaType, String, Transformer<MediaType, String>>(
                new MediaTypeToStringTransformer());
        setMediaTypeToStringTransformer(mediaTypeToStringTransformer);

        Transformer<MediaType, Class<?>> mediaTypeToClassTransformer = new CachingTransformer<MediaType, Class<?>, Transformer<MediaType, Class<?>>>(
                new MediaTypeToClassTransformer());

        setMediaTypeToClassTransformer(mediaTypeToClassTransformer);

        Transformer<MediaType, URI> mediaTypeToSchemaIdTransformer = new CachingTransformer<MediaType, URI, Transformer<MediaType, URI>>(
                new MediaTypeToSchemaIdTransformer());

        setMediaTypeToSchemaIdTransformer(mediaTypeToSchemaIdTransformer);
    }

    public final String getClassName(URI schemaId) {
        UriTransformer<?> schemaIdTransformer = _SchemaService.getIdTransformer();
        return (String) schemaIdTransformer.aToB(schemaId);
    }

    /*
     * TODO: Consider moving all of this voodoo to the SchemaService interface.
     * Keep this class more universally useful to all apss - not just the
     * bootstrapping of the WRML runtime "app".
     */

    public final Transformer<MediaType, Class<?>> getMediaTypeToClassTransformer() {
        return _MediaTypeToClassTransformer;
    }

    public final Transformer<MediaType, URI> getMediaTypeToSchemaIdTransformer() {
        return _MediaTypeToSchemaIdTransformer;
    }

    public final Transformer<MediaType, String> getMediaTypeToStringTransformer() {
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
        Service schemaService = getService(Schema.class);
        return (Schema) schemaService.get(schemaId);
    }

    public final URI getSchemaId(Class<?> clazz) {
        return getSchemaId(clazz.getCanonicalName());
    }

    public final URI getSchemaId(String className) {
        @SuppressWarnings("unchecked")
        UriTransformer<String> schemaIdTransformer = (UriTransformer<String>) _SchemaService.getIdTransformer();
        return schemaIdTransformer.bToA(className);
    }

    public final SchemaService getSchemaService() {
        return _SchemaService;
    }

    public final Service getService(Class<?> clazz) {
        URI schemaId = getSchemaId(clazz);
        return getService(schemaId);
    }

    public final Service getService(String className) {
        URI schemaId = getSchemaId(className);
        return getService(schemaId);
    }

    public final void fetchAllDocuments(Container<? extends Document> documents, List<? extends Document> allDocuments) {
        // TODO: Page through container filling allDocuments
        
    }

    public final Service getService(URI schemaId) {

        /*
         * TODO: Allow for more complex mapping of schemas to services
         * For example, allow for a base schema to be registered for
         * sub-services or a uri pattern match.
         */

        return _ServiceMap.get(schemaId);
    }

    public final ObservableMap<URI, Service> getServiceMap() {
        return _ServiceMap;
    }

    public final StringTransformer<?> getStringTransformer(TextSyntaxConstraint textSyntaxConstraint) {
        // TODO: Implement this mapping with configuration-based mapping falling back to code on demand
        return null;

    }

    public final CachingService instantiateCachingService(Service originService) {
        final Map<URI, Object> rawModelMap = new HashMap<URI, Object>();
        final ObservableMap<URI, Object> observableModelCache = new DelegatingObservableMap<URI, Object>(rawModelMap);
        return new CachingService(this, originService, observableModelCache);
    }

    public final Model instantiateModel(Class<?> schemaClass, URI resourceTemplateId) {
        final URI schemaId = getSchemaId(schemaClass);
        return instantiateModel(schemaId, resourceTemplateId);
    }

    public final Model instantiateModel(URI schemaId, URI resourceTemplateId) {
        RuntimeModel model = new RuntimeModel(this, schemaId, resourceTemplateId);
        return model;
    }

    public final void setMediaTypeToClassTransformer(Transformer<MediaType, Class<?>> mediaTypeToClassTransformer) {
        _MediaTypeToClassTransformer = mediaTypeToClassTransformer;
    }

    public final void setMediaTypeToSchemaIdTransformer(Transformer<MediaType, URI> mediaTypeToSchemaIdTransformer) {
        _MediaTypeToSchemaIdTransformer = mediaTypeToSchemaIdTransformer;
    }

    public final void setMediaTypeToStringTransformer(Transformer<MediaType, String> mediaTypeToStringTransformer) {
        _MediaTypeToStringTransformer = mediaTypeToStringTransformer;
    }

    public final HypermediaEngine getHypermediaEngine(URI apiId) {
        if (!_HypermediaEngines.containsKey(apiId)) {
            HypermediaEngine hypermediaEngine = new HypermediaEngine(this, apiId);
            _HypermediaEngines.put(apiId, hypermediaEngine);
        }

        return _HypermediaEngines.get(apiId);
    }

    public final void setSchemaService(SchemaService schemaService) {

        _SchemaService = schemaService;
        _SchemaCachingService = instantiateCachingService(schemaService);
        URI schemaSchemaId = getSchemaId(Schema.class);
        _ServiceMap.put(schemaSchemaId, _SchemaCachingService);
    }

    private final class MediaTypeToClassTransformer extends MediaTypeTransformer<Class<?>> {

        public Class<?> aToB(MediaType bValue) {
            // TODO Auto-generated method stub
            return null;
        }

        public MediaType bToA(Class<?> clazz) {

            if (clazz == null) {
                return null;
            }

            MediaType mediaType = null;
            Transformer<MediaType, String> mediaTypeToStringTransformer = getContext()
                    .getMediaTypeToStringTransformer();

            if (clazz.isAssignableFrom(Model.class)) {

                if (clazz.equals(Model.class)) {

                    // Return the vanilla application/wrml type for base models                    
                    return mediaTypeToStringTransformer.bToA(MEDIA_TYPE_STRING_WRML);
                }
                else {
                    // Return the application/wrml with schema param for derived models
                    URI schemaId = getSchemaId(clazz);

                    // TODO: Allow for parameterized types via Constraints
                    SortedMap<String, String> parameters = null;

                    /*
                     * Use reflection to determine if the type is parameterized.
                     * Ultimately
                     * looking to get a map of type parameter name (aka Schema
                     * Constraint Name)
                     * to schema Id. Get the name (keys) from the Java generics
                     * reflection API.
                     * Get the URI values using Java class name to Schema URI
                     * transform.
                     * 
                     * TODO: Need to figure out how to get a Java generic
                     * interface's
                     * parameterized type names and actual types (as a Model
                     * subclass)
                     * 
                     * http://www.artima.com/weblogs/viewpost.jsp?thread=208860
                     */

                    TypeVariable<?>[] typeParameters = clazz.getTypeParameters();
                    if (typeParameters != null) {
                        parameters = new TreeMap<String, String>();
                        for (TypeVariable<?> typeParam : typeParameters) {
                            String name = typeParam.getName();

                            Class<?> typeParamSchemaClass = null;
                            URI typeParamSchemaId = getSchemaId(typeParamSchemaClass);
                            parameters.put(name, String.valueOf(typeParamSchemaId));
                        }
                    }

                    String mediaTypeString = createMediaTypeString(TYPE_APPLICATION, SUBTYPE_WRML, parameters);
                    return mediaTypeToStringTransformer.bToA(mediaTypeString);
                }
            }
            else {
                // TODO: Get MediaType associated with non-WRML class
            }

            return mediaType;
        }

        public Context getContext() {
            return Context.this;
        }
    }

    private final class MediaTypeToSchemaIdTransformer extends MediaTypeTransformer<URI> {

        public URI aToB(MediaType aValue) {
            // TODO Auto-generated method stub
            return null;
        }

        public MediaType bToA(URI bValue) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    private final class MediaTypeToStringTransformer extends MediaTypeTransformer<String> {

        public String aToB(MediaType bValue) {
            return createMediaTypeString(bValue.getType(), bValue.getSubtype(), bValue.getParameters());
        }

        public MediaType bToA(String aValue) {

            final Matcher matcher = MEDIA_TYPE_REGEX_PATTERN.matcher(aValue);

            final String type = matcher.group(0);
            final String subtype = matcher.group(1);

            // TODO: Finish regex parsing for parameters

            final SortedMap<String, String> parameters = null;
            return createMediaType(type, subtype, parameters);
        }

        private MediaType createMediaType(final String type, final String subtype,
                final SortedMap<String, String> parameters) {

            MediaType mediaType = (MediaType) getContext().instantiateModel(MediaType.class, null).getStaticInterface();
            mediaType.setFieldValue("type", type);
            mediaType.setFieldValue("subtype", subtype);
            mediaType.setFieldValue("parameters", (parameters != null) ? Observables.observableMap(parameters) : null);

            return mediaType;
        }
    }

    private abstract class MediaTypeTransformer<B> implements Transformer<MediaType, B> {

        public Context getContext() {
            return Context.this;
        }

        protected String createMediaTypeString(final String type, final String subtype,
                final Map<String, String> parameters) {
            final StringBuilder sb = new StringBuilder();
            sb.append(type.trim()).append('/').append(subtype.trim());

            for (String parameterName : parameters.keySet()) {
                sb.append("; ").append(parameterName.trim()).append('=').append('\"')
                        .append(parameters.get(parameterName).trim()).append('\"');
            }

            return sb.toString();
        }

    }

}
