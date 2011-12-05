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
package org.wrml.model.restapi;

import java.net.URI;

import org.wrml.model.Collection;
import org.wrml.model.Descriptive;
import org.wrml.model.Document;
import org.wrml.model.Titled;
import org.wrml.model.Named;
import org.wrml.model.Versioned;

// Generated from a Web Resource Schema
public interface RestApiTemplate extends Named, Titled, Versioned, Descriptive, Document {

    // Added to Field
    //     Name: name 
    //     Constraints: TextSyntax - Mixed-Upper Case

    // Generated from Field
    //     Name: rootResourceTemplatesId
    //     Value: Text[URI]
    //     Constraints: TextSyntax - URI
    //     Flags: ReadOnly
    public URI getRootResourceTemplatesId();

    // Generated from Link
    //     Relation: rootResourceTemplates
    //         Methods: GET 
    //         ResponseSchema: Collection[ResourceTemplate]
    //     EnabledFormula: rootResourceTemplatesId != null
    //     DestinationUriTemplate: {rootResourceTemplatesId} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["rootResourceTemplatesId"]]
    //     Href: <rootResourceTemplatesId>
    public Collection<ResourceTemplate> getRootResourceTemplates();

}
