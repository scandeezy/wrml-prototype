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

package org.wrml.core.model;

import java.net.URI;

import org.wrml.core.Model;

/**
 * A resource archetype used to model a singular concept.
 */
// Generated from a Web Resource Schema
public interface Document extends Model {

    public void delete();

    public String getEtag();

    public URI getId();

    public DocumentMetadata getMetadata();

    public DocumentOptions getOptions();

    public Document getParent();

    public Long getSecondsToLive();

    public Document getSelf();

    public boolean isReadOnly();

    public String setEtag(String etag);

    public URI setId(URI id);

    public boolean setReadOnly(boolean readOnly);

    public Long setSecondsToLive(Long secondsToLive);

    public Document update();

}
