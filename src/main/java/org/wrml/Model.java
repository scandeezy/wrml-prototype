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
import java.util.List;
import java.util.Map;

import org.wrml.event.FieldEventListener;
import org.wrml.event.LinkEventListener;
import org.wrml.event.ModelEventListener;
import org.wrml.model.api.ResourceTemplate;
import org.wrml.model.schema.Schema;
import org.wrml.runtime.Context;
import org.wrml.util.MediaType;

/**
 * <p>
 * Model is the base interface of all Web resource schema instances. A "model"
 * in both the design-time context of data "modeling" and the runtime context of
 * a client-server "Model" View Controller (MVC) Web application.
 * </p>
 * 
 * <blockquote>
 * "You're a problem solver. You’re one of these people who would pick up a
 * rope that's gotten all tangled up and spend an entire day untangling it.
 * Because it's a challenge, because it defies your sense of order in the
 * universe, and because you can."
 * <blockquote>
 * 
 * <span>-- Delenn, Babylon 5</span>
 * 
 */
public interface Model extends Serializable {

    public void addEventListener(ModelEventListener listener);

    public void addFieldEventListener(String fieldName, FieldEventListener listener);

    public void addLinkEventListener(URI linkRelationId, LinkEventListener listener);

    public Object clickLink(URI rel, MediaType responseType, Object requestEntity, Map<String, String> hrefParams);

    /**
     * Die means that the model is gone but may not be deleted
     */
    public void die();

    public void extend(Model modelToExtend, Model... additionalModelsToExtend);

    public Context getContext();

    public Model getDynamicInterface();

    public List<URI> getEmbeddedLinkRelationIds();

    public <V> V getFieldValue(String fieldName);

    public ResourceTemplate getResourceTemplate();

    public URI getResourceTemplateId();

    public Schema getSchema();

    public URI getSchemaId();

    public <M extends Model> M getStaticInterface();

    public void removeEventListener(ModelEventListener listener);

    public void removeFieldEventListener(String fieldName, FieldEventListener listener);

    public void removeLinkEventListener(URI linkRelationId, LinkEventListener listener);

    public void setAllFieldsToDefaultValue();

    public void setFieldToDefaultValue(String fieldName);

    public <V> V setFieldValue(String fieldName, V fieldValue);

}
