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
import java.util.List;

import org.wrml.util.Identifiable;

/**
 * A field constraint's metadata and pointers to "executable" Validators.
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 * 
 * @param <T>
 *            The field type
 */
public final class Constraint<T> extends Identifiable<URI> {

    private static final long serialVersionUID = -5489178662777819721L;

    private String _Name;
    private long _Version;
    private String _Description;
    private String _Title;
    
    private List<Validator<T>> _Validators;

    public Constraint() {
    }

    public Constraint(String name) {
        this(name, null);
    }

    public Constraint(String name, String description) {
        setName(name);        
        setTitle(name);
        setDescription(description);
    }

    public String getDescription() {
        return _Description;
    }

    public String getName() {
        return _Name;
    }

    public String getTitle() {
        return _Title;
    }

    public List<Validator<T>> getValidators() {
        return _Validators;
    }

    public long getVersion() {
        return _Version;
    }

    public void setDescription(String description) {
        _Description = description;
    }

    public void setName(String name) {
        _Name = name;
    }

    public void setTitle(String title) {
        _Title = title;
    }

    public void setValidators(List<Validator<T>> validators) {
        _Validators = validators;
    }

    public void setVersion(long version) {
        _Version = version;
    }

}
