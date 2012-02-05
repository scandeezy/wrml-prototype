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
import java.util.Map;
import java.util.TreeMap;

import org.wrml.core.Hyperlink;
import org.wrml.core.Model;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.util.observable.Observables;

/**
 * The ModelHeap is the uber cache of all models. It "owns" all of the
 * {@link Model} caching in order to provide a convenient hook
 * to learn about the comings and goings of models in and out of the runtime.
 * 
 * The ModelHeap is divided into {@link ModelHeapShard}s which act as
 * monomorphic Model "tables", with each shard holding all of the Models of a
 * single native {@link Type}. Thus the Type acts as the "top-level" key to the
 * shards themselves.
 * 
 * The ModelHeap must be used to create new models so that their "identity" can
 * be properly managed by the framework. The runtime works with the ModelHeap to
 * ensure that only one Model instance (of a given type) will be associated with
 * a given identity (URI). This is a key component of the MVC framework used by
 * the runtime and offered to apps.
 * 
 * The details get a bit hairy but the basic principle is that this class acts
 * as the "storage of record" for Models so that no matter how many times (and
 * ways) a Model is referenced at runtime, it can be consistently updated and
 * viewed without duplicated effort. To ModelHeap helps accomplish this goal, by
 * providing an easy and logical way to access a Model that is "alive" in our
 * local runtime, in the area that most programming platforms might call
 * "the heap".
 */
public final class ModelHeap extends RuntimeObject {

    private final ObservableMap<Type, ModelHeapShard> _Shards;

    public ModelHeap(Context context) {
        super(context);
        _Shards = Observables.observableMap(new HashMap<Type, ModelHeapShard>());
    }

    public ModelHeapShard getShard(final Type nativeType) {

        ModelHeapShard shard;
        if (!_Shards.containsKey(nativeType)) {
            shard = new ModelHeapShard(getContext(), nativeType);
            _Shards.put(nativeType, shard);
        }
        else {
            shard = _Shards.get(nativeType);
        }

        return shard;
    }

    public ObservableMap<Type, ModelHeapShard> getShards() {
        return _Shards;
    }

    public Model newModel(final java.lang.reflect.Type nativeType, final ModelGraph modelGraph) {
        return newModel(nativeType, modelGraph, null, null);
    }

    public Model newModel(final java.lang.reflect.Type nativeType, final ModelGraph modelGraph, FieldMap fieldMap,
            Map<URI, Hyperlink> linkMap) {

        if (fieldMap == null) {
            // Build the default backing map for fields
            fieldMap = new ModelFieldMap(modelGraph.getContext(), new TreeMap<String, Object>());
        }

        if (linkMap == null) {
            // Build the default backing map for links
            linkMap = new HashMap<URI, Hyperlink>();
        }

        // Create a model new model 
        final RuntimeModel runtimeModel = new RuntimeModel(modelGraph.getContext(), nativeType, modelGraph, fieldMap,
                linkMap);

        if (fieldMap instanceof ModelFieldMap) {
            // Tie the knot
            ((ModelFieldMap) fieldMap).setModel(runtimeModel);
        }

        // Put everything in its right place
        final Model model = shardModel(runtimeModel);

        // Return the most appropriate model
        return model;
    }

    private Model shardModel(RuntimeModel model) {
        final ModelHeapShard shard = getShard(model.getNativeType());
        return shard.add(model);
    }

}
