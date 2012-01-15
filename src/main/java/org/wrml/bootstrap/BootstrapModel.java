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

package org.wrml.bootstrap;

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wrml.Model;
import org.wrml.runtime.Context;
import org.wrml.runtime.HyperLink;
import org.wrml.util.MediaType;
import org.wrml.util.ReflectiveFieldMap;

/**
 * A bootstrap proxy-based implementation of WRML's Model interface.
 * 
 * @param <M>
 *            The Model type to proxy.
 */
public class BootstrapModel<M extends Model> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final transient Context _Context;
    private final Class<M> _StaticType;
    private M _StaticInterface;
    private final List<URI> _EmbeddedLinkRelationIds;

    public BootstrapModel(final Context context, final Class<M> staticType) {
        this(context, staticType, null);
    }

    public BootstrapModel(final Context context, final Class<M> staticType, final List<URI> embeddedLinkRelationIds) {
        _Context = context;
        _StaticType = staticType;
        _EmbeddedLinkRelationIds = embeddedLinkRelationIds;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public final Context getContext() {
        return _Context;
    }

    public final List<URI> getEmbeddedLinkRelationIds() {
        return _EmbeddedLinkRelationIds;
    }

    @SuppressWarnings("unchecked")
    public final Model getStaticInterface() {

        try {
            if (Proxy.isProxyClass(this.getClass())) {
                return (Model) this;
            }

            if (_StaticInterface == null) {

                final Class<M> staticType = getStaticType();
                final Context context = getContext();

                final MediaType mediaType = context.getMediaTypeToClassTransformer().bToA(staticType);

                final List<URI> embeddedLinkRelationIds = getEmbeddedLinkRelationIds();

                final ReflectiveFieldMap<M> fieldMap = new ReflectiveFieldMap<M>(this, staticType);

                final Map<URI, HyperLink> linkMap = new HashMap<URI, HyperLink>();

                _StaticInterface = (M) context.instantiateModel(mediaType, embeddedLinkRelationIds, fieldMap, linkMap)
                        .getStaticInterface();

            }
        }
        catch (Throwable bootstrapProblem) {

            System.err.println(bootstrapProblem.getMessage());
            bootstrapProblem.printStackTrace();
        }

        return _StaticInterface;
    }

    public final Class<M> getStaticType() {
        return _StaticType;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + " { staticType : \"" + _StaticType + "\", hashCode : " + hashCode() + "}";
    }

}
