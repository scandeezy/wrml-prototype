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

package org.wrml.communication;

import java.net.URI;

import org.wrml.util.Identifiable;
import org.wrml.util.ObservableList;

/**
 * Link relations are a big deal in WRML. They are a first class entity with
 * supporting REST API to manage and deliver them. An applications use of link
 * relations is like a "shared vocabulary" of resource relationships - the named
 * "actions" - understood by both the client and server of an application.
 * 
 * Their uniqueness is used liberally in WRML Java application's as they form
 * the key between the object model's data and metadata layers.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public final class LinkRelation extends Identifiable<URI> {

    private static final long serialVersionUID = 2465664864460075452L;
    
    private String _Name;
    private long _Version;
    private String _Description;
    private String _Title;

    private ObservableList<MediaType> _RequestMediaTypes;
    private ObservableList<MediaType> _ResponseMediaTypes;

    private Method _Method;

    public String getDescription() {
        return _Description;
    }

    public Method getMethod() {
        return _Method;
    }

    public String getName() {
        return _Name;
    }

    public ObservableList<MediaType> getRequestMediaTypes() {
        return _RequestMediaTypes;
    }

    public ObservableList<MediaType> getResponseMediaTypes() {
        return _ResponseMediaTypes;
    }

    public String getTitle() {
        return _Title;
    }

    public void setDescription(String description) {
        _Description = description;
    }

    public void setMethod(Method method) {
        _Method = method;
    }

    public void setName(String name) {
        _Name = name;
    }

    public void setRequestMediaTypes(ObservableList<MediaType> requestMediaTypes) {
        _RequestMediaTypes = requestMediaTypes;
    }

    public void setResponseMediaTypes(ObservableList<MediaType> responseMediaTypes) {
        _ResponseMediaTypes = responseMediaTypes;
    }

    public void setTitle(String title) {
        _Title = title;
    }

    public long getVersion() {
        return _Version;
    }

    public void setVersion(long version) {
        _Version = version;
    }

}
