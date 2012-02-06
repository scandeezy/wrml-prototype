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

import java.net.URI;

import org.wrml.core.model.Document;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.ModelGraph;

/**
 * A bootstrap proxy-based implementation of WRML's Document schema.
 * 
 * @param <D>
 *            The Document type to proxy.
 */
public class BootstrapDocument<D extends Document> extends BootstrapModel<D> {

    private static final long serialVersionUID = 1L;

    private String _Etag;
    private URI _Id;
    private boolean _ReadOnly;
    private Long _SecondsToLive;

    public BootstrapDocument(Context context, Class<D> staticType, ModelGraph modelGraph, URI id) {
        super(context, staticType, modelGraph);

        setId(id);

        // TODO: Need to set this in the actual Document schema
        setSecondsToLive(Long.MAX_VALUE);
    }

    public final void delete() {
        getStaticInterface().free();
    }

    public final String getEtag() {
        return _Etag;
    }

    public final URI getId() {
        return _Id;
    }

    public final Long getSecondsToLive() {
        return _SecondsToLive;
    }

    @SuppressWarnings("unchecked")
    public final D getSelf() {
        return (D) getStaticInterface();
    }

    public final boolean isReadOnly() {
        return _ReadOnly;
    }

    public final String setEtag(String etag) {
        final String oldEtag = _Etag;
        _Etag = etag;
        return oldEtag;
    }

    public final URI setId(URI id) {
        final URI oldId = _Id;
        _Id = id;
        return oldId;
    }

    public final boolean setReadOnly(boolean readOnly) {
        final boolean oldReadOnly = _ReadOnly;
        _ReadOnly = readOnly;
        return oldReadOnly;
    }

    public final Long setSecondsToLive(Long secondsToLive) {
        final Long oldSecondsToLive = _SecondsToLive;
        _SecondsToLive = secondsToLive;
        return oldSecondsToLive;
    }

}
