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

public class SimpleLinkFormula extends LinkFormula {

    private static final long serialVersionUID = 6132706987780922911L;

    private String _SourceCode;

    public SimpleLinkFormula(Schema schema, URI declaredSchemaId) {
        super(schema, declaredSchemaId);
    }

    public SimpleLinkFormula(Schema schema, URI declaredSchemaId, URI linkRelationId) {
        super(schema, declaredSchemaId, linkRelationId);
    }

    @Override
    public boolean execute(Model model) {
        // TODO
        return false;
    }

    @Override
    public ObservableList<String> getFieldOperands() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getSourceCode() {
        return _SourceCode;
    }

    public void setSourceCode(String sourceCode) {
        _SourceCode = sourceCode;
    }

}
