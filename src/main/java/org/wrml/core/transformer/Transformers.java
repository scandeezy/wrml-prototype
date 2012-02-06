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

package org.wrml.core.transformer;

import java.util.HashMap;

import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.RuntimeObject;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.util.observable.Observables;

public final class Transformers<B> extends RuntimeObject {

    private final ObservableMap<Class<?>, Transformer<?, B>> _ToBTransformers;

    public Transformers(Context context) {
        super(context);

        _ToBTransformers = Observables.observableMap(new HashMap<Class<?>, Transformer<?, B>>());
    }

    @SuppressWarnings("unchecked")
    public final <A> Transformer<A, B> getTransformer(Class<A> aType) {
        return (Transformer<A, B>) _ToBTransformers.get(aType);
    }

    public final <A> void setTransformer(Class<A> aType, Transformer<A, B> transformer) {
        _ToBTransformers.put(aType, transformer);
    }

}
