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

package org.wrml.core;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Map;

import org.wrml.core.model.api.ResourceTemplate;
import org.wrml.core.model.schema.LinkRelation;
import org.wrml.core.model.schema.Schema;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.ModelGraph;
import org.wrml.core.runtime.event.FieldEventListener;
import org.wrml.core.runtime.event.LinkEventListener;
import org.wrml.core.runtime.event.ModelEventListener;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.www.MediaType;

/**
 * <p>
 * Model is the base interface of all Web resource schema instances. A "model"
 * in both the design-time context of data "modeling" and the runtime context of
 * a client-server "Model" View Controller (MVC) Web application.
 * </p>
 */
public interface Model extends Serializable {

    /**
     * Absorb all of the fields, links, and listeners from the specified
     * {@link Model} before freeing it.
     * 
     * @param modelToAbsorb
     *            to be absorbed.
     * @param additionalModelsToAbsorb
     *            The optional list of other models to absorb.
     */
    public void absorb(Model modelToAbsorb, Model... additionalModelsToAbsorb);

    /**
     * Add a listener for a variety of model events (the classic MVC pattern).
     * 
     * @param listener
     *            the {@link ModelEventListener} to add.
     * 
     * @see Model#addFieldEventListener(String, FieldEventListener)
     * @see Model#addLinkEventListener(URI, LinkEventListener)
     */
    public boolean addEventListener(ModelEventListener listener);

    /**
     * Add a listener for events relating to the named field.
     * 
     * @param listener
     *            The {@link FieldEventListener} to add.
     * 
     * @see Model#setFieldValue(String, Object)
     */
    public boolean addFieldEventListener(String fieldName, FieldEventListener listener);

    /**
     * Click the {@link Hyperlink} that is identified by the rel field value,
     * which is a {@link URI} of a {@link LinkRelation}. The click's effect is a
     * function of the metadata.
     * 
     * @param rel
     *            The {@link URI} of a {@link LinkRelation} that corresponds
     *            with a link that starts from this {@link Model}.
     * @param nativeReturnType
     *            The {@link Type} that the client would like to have returned.
     * @param requestEntity
     *            The optional entity to include in the request body.
     * @param hrefParams
     *            TODO
     * 
     * @return The result of clicking the link.
     * 
     * @see Model#getHyperLinks()
     * @see Model#addLinkEventListener(URI, LinkEventListener)
     */
    public Object clickLink(URI rel, java.lang.reflect.Type nativeReturnType, Object requestEntity,
            Map<String, String> hrefParams);

    /**
     * Add/override the fields of this {@link Model} with those contained within
     * the specified models. This is a form of prototypical extension.
     * 
     * @param modelToExtend
     *            The first model to extend.
     * @param additionalModelsToExtend
     *            The optional list of other models to extend.
     */
    public void extend(Model modelToExtend, Model... additionalModelsToExtend);

    /**
     * Free the model from the WRML runtime. The model may or may not still
     * have an associated (and available) Web resource.
     */
    public void free();

    /**
     * Get the {@link Context} associated with this {@link Model}.
     * 
     * @return the Model's {@link Context}.
     */
    public Context getContext();

    /**
     * Get the inner, purely dynamic {@link Model} interface.
     * 
     * @return The runtime {@link Model} with its dynamic interface.
     */
    public Model getDynamicInterface();

    /**
     * Get the value of the named field
     * 
     * @param fieldName
     *            The name of the field to lookup.
     * @return The value of the named field.
     * 
     * @see #setFieldValue(String, Object)
     */
    public Object getFieldValue(String fieldName);

    /**
     * Get the mapping of {@link LinkRelation} {@link URI} to {@link Hyperlink}.
     * 
     * @return The mapping of {@link LinkRelation} {@link URI} to
     *         {@link Hyperlink}.
     * 
     * @see Model#clickLink(URI, Type, Object, Map)
     * @see Model#addLinkEventListener(URI, LinkEventListener)
     */
    public ObservableMap<URI, Hyperlink> getHyperLinks();

    /**
     * Get the {@link MediaType} associated with this {@link Model}.
     * 
     * @return An "application/wrml" {@link MediaType}.
     */
    public MediaType getMediaType();

    /**
     * Get a graph of models that represents the root and its nested "children"
     * as described by the document which was deserialized in order to
     * instantiate this model.
     * 
     * @return The {@link ModelGraph} which provides a DOM-like Model
     *         navigation/traversal API.
     */
    public ModelGraph getModelGraph();

    /**
     * Get the Java {@link Type} representation of this Model's schema.
     * 
     * @return The native {@link Type}.
     * 
     * @see Model#getNativeTypeParameters()
     */
    public java.lang.reflect.Type getNativeType();

    /**
     * Get the Model's Java type's parameterized types.
     * 
     * @return The native {@link Type}.
     * 
     * @see Model#getNativeType()
     */
    public java.lang.reflect.Type[] getNativeTypeParameters();

    /**
     * A {@link ResourceTemplate} is a node within a particular's API's resource
     * tree, which is most commonly visualized via the forward slashes that
     * separates the resources in a URI's path segment.
     * 
     * @return The {@link ResourceTemplate} that this model instance originated
     *         from (or is intended for).
     */
    public ResourceTemplate getResourceTemplate();

    /**
     * Get the URI of the {@link ResourceTemplate} that this model instance.
     * 
     * @return The {@link ResourceTemplate} that this model instance originated
     *         from (or is intended for).
     */
    public URI getResourceTemplateId();

    /**
     * Get the WRML {@link Schema} associated with this Model.
     * 
     * @return the Model's schema
     */
    public Schema getSchema();

    /**
     * Get the id of this Model's {@link Schema}.
     * 
     * @return the Model's schema id
     */
    public URI getSchemaId();

    /**
     * Get a static proxy/facade which implements the Java interface associated
     * with this Model's {@link Schema} and backed by this same Model instance.
     * The Model returned is a JavaBean-oriented rendition of the Model's schema
     * with getters/setters for each field and normal looking methods for each
     * link.
     * 
     * @return a Model that implements the interface associated with the schema.
     * 
     * @see Model#getNativeType()
     * @see Model#getNativeTypeParameters()
     */
    public Model getStaticInterface();

    /**
     * Remove the specified {@link ModelEventListener}.
     * 
     * @param listener
     *            the {@link ModelEventListener} that is no longer interested in
     *            receiving events from this Model.
     * 
     * @see Model#addEventListener(ModelEventListener)
     */
    public boolean removeEventListener(ModelEventListener listener);

    /**
     * Remove the specified {@link FieldEventListener}.
     * 
     * @param fieldName
     *            the name of the field that is no longer interesting.
     * @param listener
     *            the {@link FieldEventListener} that is no longer interested in
     *            receiving events from this Model about the named field.
     * 
     * @see Model#addFieldEventListener(String, FieldEventListener)
     */
    public boolean removeFieldEventListener(String fieldName, FieldEventListener listener);

    /**
     * Restore the default value of each field.
     * 
     * @see Model#setFieldToDefaultValue(String)
     */
    public void setAllFieldsToDefaultValue();

    /**
     * Restore the default value of the named field.
     * 
     * @param fieldName
     *            the name of the field to reset to its default value.
     * 
     * @see Model#setAllFieldsToDefaultValue()
     */
    public void setFieldToDefaultValue(String fieldName);

    /**
     * Set the value of the named field. The value can be any of the types
     * described by {@link org.wrml.core.model.schema.Type}.
     * 
     * @param fieldName
     *            the name of the field to set.
     * @param fieldValue
     *            the value to be set on the named field.
     * @return the previous value of the named field.
     * 
     * @see Model#getFieldValue(String)
     * @see Model#addFieldEventListener(String, FieldEventListener)
     */
    public Object setFieldValue(String fieldName, Object fieldValue);

    /*
     * One of the fields in AlternateDimensions should be Locale.
     * 
     * Another might be size (big or small) full/partial or something to control
     * data payload sizing up and down by internally managing a mapping of many
     * to one Models beneath a facade of unique and separate instances. This
     * will allow field setting on the full model to write through to the inner
     * lightweight instance, which may also be viewed elsewhere.
     * 
     * Maybe create Enum constants behind the Static/Dynamic dimension
     * switching?
     */
    //public Model getAlternateRepresentation(AlternateDimensions alternateDimensions);

    //public void refresh(boolean forceSync);

}
