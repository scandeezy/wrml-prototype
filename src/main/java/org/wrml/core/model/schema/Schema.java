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

import java.net.URI;

import org.wrml.core.model.Descriptive;
import org.wrml.core.model.Document;
import org.wrml.core.model.Named;
import org.wrml.core.model.Versioned;
import org.wrml.core.util.observable.ObservableList;

/**
 * Schemas are one of WRML's key ideas. Like Java's classes, schemas add
 * type information to the representations (of various formats) that are traded
 * back and forth between programs on the Web.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public interface Schema extends Named, Versioned, Descriptive, Constrainable<Schema>, Document {

    /**
     * Get the ids of this Schema's base Schemas. Schemas are fundamentally
     * interface-oriented, and as such they can safely, meaning without crazy
     * confusion, support multiple inheritance. This list contains the ids of
     * the Schemas that this Schema *directly* extends, or, in other words, it
     * is a shallow (depth one) list of inherited Schemas.
     * 
     * @return the list of Schemas that this Schema directly "extends".
     */
    public ObservableList<URI> getBaseSchemaIds();

    /**
     * The list of {@link Field}s defined for this Schema. This list does not
     * include the Fields of any base Schemas, unless they were re-defined
     * "locally" by this Schema.
     * 
     * Schemas re-define (a.k.a. override) Fields that a base Schema had already
     * defined, based on an exact same *field name* collision. In such cases,
     * the
     * Field's {@link Type} must remain the same in the overriding sub-Schema's
     * Field of the same name.
     * 
     * This "rule" mirrors Java's rule about a subclass overriding/overloading
     * a method must keep its return type consistent. Pretty standard stuff.
     * 
     * @return the list of Fields that are defined (locally) by this Schema.
     */
    public ObservableList<Field> getFields();

    /**
     * The list of {@link Link}s defined for this Schema. This list does not
     * include the Links of any base Schemas, unless they were re-defined
     * "locally" by this Schema.
     * 
     * Schemas re-define (a.k.a. override) Links that a base Schema had already
     * defined, based on an exact same *link relation id* collision.
     * 
     * @return the list of Link that are defined (locally) by this Schema.
     */
    public ObservableList<Link> getLinks();

}
