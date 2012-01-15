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

import org.wrml.Model;
import org.wrml.bootstrap.FieldNames;
import org.wrml.event.FieldEvent;
import org.wrml.event.FieldEventListener;
import org.wrml.event.LinkEventListener;
import org.wrml.event.ModelEventListener;
import org.wrml.model.Document;
import org.wrml.model.DocumentMetadata;
import org.wrml.model.DocumentOptions;
import org.wrml.model.api.ResourceTemplate;
import org.wrml.model.schema.Schema;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.util.FieldMap;
import org.wrml.util.MediaType;
import org.wrml.util.observable.MapEvent;
import org.wrml.util.observable.MapEventListener;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;

/**
 * <p>
 * The base class for all web resource schema instances. It provides a set of
 * features intended to attract developers wanting to work with an easy-to-use
 * REST-based application framework.
 * </p>
 */
public final class RuntimeModel implements Model {

    private Model _StaticInterface;

    private static final long serialVersionUID = 1L;

    private final ObservableMap<String, Object> _Fields;
    private final ObservableMap<URI, HyperLink> _Links;

    private transient FieldMapEventListener _FieldMapEventListener;

    private final transient Context _Context;

    private final MediaType _MediaType;

    private URI _ResourceTemplateId;

    private final List<URI> _EmbeddedLinkRelationIds;
    private transient List<ModelEventListener> _EventListeners;
    private transient Map<String, List<FieldEventListener>> _FieldEventListeners;

    RuntimeModel(Context context, MediaType mediaType, List<URI> embeddedLinkRelationIds, FieldMap fieldMap,
            Map<URI, HyperLink> linkMap) {

        if (context == null) {
            throw new NullPointerException("Context cannot be null");
        }

        if (mediaType == null) {
            throw new NullPointerException("Media Type cannot be null");
        }

        _Context = context;
        _MediaType = mediaType;
        _EmbeddedLinkRelationIds = embeddedLinkRelationIds;

        _Fields = Observables.observableMap(fieldMap);
        _FieldMapEventListener = new FieldMapEventListener();
        _Fields.addMapEventListener(_FieldMapEventListener);

        _Links = Observables.observableMap(linkMap);

        init();

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

    @SuppressWarnings("unchecked")
    public Object clickLink(URI rel, MediaType responseType, Object requestEntity, Map<String, String> hrefParams) {
        final HyperLink hyperLink = getHyperLink(rel);

        if (hyperLink == null) {
            // TODO: Error here instead?
            return null;
        }

        return hyperLink.click(responseType, requestEntity, hrefParams);
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

    private void extendFields(Model modelToExtend) {
        final RuntimeModel modelToExtendDynamicInterface = (RuntimeModel) modelToExtend.getDynamicInterface();
        final Map<String, Object> fieldsToExtend = modelToExtendDynamicInterface._Fields;
        _Fields.putAll(fieldsToExtend);
    }

    public final Context getContext() {
        return _Context;
    }

    public Model getDynamicInterface() {
        return this;
    }

    public final List<URI> getEmbeddedLinkRelationIds() {
        return _EmbeddedLinkRelationIds;
    }

    @SuppressWarnings("unchecked")
    public Object getFieldValue(String fieldName) {
        return _Fields.get(fieldName);
    }

    public final HypermediaEngine getHypermediaEngine() {
        final ResourceTemplate resourceTemplate = getResourceTemplate();
        if (resourceTemplate == null) {
            return null;
        }

        final URI apiId = resourceTemplate.getRoot().getId();
        return getContext().getHypermediaEngine(apiId);
    }

    public ObservableMap<URI, HyperLink> getLinkMap() {
        return _Links;
    }

    public DocumentMetadata getMetadata() {
        // TODO Auto-generated method stub
        return null;
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

    public DocumentOptions getOptions() {
        // TODO Auto-generated method stub
        return null;
    }

    public Document getParent() {
        // TODO Auto-generated method stub
        return null;
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
        final Context context = getContext();
        return context.getSchemaIdToMediaTypeTransformer().bToA(getMediaType());
    }

    public MediaType getMediaType() {
        return _MediaType;
    }

    public Prototype getPrototype() {
        return getContext().getPrototype(getMediaType());
    }

    @SuppressWarnings("unchecked")
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
        final URI modelSchemaURI = context.getSchemaIdToClassTransformer().bToA(Model.class);
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

            return (Model) this;
        }

        // Get the Java class representing our schema
        final Class<?> schemaInterface = context.getSchemaIdToClassTransformer().aToB(schemaId);

        //System.err.println("====== getStaticInterface() schemaId : \"" + schemaId + "\" schemaInterface : \""  + schemaInterface + "\" (" + hashCode() + ")");

        /*
         * If we already implement the interface...
         * 
         * Is this possible? If this class is final then this cannot already
         * implement some unknown schema interface, right? Unless maybe this
         * class has been subclassed? This may be needed if this class ever
         * becomes un-final. Not sure if it is harmful to keep here anyway.
         */
        if (schemaInterface.isInstance(this)) {
            _StaticInterface = (Model) schemaInterface.cast(this);
        }
        else {

            /*
             * Create a proxy that layers the static Java interface
             * associated
             * with this model's "declared" schema.
             */

            final Class<?>[] schemaInterfaceArray = new Class<?>[] { schemaInterface };
            final StaticInterfaceFacade facade = new StaticInterfaceFacade(this);

            _StaticInterface = (Model) Proxy.newProxyInstance(context, schemaInterfaceArray, facade);
        }

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

    @SuppressWarnings("unchecked")
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
    protected void init() {
        //    setAllFieldsToDefaultValue();
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

        if (!_Links.containsKey(rel)) {
            final HyperLink link = new HyperLink(this, rel);
            _Links.put(rel, link);
        }

        return _Links.get(rel);
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
