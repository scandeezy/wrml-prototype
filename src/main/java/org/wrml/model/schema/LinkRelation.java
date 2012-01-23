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

package org.wrml.model.schema;

import org.wrml.model.Descriptive;
import org.wrml.model.Document;
import org.wrml.model.Named;
import org.wrml.model.Titled;
import org.wrml.model.Versioned;
import org.wrml.util.observable.ObservableList;
import org.wrml.www.MediaType;
import org.wrml.www.http.Method;

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
public interface LinkRelation extends Named, Titled, Versioned, Descriptive, Document {

    public Method getMethod();

    public ObservableList<MediaType> getRequestMediaTypes();

    public ObservableList<MediaType> getResponseMediaTypes();

    public Method setMethod(Method method);
}
