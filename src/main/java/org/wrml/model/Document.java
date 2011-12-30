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

package org.wrml.model;

import java.net.URI;

import org.wrml.Model;


/**
 * A resource archetype used to model a singular concept.
 */
// Generated from a Web Resource Schema
public interface Document extends Model {

    // Generated from Link
    //     Relation: delete
    //         Methods: DELETE 
    //         ResponseSchema: Document
    //     EnabledFormula: id != null
    //     DestinationUriTemplate: {id} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["id"]]
    //     Href: <id>
    public void delete();

    public String getEtag();

    public URI getId();

    public URI setId(URI id);

    // Generated from Link
    //     Relation: metadata
    //         Methods: HEAD 
    //         ResponseSchema: DocumentMetadata
    //     EnabledFormula: id != null
    //     DestinationUriTemplate: {id} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["id"]]
    //     Href: <id>
    public DocumentMetadata getMetadata();

    // Generated from Link
    //     Relation: options
    //         Methods: OPTIONS 
    //         ResponseSchema: DocumentOptions
    //     EnabledFormula: id != null
    //     DestinationUriTemplate: {id} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["id"]]
    //     Href: <id>
    public DocumentOptions getOptions();

    // Generated from Link
    //     Relation: parent
    //         Methods: GET 
    //         ResponseSchema: Document
    //     EnabledFormula: parentId != null
    //     DestinationUriTemplate: {parentId} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["parentId"]]
    //     Href: <parentId>
    public Document getParent();

    // Generated from Field
    //     Name: parentId 
    //     Flags: ReadOnly
    public URI getParentId();

    public Long getSecondsToLive();

    // Generated from Link
    //     Relation: self
    //         Methods: GET 
    //         ResponseSchema: Document
    //     EnabledFormula: id != null
    //     DestinationUriTemplate: {id} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["id"]]
    //     Href: <id>
    public Document getSelf();

    // Generated from Field
    //     Name: readOnly 
    public boolean isReadOnly();

    public String setEtag(String etag);

    public boolean setReadOnly(boolean readOnly);

    public Long setSecondsToLive(Long secondsToLive);

    // Generated from Link
    //     Relation: update
    //         Methods: PUT
    //         ResponseSchema: Document
    //     EnabledFormula: id != null
    //     DestinationUriTemplate: {id} 
    //     DestinationUriTemplateParameters: [FieldUriTemplateParameter["id"]]
    //     Href: <id>
    public Document update();

}
