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

import org.wrml.model.CodeOnDemand;
import org.wrml.model.Descriptive;
import org.wrml.model.Document;
import org.wrml.model.Named;
import org.wrml.model.Titled;
import org.wrml.model.Versioned;
import org.wrml.util.observable.ObservableList;

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
public interface Constraint extends Named, Titled, Versioned, Descriptive, Document {

    // Added to Field
    //     Name: name 
    //     Constraints: TextSyntax - Mixed-Upper Case

    /**
     * Get the (optional) code-on-demand validators
     * @return
     */
    public ObservableList<CodeOnDemand> getValidators();
}
