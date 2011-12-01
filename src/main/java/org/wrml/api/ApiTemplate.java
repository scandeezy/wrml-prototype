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

package org.wrml.api;

import java.net.URI;

import org.wrml.util.Identifiable;
import org.wrml.util.ObservableList;

public class ApiTemplate extends Identifiable<URI> {

    private static final long serialVersionUID = 468618916602653649L;

    private String _Name;
    private long _Version;
    private String _Description;
    private String _Title;
    private ObservableList<URI> _RootResourceTemplateIds;

    public ApiTemplate() {
    }

    public ApiTemplate(String name) {
        this(name, null);
    }

    public ApiTemplate(String name, String description) {
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

    public ObservableList<URI> getRootResourceTemplateIds() {
        return _RootResourceTemplateIds;
    }

    public String getTitle() {
        return _Title;
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

    public void setRootResourceTemplateIds(ObservableList<URI> rootResourceTemplateIds) {
        _RootResourceTemplateIds = rootResourceTemplateIds;
    }

    public void setTitle(String title) {
        _Title = title;
    }

    public void setVersion(long version) {
        _Version = version;
    }

}
