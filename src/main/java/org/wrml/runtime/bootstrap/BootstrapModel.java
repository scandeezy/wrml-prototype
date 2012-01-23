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

package org.wrml.runtime.bootstrap;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.wrml.HyperLink;
import org.wrml.Model;
import org.wrml.runtime.Context;
import org.wrml.runtime.Contextual;
import org.wrml.runtime.ReflectiveFieldMap;
import org.wrml.runtime.TypeSystem;

/**
 * A bootstrap proxy-based implementation of WRML's Model interface.
 * 
 * @param <M>
 *            The Model type to proxy.
 */
public class BootstrapModel<M extends Model> extends Contextual implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Class<M> _StaticInterfaceClass;
    private M _StaticInterface;

    public BootstrapModel(final Context context, final Class<M> staticInterfaceClass) {
        super(context);
        _StaticInterfaceClass = staticInterfaceClass;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    // TODO: Does the bootstrap model need links?
    /*
     * public ObservableMap<URI, HyperLink> getHyperLinks() {
     * return _HyperLinks;
     * }
     */

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

            final Map<URI, HyperLink> linkMap = new HashMap<URI, HyperLink>();

            _StaticInterface = (M) context.instantiateModel(_StaticInterfaceClass, fieldMap, linkMap)
                    .getStaticInterface();

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
