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

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.wrml.api.FieldDefault;
import org.wrml.api.ResourceTemplate;
import org.wrml.schema.Field;
import org.wrml.schema.Schema;
import org.wrml.util.Identifiable;
import org.wrml.util.MapEvent;
import org.wrml.util.MapEventListener;
import org.wrml.util.ObservableList;
import org.wrml.util.ObservableMap;

/**
 * The abstract base class for all web resource schema instances.
 */
public class AbstractWrmlObject extends Identifiable<String> implements WrmlObject {

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

    private static final long serialVersionUID = -7696720779481780624L;

    private final URI _ResourceTemplateId;
    private final URI _SchemaId;
    private transient Context _Context;
    private ObservableMap<String, Object> _FieldMap;
    private ObservableMap<URI, Link> _LinkMap;
    private transient FieldMapEventListener _FieldMapEventListener;
    private boolean _ReadOnly;

    private transient List<WrmlObjectEventListener> _EventListeners;
    private transient Map<String, List<FieldEventListener<?>>> _FieldEventListeners;

    public AbstractWrmlObject(URI schemaId, URI resourceTemplateId, Context context) {
        _SchemaId = schemaId;
        _ResourceTemplateId = resourceTemplateId;
        _Context = context;
    }

    public void addEventListener(WrmlObjectEventListener listener) {
        if (_EventListeners == null) {
            _EventListeners = new CopyOnWriteArrayList<WrmlObjectEventListener>();
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

    public Context getContext() {
        return _Context;
    }

    public ObservableMap<String, Object> getFieldMap() {
        return _FieldMap;
    }

    public Object getFieldValue(String fieldName) {

        if ((_FieldMap != null) && _FieldMap.containsKey(fieldName)) {
            return _FieldMap.get(fieldName);
        }

        // TODO: No value for the field was set locally. Locate "nearest"
        // default value setting by looking at the ResourceTemplate and
        // then up the schema tree.

        final ResourceTemplate resourceTemplate = getResourceTemplate();

        final URI schemaId = getSchemaId();

        final ObservableMap<String, FieldDefault<?>> fieldDefaults = resourceTemplate.getFieldDefaults(schemaId);

        if (fieldDefaults.containsKey(fieldName)) {
            return fieldDefaults.get(fieldName).getDefaultValue();
        }

        // TODO: Traverse up the rest of resource template tree to find a
        // default specified in the resource hierarchy

        final Schema schema = getSchema();
        final Field<?> field = schema.getFields().get(fieldName);
        final Object defaultValue = field.getDefaultValue();
        if (defaultValue != null) {
            return defaultValue;
        }

        final Schema fieldDeclaredSchema = getContext().getSchema(field.getDeclaredSchemaId());
        final Schema baseSchema = getContext().getSchema(field.getDeclaredSchemaId());

        // TODO: Traverse up the base schemas from this object's schema through
        // the list of schemas between it and the field's declared schema.
        // This is not the same as going over all of the base schemas. Nor is it
        // necessary to go over all base schemas and their ancestors. Again, the
        // schemas that need to be checked range from this object's schema to
        // the fields delcared schema (only).

        /*
         * List<URI> baseSchemaIds = schema.getBaseSchemaIds(); if
         * (baseSchemaIds != null && !baseSchemaIds.isEmpty()) { for (URI
         * baseSchemaId : baseSchemaIds) {
         * 
         * baseSchema = getContext().getSchema();
         * 
         * }
         * 
         * }
         */
        return defaultValue;
    }

    public Link getLink(URI linkRelationId) {

        Link link;

        if (_LinkMap == null) {
            initLinks();
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

    public ObservableMap<URI, Link> getLinkMap() {
        return _LinkMap;
    }

    public ResourceTemplate getResourceTemplate() {
        return getContext().getResourceTemplate(getResourceTemplateId());
    }

    public URI getResourceTemplateId() {
        return _ResourceTemplateId;
    }

    public Schema getSchema() {
        return getContext().getSchema(getSchemaId());
    }

    public URI getSchemaId() {
        return _SchemaId;
    }

    private void initFields() {
        _FieldMap = new ObservableMap<String, Object>(new HashMap<String, Object>());
        _FieldMapEventListener = new FieldMapEventListener();
        _FieldMap.addEventListener(_FieldMapEventListener);
    }

    private void initLinks() {
        _LinkMap = new ObservableMap<URI, Link>(new HashMap<URI, Link>());
    };

    public boolean isDocroot() {
        final ResourceTemplate resourceTemplate = getResourceTemplate();
        return ((resourceTemplate != null) && (resourceTemplate.getParentResourceTemplateId() == null));
    }

    public boolean isReadOnly() {
        return _ReadOnly;
    }

    public void removeEventListener(WrmlObjectEventListener listener) {
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

    public Object setFieldValue(String fieldName, Object fieldValue) {

        if (_FieldMap == null) {
            initFields();
        }

        return _FieldMap.put(fieldName, fieldValue);
    }

    public void setReadOnly(boolean readOnly) {
        _ReadOnly = readOnly;
    }

    
    private void fireConstraintViolated(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener<?>> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (FieldEventListener<?> fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }
            
            fieldEventListener.constraintViolated(event);
        }
    }
    
    private void fireValueChanged(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener<?>> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (FieldEventListener<?> fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }
            
            fieldEventListener.valueChanged(event);
        }
        
    }        
    
    private void fireValueInitialized(final FieldEvent event) {
        final String fieldName = event.getFieldName();
        final List<FieldEventListener<?>> fieldEventListenerList = _FieldEventListeners.get(fieldName);
        for (FieldEventListener<?> fieldEventListener : fieldEventListenerList) {
            if (event.isCancelable() && event.isCancelled()) {
                break;
            }
            
            fieldEventListener.valueInitialized(event);
        }
    }

     

    
}
