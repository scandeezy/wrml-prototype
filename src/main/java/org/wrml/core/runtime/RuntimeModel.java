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

import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.wrml.core.Hyperlink;
import org.wrml.core.Model;
import org.wrml.core.event.EventSource;
import org.wrml.core.model.Document;
import org.wrml.core.model.DocumentMetadata;
import org.wrml.core.model.DocumentOptions;
import org.wrml.core.model.api.ResourceTemplate;
import org.wrml.core.model.schema.Schema;
import org.wrml.core.runtime.bootstrap.FieldNames;
import org.wrml.core.runtime.event.FieldEvent;
import org.wrml.core.runtime.event.FieldEventListener;
import org.wrml.core.runtime.event.FieldEventListener.FieldEventName;
import org.wrml.core.runtime.event.LinkEventListener;
import org.wrml.core.runtime.event.ModelEvent;
import org.wrml.core.runtime.event.ModelEventListener;
import org.wrml.core.runtime.system.Prototype;
import org.wrml.core.runtime.system.transformer.SystemTransformers;
import org.wrml.core.service.ProxyService;
import org.wrml.core.service.Service;
import org.wrml.core.util.Cancelable;
import org.wrml.core.util.observable.CancelableMapEvent;
import org.wrml.core.util.observable.MapEvent;
import org.wrml.core.util.observable.MapEventListener;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.util.observable.Observables;
import org.wrml.core.www.MediaType;

/**
 * <p>
 * The base class for all web resource schema instances. It provides a set of
 * features intended to attract developers wanting to work with an easy-to-use
 * REST-based application framework.
 * </p>
 */
public final class RuntimeModel extends RuntimeObject implements Model {

    private static final long serialVersionUID = 1L;

    private transient final ModelGraph _ModelGraph;
    private transient Model _StaticInterface;
    private transient final java.lang.reflect.Type _NativeType;

    private final ObservableMap<String, Object> _Fields;
    private final ObservableMap<URI, Hyperlink> _Hyperlinks;

    private transient FieldMapEventListener _FieldMapEventListener;

    private MediaType _MediaType;
    private transient URI _ResourceTemplateId;

    private transient EventSource<ModelEventListener> _ModelEventSource;
    private transient Map<String, EventSource<FieldEventListener>> _FieldEventSources;
    private transient Map<URI, EventSource<LinkEventListener>> _LinkEventSources;

    RuntimeModel(Context context, java.lang.reflect.Type nativeType, ModelGraph modelGraph, FieldMap fieldMap,
            Map<URI, Hyperlink> linkMap) {
        super(context);

        if (nativeType == null) {
            throw new NullPointerException("Static Interface Type cannot be null");
        }

        _NativeType = nativeType;

        if (modelGraph == null) {
            throw new NullPointerException("ModelGraph cannot be null");
        }

        _Fields = Observables.observableMap(fieldMap);
        _FieldMapEventListener = new FieldMapEventListener();
        _Fields.addEventListener(_FieldMapEventListener);

        _Hyperlinks = Observables.observableMap(linkMap);

        _ModelGraph = modelGraph;
        _ModelGraph.pushInitCursorIn(this);

        //init();

        // TODO: Self-register for ID changes so that we can figure out where we are in the API.
        // Use the Context to gain access to the configured APIs which will then tell you which 
        // API you match.

        // Look up the API and initialize the hypermedia engine.

        // This should be helpful on both client and server-side.

    }

    public void absorb(Model modelToAbsorb, Model... additionalModelsToAbsorb) {
        // TODO Auto-generated method stub

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

    public boolean addEventListener(ModelEventListener listener) {
        if (_ModelEventSource == null) {
            _ModelEventSource = new EventSource<ModelEventListener>(ModelEventListener.class);
        }
        return _ModelEventSource.addEventListener(listener);
    }

    public boolean addFieldEventListener(String fieldName, FieldEventListener listener) {

        if (_FieldEventSources == null) {
            _FieldEventSources = new HashMap<String, EventSource<FieldEventListener>>();
        }

        EventSource<FieldEventListener> fieldEventSource = _FieldEventSources.get(fieldName);
        if (fieldEventSource == null) {
            fieldEventSource = new EventSource<FieldEventListener>(FieldEventListener.class);
            _FieldEventSources.put(fieldName, fieldEventSource);
        }

        return fieldEventSource.addEventListener(listener);
    }

    public boolean addLinkEventListener(URI linkRelationId, LinkEventListener listener) {

        if (_LinkEventSources == null) {
            _LinkEventSources = new HashMap<URI, EventSource<LinkEventListener>>();
        }

        EventSource<LinkEventListener> linkEventSource = _LinkEventSources.get(linkRelationId);
        if (linkEventSource == null) {
            linkEventSource = new EventSource<LinkEventListener>(LinkEventListener.class);
            _LinkEventSources.put(linkRelationId, linkEventSource);
        }

        return linkEventSource.addEventListener(listener);

    }

    public Object clickLink(URI rel, java.lang.reflect.Type nativeReturnType, Object requestEntity,
            Map<String, String> hrefParams) {
        final Hyperlink runtimeHyperLink = getHyperLink(rel);

        if (runtimeHyperLink == null) {
            // TODO: Error here instead?
            return null;
        }

        return runtimeHyperLink.click(nativeReturnType, requestEntity, hrefParams);
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void extend(Model modelToExtend, Model... additionalModelsToExtend) {

        extendFields(modelToExtend);

        for (final Model additionalModelToExtend : additionalModelsToExtend) {
            extendFields(additionalModelToExtend);
        }

    }

    public void free() {

        if (_ModelEventSource != null) {
            _ModelEventSource.fireEvent(ModelEventListener.ModelEventName.modelFreed, new ModelEvent(this));
            _ModelEventSource.free();
        }

        if (_FieldEventSources != null) {
            for (final String fieldName : _FieldEventSources.keySet()) {
                final EventSource<FieldEventListener> fieldEventSource = _FieldEventSources.get(fieldName);
                if (fieldEventSource != null) {
                    fieldEventSource.free();
                }
            }

            _FieldEventSources.clear();
            _FieldEventSources = null;
        }

        _Fields.removeEventListener(_FieldMapEventListener);

        if (_LinkEventSources != null) {
            for (final URI relId : _LinkEventSources.keySet()) {
                final EventSource<LinkEventListener> linkEventSource = _LinkEventSources.get(relId);
                if (linkEventSource != null) {
                    linkEventSource.free();
                }
            }

            _LinkEventSources.clear();
            _LinkEventSources = null;
        }

        for (final URI relId : _Hyperlinks.keySet()) {
            final Hyperlink hyperlink = _Hyperlinks.get(relId);
            hyperlink.free();
        }

    }

    public Model getDynamicInterface() {
        return this;
    }

    public Object getFieldValue(String fieldName) {
        return _Fields.get(fieldName);
    }

    public final URI getHeapId() {

        final Object idFieldValue = getFieldValue(FieldNames.Document.id.name());
        if (idFieldValue instanceof URI) {
            final URI id = (URI) idFieldValue;
            return id;
        }

        return null;
    }

    public ObservableMap<URI, Hyperlink> getHyperLinks() {
        return _Hyperlinks;
    }

    public final HypermediaEngine getHypermediaEngine() {
        final ResourceTemplate resourceTemplate = getResourceTemplate();
        if (resourceTemplate == null) {
            return null;
        }

        final URI apiId = resourceTemplate.getRoot().getId();
        return getContext().getHypermediaEngine(apiId);
    }

    public MediaType getMediaType() {

        if (_MediaType == null) {
            _MediaType = getContext().getSystemTransformers().getMediaTypeToNativeTypeTransformer()
                    .bToA(getNativeType());
        }

        return _MediaType;
    }

    public DocumentMetadata getMetadata() {
        // TODO Auto-generated method stub
        return null;
    }

    public ModelGraph getModelGraph() {
        return _ModelGraph;
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

    public java.lang.reflect.Type getNativeType() {
        return _NativeType;
    }

    public java.lang.reflect.Type[] getNativeTypeParameters() {
        final Context context = getContext();
        final TypeSystem typeSystem = context.getTypeSystem();
        return typeSystem.getNativeTypeParameters(getNativeType());
    }

    public DocumentOptions getOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    public Document getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    public Prototype getPrototype() {
        return getContext().getPrototype(getNativeType());
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
        final SystemTransformers systemTransformers = getContext().getSystemTransformers();
        return systemTransformers.getMediaTypeToSchemaIdTransformer().aToB(getMediaType());
    }

    public Model getStaticInterface() {

        if (Proxy.isProxyClass(this.getClass())) {

            /*
             * If there is a proxy wrapped around us lets assume that we've
             * got
             * a static interface.
             */

            return this;
        }

        if (_StaticInterface != null) {
            /*
             * We've been here before.
             */
            return _StaticInterface;
        }

        final URI schemaId = getSchemaId();
        final Context context = getContext();
        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final URI modelSchemaURI = systemTransformers.getClassToSchemaIdTransformer().aToB(Model.class);
        if (modelSchemaURI.equals(schemaId)) {

            /*
             * We have the same schema ID as Model.class does.
             * 
             * So, this model is already just supposed to be a model (i.e.
             * it's
             * not a subschema). This class implements the Model interface.
             * So this object already provides a "static" Model interface.
             * 
             * No need to proxy.
             */

            return this;
        }

        /*
         * If we already implement the interface...
         * 
         * Is this possible? If this class is final then this cannot already
         * implement some unknown schema interface, right? Unless maybe this
         * class has been subclassed? This may be needed if this class ever
         * becomes un-final. Not sure if it is harmful to keep here anyway.
         */
        //if (_NativeType.isInstance(this)) {
        //    _StaticInterface = (Model) _NativeType.cast(this);
        //}
        //else {

        /*
         * Create a proxy that layers the static Java interface
         * associated
         * with this model's "declared" schema.
         */

        final Class<?> schemaInterfaceClass = systemTransformers.getNativeTypeToClassTransformer()
                .aToB(getNativeType());
        final Class<?>[] schemaInterfaceArray = new Class<?>[] { schemaInterfaceClass };
        final StaticInterfaceFacade facade = new StaticInterfaceFacade(this);

        _StaticInterface = (Model) Proxy.newProxyInstance(context, schemaInterfaceArray, facade);
        //}

        /*
         * We are now able to return this same static interface repeatedly.
         * There's no need to generate a new static facade for each call.
         */

        return _StaticInterface;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
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

    public boolean isFieldValueSet(String fieldName) {
        return (_Fields != null) && _Fields.containsKey(fieldName);
    }

    public final boolean removeEventListener(ModelEventListener listener) {

        if (_ModelEventSource == null) {
            return false;
        }

        return _ModelEventSource.removeEventListener(listener);
    }

    public final boolean removeFieldEventListener(String fieldName, FieldEventListener listener) {
        if (_FieldEventSources == null) {
            return false;
        }

        final EventSource<FieldEventListener> fieldEventSource = _FieldEventSources.get(fieldName);
        if (fieldEventSource == null) {
            return false;
        }

        return fieldEventSource.removeEventListener(listener);
    }

    public final boolean removeLinkEventListener(URI linkRelationId, LinkEventListener listener) {
        if (_LinkEventSources == null) {
            return false;
        }

        final EventSource<LinkEventListener> linkEventSource = _LinkEventSources.get(linkRelationId);
        if (linkEventSource == null) {
            return false;
        }

        return linkEventSource.removeEventListener(listener);
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

    public void setAllFieldsToDefaultValue() {

        // TODO: Setting default values doesn't mean clearing the fields (check the schema defaults instead).
        // TODO: Consider prototype default values somewhere

        _Fields.clear();

    }

    public void setFieldToDefaultValue(String fieldName) {

        // TODO: Consider prototype default value somewhere

        _Fields.put(fieldName, null);

    }

    public Object setFieldValue(String fieldName, Object newValue) {
        return _Fields.put(fieldName, newValue);
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
        sb.append("    mediaType : " + getMediaType() + ",\n");
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
    //protected void init() {
    //    setAllFieldsToDefaultValue();
    //}

    private void extendFields(Model modelToExtend) {
        final RuntimeModel modelToExtendDynamicInterface = (RuntimeModel) modelToExtend.getDynamicInterface();
        final Map<String, Object> fieldsToExtend = modelToExtendDynamicInterface._Fields;
        _Fields.putAll(fieldsToExtend);
    }

    private void fireFieldConstraintViolated(final FieldEvent event) {
        fireFieldEvent(FieldEventName.fieldConstraintViolated, event);
    }

    private boolean fireFieldEvent(final FieldEventName fieldEventName, final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final EventSource<FieldEventListener> fieldEventSource = _FieldEventSources.get(fieldName);
        if (fieldEventSource != null) {

            fieldEventSource.fireEvent(fieldEventName, event);

            if (event instanceof Cancelable) {
                return !((Cancelable) event).isCancelled();
            }
        }

        return true;
    }

    private void fireFieldValueChanged(final FieldEvent event) {
        fireFieldEvent(FieldEventName.fieldValueChanged, event);
    }

    private void fireFieldValueInitialized(final FieldEvent event) {
        fireFieldEvent(FieldEventName.fieldValueInitialized, event);
    }

    private Hyperlink getHyperLink(URI rel) {

        if (!_Hyperlinks.containsKey(rel)) {
            final Hyperlink link = new RuntimeHyperlink(this, rel);
            _Hyperlinks.put(rel, link);
        }

        return _Hyperlinks.get(rel);
    }

    private Class<?> getStaticInterfaceClass() {
        return getContext().getSystemTransformers().getNativeTypeToClassTransformer().aToB(getNativeType());
    }

    private class FieldMapEventListener implements MapEventListener {

        public void omMapRemovingEntry(CancelableMapEvent event) {
            // TODO Auto-generated method stub

        }

        public void onMapCleared(MapEvent event) {
            // TODO Auto-generated method stub

        }

        public void onMapClearing(CancelableMapEvent event) {
            // TODO Auto-generated method stub

        }

        public void onMapEntryRemoved(MapEvent event) {
            // TODO Auto-generated method stub

        }

        public void onMapEntryUpdated(MapEvent event) {
            // TODO Auto-generated method stub

        }

        public void onMapUpdatingEntry(CancelableMapEvent event) {
            // TODO Auto-generated method stub

        }

        // TODO Run constraint validators

        // TODO Consider the read only status of this object

        // TODO Consider the read only status of the schema's field

        // TODO Fire field events

    }

}
