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

import org.wrml.util.Identifiable;
import org.wrml.util.ObservableList;
import org.wrml.util.ObservableMap;

/**
 * Schemas are one of WRML's main ideas. Like Java's generics, schemas add
 * parameterized type information to the representations (of various formats)
 * that are traded back and forth between programs on the Web.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public final class Schema extends Identifiable<URI> {

    private static final long serialVersionUID = -612621291768743601L;

    private String _Name;

    private long _Version;
    private ObservableList<URI> _BaseSchemaIds;
    private String _Description;

    private ObservableMap<String, Field<?>> _Fields;

    private ObservableMap<URI, LinkFormula> _LinkFormulas;

    public ObservableList<URI> getBaseSchemaIds() {
        return _BaseSchemaIds;
    }

    public String getDescription() {
        return _Description;
    }

    public ObservableMap<String, Field<?>> getFields() {
        return _Fields;
    }

    public ObservableMap<URI, LinkFormula> getLinkFormulas() {
        return _LinkFormulas;
    }

    public String getName() {
        return _Name;
    }

    public long getVersion() {
        return _Version;
    }

    public void setBaseSchemaIds(ObservableList<URI> baseSchemaIds) {
        _BaseSchemaIds = baseSchemaIds;
    }

    public void setDescription(String description) {
        _Description = description;
    }

    public void setFields(ObservableMap<String, Field<?>> fields) {
        _Fields = fields;
    }

    public void setLinkFormulas(ObservableMap<URI, LinkFormula> linkFormulas) {
        _LinkFormulas = linkFormulas;
    }

    public void setName(String name) {
        _Name = name;
    }

    public void setVersion(long version) {
        _Version = version;
    }

}
