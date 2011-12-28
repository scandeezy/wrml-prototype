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

package org.wrml.service;

import java.util.List;

import org.wrml.Context;
import org.wrml.util.DelegatingObservableList;

/**
 * TODO: Make this a useful base class for various types of multiproxies (load
 * balancers, aggregators, and other cluster-oriented operators).
 */
public abstract class MultiProxyService extends AbstractService implements DelegatingService<List<Service>> {

    private final DelegatingObservableList<Service> _Services;

    public MultiProxyService(Context context, List<Service> services) {
        super(context);
        _Services = new DelegatingObservableList<Service>(services);
    }

    public DelegatingObservableList<Service> getServices() {
        return _Services;
    }

    public List<Service> getDelegate() {
        return _Services.getDelegate();
    }

}
