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

package org.wrml.core.runtime.bootstrap;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.wrml.core.Hyperlink;
import org.wrml.core.Model;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.ModelGraph;
import org.wrml.core.runtime.ReflectiveFieldMap;
import org.wrml.core.runtime.RuntimeObject;
import org.wrml.core.runtime.TypeSystem;

/**
 * A bootstrap proxy-based implementation of WRML's Model interface.
 * 
 * @param <M>
 *            The Model type to proxy.
 */
public class BootstrapModel<M extends Model> extends RuntimeObject implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Class<M> _StaticInterfaceClass;
    private M _StaticInterface;
    private transient final ModelGraph _ModelGraph;

    public BootstrapModel(final Context context, final Class<M> staticInterfaceClass, ModelGraph modelGraph) {
        super(context);
        _StaticInterfaceClass = staticInterfaceClass;
        _ModelGraph = modelGraph;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    // TODO: Does the bootstrap model need links?
    /*
     * public ObservableMap<URI, Hyperlink> getHyperLinks() {
     * return _HyperLinks;
     * }
     */

    public ModelGraph getModelGraph() {
        return _ModelGraph;
    }

    public java.lang.reflect.Type getNativeType() {
        return _StaticInterfaceClass;
    }

    public java.lang.reflect.Type[] getNativeTypeParameters() {
        final Context context = getContext();
        final TypeSystem typeSystem = context.getTypeSystem();
        return typeSystem.getNativeTypeParameters(getNativeType());
    }

    @SuppressWarnings("unchecked")
    public final Model getStaticInterface() {

        if (Proxy.isProxyClass(this.getClass())) {
            return (Model) this;
        }

        if (_StaticInterface == null) {

            final Context context = getContext();

            final ReflectiveFieldMap<M> fieldMap = new ReflectiveFieldMap<M>(context, this, _StaticInterfaceClass);

            final Map<URI, Hyperlink> linkMap = new HashMap<URI, Hyperlink>();

            final ModelGraph modelGraph = getModelGraph();
            final Model dynamicModel = context.instantiateModel(_StaticInterfaceClass, modelGraph, fieldMap, linkMap);
            _StaticInterface = (M) dynamicModel.getStaticInterface();
        }

        return _StaticInterface;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + " { staticType : \"" + _StaticInterfaceClass + "\", hashCode : " + hashCode()
                + "}";
    }

}
