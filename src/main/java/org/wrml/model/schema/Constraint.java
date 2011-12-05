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
import org.wrml.model.Titled;
import org.wrml.model.Named;
import org.wrml.model.Versioned;
import org.wrml.util.ObservableList;
import org.wrml.util.Validator;

/**
 * A field constraint's metadata and pointers to "executable" Validators.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 * 
 * @param <T>
 *            The field type
 */
// Generated from a Web Resource Schema
public interface Constraint<T> extends Named, Titled, Versioned, Descriptive, Document {

    // Added to Field
    //     Name: name 
    //     Constraints: TextSyntax - Mixed-Upper Case

    /*
     * TODO: Note that the getValidators() method may need to be "magically"
     * generated to enable code-on-demand.
     * 
     * WRML's Java framework should have a generalized design for how to
     * incorporate hooks to non-model (executable) Java code. Everything from
     * how it is packaged (Jar, Manifest file, etc) and design a REST API-based
     * exchange hidden behind WRML's Service JAVA API.
     * 
     * Possibly need to add some kind of marker to the metadata to indicate that
     * there is downloadable code and then the interface generation code can
     * download and inspect the interface (within the Jar) in order to get the
     * method/type signatures right.
     */
    public ObservableList<Validator<T>> getValidators();
}
