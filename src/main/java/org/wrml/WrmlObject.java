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

package org.wrml;

import java.io.Serializable;
import java.net.URI;

import org.wrml.api.ResourceTemplate;
import org.wrml.schema.Field;
import org.wrml.schema.Schema;
import org.wrml.util.ObservableList;
import org.wrml.util.ObservableMap;

/**
 * The base interface for all web resource schema instances.
 */
public interface WrmlObject extends Serializable {

    public void addEventListener(WrmlObjectEventListener listener);

    public void addFieldEventListener(String fieldName, FieldEventListener<?> listener);

    public Context getContext();

    public ObservableMap<String, Object> getFieldMap();

    public Object getFieldValue(String fieldName);

    public Link getLink(URI linkRelationId);

    public ObservableMap<URI, Link> getLinkMap();

    public ResourceTemplate getResourceTemplate();

    public URI getResourceTemplateId();

    public Schema getSchema();

    public URI getSchemaId();

    public void removeEventListener(WrmlObjectEventListener listener);

    public void removeFieldEventListener(String fieldName, FieldEventListener<?> listener);

    public Object setFieldValue(String fieldName, Object fieldValue);

    public boolean isDocroot();

    public boolean isReadOnly();

}
