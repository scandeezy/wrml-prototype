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

import java.net.URI;

import org.wrml.model.Descriptive;
import org.wrml.model.Document;
import org.wrml.model.Named;
import org.wrml.model.Versioned;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;

/**
 * Schemas are one of WRML's main ideas. Like Java's generics, schemas add
 * parameterized type information to the representations (of various formats)
 * that are traded back and forth between programs on the Web.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
// Generated from a Web Resource Schema
public interface Schema extends Named, Versioned, Descriptive, Document {

    // Added to Field
    //     Name: name 
    //     Constraints: TextSyntax - Mixed-Upper Case

    public ObservableList<URI> getBaseSchemaIds();

    /**
     * The ability to add Constraints to Schemas can have serve a few different
     * functions.
     * 
     * 1. SchemaConstraints can be added to generate parameterized types. For
     * example a SchemaConstraint named "T" could be used in a WRML schema like
     * "Container" to generate "Container<T extends Model>" in Java. The use of
     * one or more named SchemaConstraints at the Schema level has the effect of
     * creating named schema "slots" whenever these types are referenced in WRML
     * "builder" GUI tools.
     * 
     * @return
     */
    public ObservableList<URI> getConstraintIds();

    // Generated from Field
    //     Name: fields 
    //     Value: Map[ Key=Text, Value=Schema[Field[?]] ]
    public ObservableMap<String, Field> getFields();

    // Generated from Field
    //     Name: links 
    //     Value: Map[ Key=Text[URI], Value=Schema[Link]] ]
    //     Constraints: KeyType - Text[URI], ValueType - Schema[Link]
    public ObservableMap<URI, Link> getLinks();

}
