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

import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;

import org.wrml.core.Model;
import org.wrml.core.event.EventSource;
import org.wrml.core.runtime.bootstrap.FieldNames;
import org.wrml.core.runtime.event.FieldEvent;
import org.wrml.core.runtime.event.LinkEvent;
import org.wrml.core.runtime.event.ModelEvent;
import org.wrml.core.runtime.event.ModelEventListener;
import org.wrml.core.runtime.event.ModelHeapShardEvent;
import org.wrml.core.runtime.event.ModelHeapShardEventListener;
import org.wrml.core.runtime.event.ModelHeapShardEventListener.ModelHeapShardEventName;
import org.wrml.core.service.CachingService;
import org.wrml.core.service.Service;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.util.observable.Observables;

public class ModelHeapShard extends EventSource<ModelHeapShardEventListener> implements Contextual {

    private final Context _Context;
    private final Type _Type;
    private final ModelEventListener _HeapModelEventListener;

    private ObservableMap<URI, Object> _HeapIdMap;
    private ObservableMap<Integer, Model> _HashCodeMap;

    public ModelHeapShard(Context context, Type type) {
        super(ModelHeapShardEventListener.class);
        _Context = context;
        _Type = type;
        _HeapModelEventListener = new HeapModelEventListener();
    }

    public Model add(RuntimeModel model) {

        if (!getType().equals(model.getNativeType())) {
            throw new IllegalArgumentException();
        }

        final ObservableMap<Integer, Model> hashCodeMap = getHashCodeMap();
        hashCodeMap.put(model.hashCode(), model);

        final ModelHeapShardEvent event = new ModelHeapShardEvent(this);
        event.setModel(model);
        fireEvent(ModelHeapShardEventName.modelAdded, event);

        // Check to see if the model has an id set on it already
        final URI heapId = model.getHeapId();

        Model finalModel = model;
        if (heapId != null) {
            // Technically we just identified this model
            finalModel = modelIdentified(model, heapId, event);
        }

        finalModel.addEventListener(_HeapModelEventListener);
        return finalModel;
    }

    public Model free(final int hashCode) {
        final Model model = get(hashCode);

        if (model == null) {
            return null;
        }

        model.free();
        return model;
    }

    public Model free(final URI heapId) {

        final Model model = get(heapId);

        if (model == null) {
            return null;
        }

        model.free();
        return model;
    }

    public Model get(final int hashCode) {
        return getHashCodeMap().get(hashCode);
    }

    public Model get(final URI heapId) {
        return (Model) getHeapIdMap().get(heapId);
    }

    public Context getContext() {
        return _Context;
    }

    public Type getType() {
        return _Type;
    }

    public CachingService newModelCachingService(final Service originService) {
        final CachingService cachingService = new CachingService(originService.getContext(), originService,
                getHeapIdMap());

        // TODO: Wire up to the origin to help deal with model cache timeouts?

        return cachingService;
    }

    private ObservableMap<Integer, Model> getHashCodeMap() {
        if (_HashCodeMap == null) {
            _HashCodeMap = Observables.observableMap(new HashMap<Integer, Model>());
        }
        return _HashCodeMap;
    }

    private ObservableMap<URI, Object> getHeapIdMap() {
        if (_HeapIdMap == null) {
            _HeapIdMap = Observables.observableMap(new HashMap<URI, Object>());
        }
        return _HeapIdMap;
    };

    private Model modelIdentified(Model model, final URI id, final ModelHeapShardEvent event) {

        fireEvent(ModelHeapShardEventName.modelIdentified, event);

        // Check to see if the cache already contains the model with the same id.
        final Model cachedModel = get(id);
        if (cachedModel != null) {

            /*
             * Absorb the new model into the cached one and make sure that the
             * cached one replaces the new one in the model graph.
             */
            cachedModel.absorb(model);

            model = cachedModel;
        }
        else {
            getHeapIdMap().put(id, model);
        }

        return model;
    }

    private final class HeapModelEventListener implements ModelEventListener {

        public void onFieldValueChanged(FieldEvent fieldEvent) {
            final String idFieldName = FieldNames.Document.id.name();
            if (idFieldName.equals(fieldEvent.getFieldName())) {
                final RuntimeModel model = (RuntimeModel) fieldEvent.getModel().getDynamicInterface();
                final URI heapId = model.getHeapId();
                if (heapId != null) {
                    final ModelHeapShardEvent modelHeapShardEvent = new ModelHeapShardEvent(ModelHeapShard.this);
                    modelHeapShardEvent.setModel(model);
                    modelIdentified(model, heapId, modelHeapShardEvent);
                }
            }
        }

        public void onModelFieldConstraintViolated(FieldEvent event) {
            // TODO Auto-generated method stub

        }

        public void onModelFieldValueChanged(FieldEvent event) {
            // TODO Auto-generated method stub

        }

        public void onModelFieldValueInitialized(FieldEvent event) {
            // TODO Auto-generated method stub

        }

        public void onModelFreed(ModelEvent event) {
            // TODO Auto-generated method stub

        }

        public void onModelLinkClicked(LinkEvent event) {
            // TODO Auto-generated method stub

        }

        public void onModelLinkEnabledStateChanged(LinkEvent event) {
            // TODO Auto-generated method stub

        }

        public void onModelLinkHrefChanged(LinkEvent event) {
            // TODO Auto-generated method stub

        }

    }

}
