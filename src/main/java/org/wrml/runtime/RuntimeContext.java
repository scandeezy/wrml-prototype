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
import java.util.Map;

import org.wrml.Context;
import org.wrml.Model;
import org.wrml.model.runtime.Prototype;
import org.wrml.model.runtime.PrototypeField;
import org.wrml.model.schema.Schema;
import org.wrml.service.CachingService;
import org.wrml.service.Service;
import org.wrml.service.WebService;
import org.wrml.service.runtime.PrototypeFieldService;
import org.wrml.service.runtime.PrototypeService;
import org.wrml.service.runtime.SchemaService;
import org.wrml.service.runtime.SystemSchemaService;
import org.wrml.util.DelegatingObservableMap;
import org.wrml.util.ObservableMap;
import org.wrml.util.UriTransformer;

/**
 * A stateful class that exposes wrml object-based services to wrml objects and
 * offers some utility functions.
 */
public class RuntimeContext implements Context {

    private final ObservableMap<URI, Service> _ServiceMap;
    private PrototypeService _PrototypeService;
    private CachingService _PrototypeCachingService;

    private SchemaService _SchemaService;
    private CachingService _SchemaCachingService;

    public RuntimeContext() {
        this(null);
    }

    public RuntimeContext(Service systemSchemaOriginService) {

        _ServiceMap = new DelegatingObservableMap<URI, Service>(new HashMap<URI, Service>());

        if (systemSchemaOriginService == null) {
            systemSchemaOriginService = new WebService(this, SystemSchemaService.DEFAULT_SCHEMA_API_DOCROOT);
        }

        SystemSchemaService systemSchemaService = new SystemSchemaService(this, systemSchemaOriginService);
        setSchemaService(systemSchemaService);

    }

    public Model createModel(URI schemaId) {
        return createModel(schemaId, null, null);
    }

    public Model createModel(URI schemaId, URI modelId) {
        return createModel(schemaId, modelId, null);
    }

    public Model createModel(URI schemaId, URI modelId, Model requestor) {
        RuntimeModel dynamicModel = new RuntimeModel(schemaId, this);
        if (modelId != null) {
            dynamicModel.setId(modelId);
        }

        Model staticModel = StaticModelProxy.newProxyInstance(dynamicModel);
        return staticModel;
    }

    public String getClassName(URI schemaId) {
        UriTransformer schemaIdTransformer = _SchemaService.getIdTransformer(null);
        return (String) schemaIdTransformer.aToB(schemaId);
    }

    public Model getModel(Class<?> clazz, URI modelId) {
        Service service = getService(clazz);
        return service.get(modelId);
    }

    public Model getModel(Class<?> clazz, URI modelId, Model requestor) {
        Service service = getService(clazz);
        return service.get(modelId, requestor);
    }

    public Prototype getPrototype(URI schemaId) {
        return (Prototype) _PrototypeCachingService.get(schemaId);
    }

    public Schema getSchema(URI schemaId) {
        return (Schema) _SchemaCachingService.get(schemaId);
    }

    public URI getSchemaId(Class<?> clazz) {
        return getSchemaId(clazz.getCanonicalName());
    }

    public URI getSchemaId(String className) {
        UriTransformer schemaIdTransformer = _SchemaService.getIdTransformer(null);
        return schemaIdTransformer.bToA(className);
    }

    public SchemaService getSchemaService() {
        return _SchemaService;
    }

    public Service getService(Class<?> clazz) {
        URI schemaId = getSchemaId(clazz);
        return getService(schemaId);
    }

    public Service getService(String className) {
        URI schemaId = getSchemaId(className);
        return getService(schemaId);
    }

    public Service getService(URI schemaId) {
        return _ServiceMap.get(schemaId);
    }

    public void setSchemaService(SchemaService schemaService) {

        _SchemaService = schemaService;
        _PrototypeService = new PrototypeService(this);
        final Service prototypeFieldService = new PrototypeFieldService(this);

        _SchemaCachingService = createCachingService(schemaService);
        _PrototypeCachingService = createCachingService(_PrototypeService);
        final CachingService prototypeFieldCachingService = createCachingService(prototypeFieldService);

        URI schemaSchemaId = getSchemaId(Schema.class);
        URI prototypeSchemaId = getSchemaId(Prototype.class);
        URI prototypeFieldSchemaId = getSchemaId(PrototypeField.class);

        _ServiceMap.put(schemaSchemaId, _SchemaCachingService);
        _ServiceMap.put(prototypeSchemaId, _PrototypeCachingService);
        _ServiceMap.put(prototypeFieldSchemaId, prototypeFieldCachingService);
    }

    public CachingService createCachingService(Service originService) {
        final Map<URI, Model> rawModelMap = new HashMap<URI, Model>();
        final ObservableMap<URI, Model> observableModelCache = new DelegatingObservableMap<URI, Model>(rawModelMap);
        return new CachingService(this, originService, observableModelCache);
    }

    public ObservableMap<URI, Service> getServiceMap() {
        return _ServiceMap;
    }

}
