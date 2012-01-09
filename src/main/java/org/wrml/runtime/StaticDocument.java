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

import java.net.URI;
import java.util.List;

import org.wrml.model.Document;

public class StaticDocument<D extends Document> extends StaticModel<D> {

    private static final long serialVersionUID = 1L;

    private URI _Id;
    private String _Name;
    private String _Description;
    private Long _SecondsToLive;
    private boolean _ReadOnly;
    private String _Etag;

    public StaticDocument(Context context, Class<D> staticType, List<URI> embeddedLinkRelationIds) {
        super(context, staticType, embeddedLinkRelationIds);
        
        // TODO: Need to set this in the actual Document schema
        _SecondsToLive = Long.MAX_VALUE;
    }

    public void delete() {
        getStaticInterface().die();
    }

    public String getDescription() {
        return _Description;
    }

    public String getEtag() {
        String etag = _Etag;
        return (etag != null) ? etag : String.valueOf(getVersion());
    }

    public URI getId() {
        return _Id;
    }

    public String getName() {
        return _Name;
    }

    public Long getSecondsToLive() {
        return _SecondsToLive;
    }

    public D getSelf() {
        return getStaticInterface();
    }

    public long getVersion() {
        return serialVersionUID;
    }

    public boolean isReadOnly() {
        return _ReadOnly;
    }

    public String setDescription(String description) {
        String oldDescription = _Description;
        _Description = description;
        return oldDescription;
    }

    public String setEtag(String etag) {
        String oldEtag = _Etag;
        _Etag = etag;
        return oldEtag;
    }

    public URI setId(URI id) {
        URI oldId = _Id;
        _Id = id;
        return oldId;
    }

    public String setName(String name) {
        String oldName = _Name;
        _Name = name;
        return oldName;
    }

    public boolean setReadOnly(boolean readOnly) {
        boolean oldReadOnly = _ReadOnly;
        _ReadOnly = readOnly;
        return oldReadOnly;
    }

    public Long setSecondsToLive(Long secondsToLive) {
        Long oldSecondsToLive = _SecondsToLive;
        _SecondsToLive = secondsToLive;
        return oldSecondsToLive;
    }

}
