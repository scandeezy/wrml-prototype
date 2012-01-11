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

package org.wrml.util.transformer;

import java.util.HashMap;

import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;

public class CachingTransformer<A, B, T extends Transformer<A, B>> extends DelegatingTransformer<A, B, T> {

    private final ObservableMap<A, B> _AbCache;
    private final ObservableMap<B, A> _BaCache;

    public CachingTransformer(T delegate) {
        this(delegate, Observables.observableMap(new HashMap<A, B>()), Observables.observableMap(new HashMap<B, A>()));
    }

    public CachingTransformer(T delegate, ObservableMap<A, B> abCache, ObservableMap<B, A> baCache) {
        super(delegate);
        _AbCache = abCache;
        _BaCache = baCache;
    }

    @Override
    public B aToB(A aValue) {

        if (_AbCache != null) {
            if (_AbCache.containsKey(aValue)) {
                return _AbCache.get(aValue);
            }
        }

        final B bValue = super.aToB(aValue);

        if (_AbCache != null) {
            _AbCache.put(aValue, bValue);
        }

        return bValue;
    }

    @Override
    public A bToA(B bValue) {

        if (_BaCache != null) {
            if (_BaCache.containsKey(bValue)) {
                return _BaCache.get(bValue);
            }
        }

        final A aValue = super.bToA(bValue);

        if (_BaCache != null) {
            _BaCache.put(bValue, aValue);
        }

        return aValue;
    }

}
