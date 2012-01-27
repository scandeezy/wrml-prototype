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

import org.wrml.HyperLink;
import org.wrml.Model;
import org.wrml.model.Document;
import org.wrml.model.DocumentMetadata;
import org.wrml.model.DocumentOptions;
import org.wrml.model.api.ResourceTemplate;
import org.wrml.model.schema.Schema;
import org.wrml.runtime.bootstrap.FieldNames;
import org.wrml.runtime.event.FieldEvent;
import org.wrml.runtime.event.FieldEventListener;
import org.wrml.runtime.event.LinkEventListener;
import org.wrml.runtime.event.ModelEventListener;
import org.wrml.runtime.system.service.schema.Prototype;
import org.wrml.runtime.system.transformer.SystemTransformers;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.util.observable.MapEvent;
import org.wrml.util.observable.MapEventListener;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;
import org.wrml.www.MediaType;

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
    private final ObservableMap<URI, HyperLink> _HyperLinks;

    private transient FieldMapEventListener _FieldMapEventListener;

    private transient List<ModelEventListener> _EventListeners;
    private transient Map<String, List<FieldEventListener>> _FieldEventListeners;

    private MediaType _MediaType;
    private transient URI _ResourceTemplateId;

    RuntimeModel(Context context, java.lang.reflect.Type nativeType, ModelGraph modelGraph, FieldMap fieldMap,
            Map<URI, HyperLink> linkMap) {
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
        _Fields.addMapEventListener(_FieldMapEventListener);

        _HyperLinks = Observables.observableMap(linkMap);

        _ModelGraph = modelGraph;
        _ModelGraph.pushInitCursorIn(this);

        //init();

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

    public Object clickLink(URI rel, java.lang.reflect.Type nativeReturnType, Object requestEntity,
            Map<String, String> hrefParams) {
        final HyperLink runtimeHyperLink = getHyperLink(rel);

        if (runtimeHyperLink == null) {
            // TODO: Error here instead?
            return null;
        }

        return runtimeHyperLink.click(nativeReturnType, requestEntity, hrefParams);
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

        extendFields(modelToExtend);

        for (final Model additionalModelToExtend : additionalModelsToExtend) {
            extendFields(additionalModelToExtend);
        }

    }

    public Model getDynamicInterface() {
        return this;
    }

    public Object getFieldValue(String fieldName) {
        return _Fields.get(fieldName);
    }

    public ObservableMap<URI, HyperLink> getHyperLinks() {
        return _HyperLinks;
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
        // TODO: Consider prototype default values somewhere

        _Fields.clear();

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

        if (!_HyperLinks.containsKey(rel)) {
            final HyperLink link = new RuntimeHyperLink(this, rel);
            _HyperLinks.put(rel, link);
        }

        return _HyperLinks.get(rel);
    }

    private Class<?> getStaticInterfaceClass() {
        return getContext().getSystemTransformers().getNativeTypeToClassTransformer().aToB(getNativeType());
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
