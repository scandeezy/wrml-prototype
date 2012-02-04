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

package org.wrml.core.model.schema;

import org.wrml.core.model.Descriptive;
import org.wrml.core.model.Document;
import org.wrml.core.model.Named;
import org.wrml.core.model.Titled;
import org.wrml.core.model.Versioned;
import org.wrml.core.util.observable.ObservableList;
import org.wrml.core.www.MediaType;
import org.wrml.core.www.http.Method;

/**
 * Link relations are a big deal in WRML. They are a first class entity with
 * supporting REST API to manage and deliver them. An applications use of link
 * relations is like a "shared vocabulary" of resource relationships - the named
 * "actions" - understood by both the client and server of an application.
 * 
 * Their uniqueness is used liberally in WRML Java application's as they form
 * the key between the object model's data and metadata layers.
 * 
 * A unique, yet highly generic LinkRelation may be shared by many different
 * instances of an A---->B relationship. For example a "Person" schema and a
 * "Node" schema may both want to link to their "parent".
 * 
 * The re-use of the same LinkRelation is common within the same domain, like
 * building a Web site about Movies that uses many different APIs that share the
 * same domain-specific language (DSL). Common, domain-specific-relationships
 * like "genre" and "cast" might appear in the application-specific APIs that
 * directly power a set of Web apps.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public interface LinkRelation extends Named, Titled, Versioned, Descriptive, Document {

    /**
     * Gets the HTTP {@link Method} to invoke on the HREF associated with this
     * LinkRelation.
     * 
     * @return the HTTP Method used to "click through" this LinkRelation.
     */
    public Method getMethod();

    /**
     * The list of {@link MediaType}s that this LinkRelation is known to
     * accept as "Content Types" in the *request* message's body.
     * 
     * Given the nature of HTTP's methods, this list only applies to
     * LinkRelation's that describe a PUT or POST-based relationship.
     * LinkRelation instances using other methods will return <code>null</code>.
     * 
     * Highly reusable LinkRelations may be shared across APIs and thus they are
     * unlikely to know about all of the different application's
     * {@link MediaType}s, and in such cases this method will return
     * <code>null</code>.
     * 
     * @return the list of content types allowed in request bodies associated
     *         with clicks using this LinkRelation. Returns <code>null</code> in
     *         some cases (see above).
     */
    public ObservableList<MediaType> getRequestTypes();

    /**
     * The list of {@link MediaType}s that this LinkRelation is known to
     * return as "Content Types" in the *response* message's body.
     * 
     * Given the nature of HTTP, this list only applies to LinkRelation's that
     * describe an interaction which is expected to result in a response message
     * containing some content in its body. LinkRelation instances which
     * describe interactions that are expected to result in response messages
     * with zero byte bodies will return <code>null</code>.
     * 
     * Highly reusable LinkRelations may be shared across APIs and thus they are
     * unlikely to know about all of the different application's
     * {@link MediaType}s, and in such cases this method will return
     * <code>null</code>.
     * 
     * @return the list of content types allowed in request bodies associated
     *         with clicks using this LinkRelation. Returns <code>null</code> in
     *         some cases (see above).
     */
    public ObservableList<MediaType> getResponseTypes();

    /**
     * Sets the HTTP {@link Method} field, which is described in
     * {@link LinkRelation#getMethod()}
     * 
     * @param method
     *            the {@link Method} to associate with this LinkRelation.
     * 
     * @return the {@link Method} that used to be associated, before the caller
     *         called.
     */
    public Method setMethod(Method method);
}
