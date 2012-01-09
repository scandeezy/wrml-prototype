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

import java.io.Serializable;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wrml.Model;

public class StaticModel<M extends Model> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final transient Context _Context;
    private final Class<M> _StaticType;
    private M _StaticInterface;
    private final List<URI> _EmbeddedLinkRelationIds;

    public StaticModel(final Context context, final Class<M> staticType) {
        this(context, staticType, null);
    }
    
    public StaticModel(final Context context, final Class<M> staticType, final List<URI> embeddedLinkRelationIds) {
        _Context = context;
        _StaticType = staticType;
        _EmbeddedLinkRelationIds = embeddedLinkRelationIds;
    }

    public final Context getContext() {
        return _Context;
    }

    public final List<URI> getEmbeddedLinkRelationIds() {
        return _EmbeddedLinkRelationIds;
    }

    @SuppressWarnings("unchecked")
    public final M getStaticInterface() {

        if (Proxy.isProxyClass(this.getClass())) {
            return (M) this;
        }

        if (_StaticInterface == null) {

            Class<M> staticType = getStaticType();
            Context context = getContext();

            URI schemaId = context.getSchemaIdToClassTransformer().bToA(staticType);

            List<URI> embeddedLinkRelationIds = getEmbeddedLinkRelationIds();

            StaticFieldMap<M> fieldMap = new StaticFieldMap<M>(this, staticType);

            Map<URI, HyperLink> linkMap = new HashMap<URI, HyperLink>();

            _StaticInterface = (M) context.instantiateModel(schemaId, embeddedLinkRelationIds, fieldMap, linkMap)
                    .getStaticInterface();

        }

        return _StaticInterface;
    }

    public final Class<M> getStaticType() {
        return _StaticType;
    }

}
