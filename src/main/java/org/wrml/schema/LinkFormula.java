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

package org.wrml.schema;

import java.net.URI;

import org.wrml.Model;
import org.wrml.util.ObservableList;

/**
 * The metadata that represents a simple boolean formula that can be evaluated
 * to determine the state of a link.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public abstract class LinkFormula extends Member<URI> {


    private static final long serialVersionUID = 4545325983989682434L;
    
    private URI _LinkRelationId;

    public LinkFormula(Schema schema, URI declaredSchemaId) {
        super(schema, declaredSchemaId);
    }

    public LinkFormula(Schema schema, URI declaredSchemaId, URI linkRelationId) {
        super(schema, declaredSchemaId);
        setLinkRelationId(linkRelationId);
    }

    public abstract boolean execute(Model model);

    public abstract ObservableList<String> getFieldOperands();

    public URI getLinkRelationId() {
        return _LinkRelationId;
    }

    public void setLinkRelationId(URI linkRelationId) {
        _LinkRelationId = linkRelationId;
        setId(linkRelationId);
    }

}
