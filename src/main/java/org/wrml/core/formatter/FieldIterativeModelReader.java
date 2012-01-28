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

package org.wrml.core.formatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.wrml.core.Model;
import org.wrml.core.event.Event;
import org.wrml.core.event.EventSource;
import org.wrml.core.formatter.FieldIterativeModelReader.EventListener;
import org.wrml.core.formatter.FieldIterativeModelReader.EventListener.EventNames;
import org.wrml.core.model.schema.Link;
import org.wrml.core.model.schema.Type;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.ModelGraph;
import org.wrml.core.runtime.TypeSystem;
import org.wrml.core.runtime.system.service.schema.FieldPrototype;
import org.wrml.core.runtime.system.service.schema.Prototype;
import org.wrml.core.runtime.system.transformer.SystemTransformers;
import org.wrml.core.transformer.Transformer;
import org.wrml.core.transformer.Transformers;
import org.wrml.core.util.observable.ObservableList;
import org.wrml.core.util.observable.ObservableMap;
import org.wrml.core.util.observable.Observables;

public abstract class FieldIterativeModelReader extends EventSource<EventListener> implements ModelReader,
        Iterator<String> {

    public FieldIterativeModelReader() {
        super(EventListener.class);
    }

    /**
     * Reads the root of a ModelGraph by iterating over a linear sequence/stream
     * of (possibly nested) models with fields.
     */
    public Model readModel(Context context, java.lang.reflect.Type nativeType) throws Exception {

        final Event<FieldIterativeModelReader> event = new Event<FieldIterativeModelReader>(this);

        /*
         * Create the ModelGraph that, once initialized, will represent the
         * scoped/nested hierarchy of models created as a result of reading the
         * root model to be returned by this method.
         */
        final ModelGraph modelGraph = new ModelGraph(context);

        final EventListener modelGraphScopeEventListener = new ModelGraphScopeEventListener(modelGraph);
        addEventListener(modelGraphScopeEventListener);

        fireEvent(EventNames.beginReadModelGraph, event);

        final Model model = readModelToGraph(context, nativeType, modelGraph);

        removeEventListener(modelGraphScopeEventListener);

        return model;
    }

    public Model readModelToGraph(Context context, java.lang.reflect.Type nativeType, ModelGraph modelGraph)
            throws Exception {

        final Event<FieldIterativeModelReader> event = new Event<FieldIterativeModelReader>(this);

        final Model model = context.instantiateModel(nativeType, modelGraph);

        fireEvent(EventNames.beginReadModel, event);

        /*
         * Assuming the initial focus of the model created above, iterate over a
         * sequence of field names and read their corresponding values so that
         * they may be set on the currently "focused" model.
         * 
         * Model focus is a scoping concept that follows the stacked nesting of
         * models in common wire layouts like JSON and XML. If this loop reads a
         * nested model as a field value, then it will become the focused model
         * (until it's scope is closed).
         * 
         * Basically we are reading the fields from top to bottom and cursoring
         * over the nested models from left to right. The model graph's helpful
         * cursor, in this case is a stack that is rotated 90 degrees to the
         * left.
         * 
         * Note that this algorithm for nested model reading conveniently
         * mirrors the general layout of both XML and JSON documents.
         */
        while (hasNext()) {

            // Read the next field name in the sequence.
            final String fieldName = next();

            if ((fieldName == null) || !modelGraph.isInitCursorFocused()) {

                // We are all done. Model graph is done.
                break;
            }

            // Get the currently focused model scope (the current '{' in a JSON file)
            final Model focusModel = modelGraph.getInitCursorFocus();

            if (modelGraph != focusModel.getModelGraph()) {
                throw new IllegalStateException("Bug: WTF");
            }

            System.out.print("CHOO-CHOO!! READING FIELD: ");
            System.out.println("\"" + fieldName + "\" for " + modelGraph);

            /*
             * If we end up reading a nested model as a field, then we can share
             * the field's name as the relationship from the model graph's point
             * of view.
             */
            modelGraph.setInitCursorFocusRelationShipName(fieldName);

            if ("links".equals(fieldName)) {

                final List<Object> list = new ArrayList<Object>();
                readListElements(context, Link.class, modelGraph, list);
                final ObservableList<Object> links = Observables.observableList(list);

                // TODO: Set the links on the model. 
                //currentModel.getHyperLinks()

                continue;
            }

            /*
             * The schema of the focused model contains interesting metadata
             * related to the named field.
             */
            final FieldPrototype fieldPrototype = getFieldPrototype(context, focusModel, fieldName);

            if (fieldPrototype == null) {

                /*
                 * Gasp! Certainly our Schema must already know about a field
                 * with this name!
                 * 
                 * This "filtering" of wire garbage is a security (fail-safe)
                 * feature in some sense.
                 * 
                 * TODO: This may need to be made more flexible to handle
                 * versioning in a robust way. Maybe log an error and drop the
                 * value instead of blowing up? Could be a "strict" mode flag?
                 */
                throw new IllegalStateException("Bug: There is no FieldPrototype for a field named: \"" + fieldName
                        + "\" in a type called: \"" + focusModel.getNativeType() + "\" (schema : \""
                        + focusModel.getSchemaId() + "\")");
            }

            final java.lang.reflect.Type fieldNativeType = fieldPrototype.getNativeType();

            // Read the field's value, possibly shifting our focus to a new, nested model in the next (field) iteration. 
            final Object fieldValue = readValue(context, fieldNativeType, modelGraph);

            System.out.print("TOOT-TOOT!! THE FIELD NAMED \"");
            System.out.print(fieldName);
            System.out.print("\" IS A \"");
            System.out.print(fieldNativeType);
            System.out.print("\" TYPE, WITH VALUE: ");
            System.out.println(fieldValue);

            /*
             * On behalf of our loop's current iteration's "focusModel", we
             * read a "fieldValue" for a Field named "fieldName". So...
             */
            focusModel.setFieldValue(fieldName, fieldValue);

            if (focusModel != modelGraph.getInitCursorFocus()) {
                // Need to break out of this loop if we are no longer reading for the focused model.
                break;
            }

        }

        fireEvent(EventNames.endReadModelGraph, event);

        System.out.println("\n\nTA DA!! A Model for type \"" + model.getNativeType() + "\" (schema : \""
                + model.getSchemaId() + "\") has been \"fully\" read (see below):\n");
        System.out.println(model);

        return model;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    protected FieldPrototype getFieldPrototype(final Context context, final Model model, final String fieldName) {
        final java.lang.reflect.Type nativeType = model.getNativeType();
        final Prototype prototype = context.getPrototype(nativeType);
        final FieldPrototype fieldPrototype = prototype.getFieldPrototype(fieldName);
        return fieldPrototype;
    }

    protected abstract Boolean readBooleanValue() throws Exception;

    @SuppressWarnings("unchecked")
    protected <T extends Enum<T>> T readChoiceValue(Context context, java.lang.reflect.Type nativeType)
            throws Exception {
        final String name = readRawTextValue();
        final TypeSystem typeSystem = context.getTypeSystem();
        return typeSystem.getEnumFromString((Class<T>) nativeType, name);
    }

    // TODO: Change to Joda DateTime?
    protected abstract Date readDateTimeValue(Context context, java.lang.reflect.Type nativeType) throws Exception;

    protected abstract Double readDoubleValue() throws Exception;

    protected abstract Integer readIntegerValue() throws Exception;

    protected abstract void readListElements(Context context, java.lang.reflect.Type elementNativeType,
            ModelGraph modelGraph, List<Object> list) throws Exception;

    protected ObservableList<?> readListValue(Context context, java.lang.reflect.Type nativeType, ModelGraph modelGraph)
            throws Exception {

        final TypeSystem typeSystem = context.getTypeSystem();
        final java.lang.reflect.Type[] nativeTypeParameters = typeSystem.getNativeTypeParameters(nativeType);

        if ((nativeTypeParameters == null) || (nativeTypeParameters.length != 1)) {
            throw new IllegalStateException("No type params in the List value \"" + nativeType + "\"");
        }

        final java.lang.reflect.Type parameterizedType = nativeTypeParameters[0];
        final List<Object> list = new ArrayList<Object>();

        String relationShipName = modelGraph.getInitCursorFocusRelationShipName();
        relationShipName = relationShipName + " - List element <" + String.valueOf(parameterizedType) + ">";
        modelGraph.setInitCursorFocusRelationShipName(relationShipName);
        readListElements(context, parameterizedType, modelGraph, list);
        return Observables.observableList(list);
    }

    protected abstract Long readLongValue() throws Exception;

    protected abstract ObservableMap<?, ?> readMapValue(Context context, java.lang.reflect.Type nativeType,
            ModelGraph modelGraph) throws Exception;

    protected abstract Model readModelValue(Context context, java.lang.reflect.Type nativeType, ModelGraph modelGraph)
            throws Exception;

    protected abstract Object readNativeValue(Context context, java.lang.reflect.Type nativeType) throws Exception;

    protected abstract String readRawTextValue() throws Exception;

    protected Object readTextValue(Context context, java.lang.reflect.Type nativeType) throws Exception {

        final String text = readRawTextValue();

        if (text == null) {
            return null;
        }

        if ((nativeType != null) && !String.class.equals(nativeType)) {
            final SystemTransformers systemTransformers = context.getSystemTransformers();
            final Class<?> nativeClass = systemTransformers.getNativeTypeToClassTransformer().aToB(nativeType);
            final Transformers<String> stringTransformers = context.getStringTransformers();
            final Transformer<?, String> stringTransformer = stringTransformers.getTransformer(nativeClass);
            if (stringTransformer != null) {
                return stringTransformer.bToA(text);
            }
        }

        return text;
    }

    protected Object readValue(Context context, java.lang.reflect.Type nativeType, ModelGraph modelGraph)
            throws Exception {

        Object value = null;

        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final Type type = systemTransformers.getNativeTypeToTypeTransformer().aToB(nativeType);

        switch (type) {

        case Text:
            value = readTextValue(context, nativeType);
            break;

        case Model:
            value = readModelValue(context, nativeType, modelGraph);
            break;

        case List:
            value = readListValue(context, nativeType, modelGraph);
            break;

        case Map:
            value = readMapValue(context, nativeType, modelGraph);
            break;

        case Boolean:
            value = readBooleanValue();
            break;

        case Choice:
            value = readChoiceValue(context, nativeType);
            break;

        case DateTime:
            value = readDateTimeValue(context, nativeType);
            break;

        case Double:
            value = readDoubleValue();
            break;

        case Integer:
            value = readIntegerValue();
            break;

        case Long:
            value = readLongValue();
            break;

        case Native:
        default:
            value = readNativeValue(context, nativeType);
            break;
        }

        return value;
    }

    public static class DefaultEventListener implements EventListener {

        public void onBeginReadModel(Event<FieldIterativeModelReader> event) {
        }

        public void onBeginReadModelGraph(Event<FieldIterativeModelReader> event) {
        }

        public void onEndReadModel(Event<FieldIterativeModelReader> event) {
        }

        public void onEndReadModelGraph(Event<FieldIterativeModelReader> event) {
        }
    }

    public static interface EventListener extends java.util.EventListener {

        public void onBeginReadModel(Event<FieldIterativeModelReader> event);

        public void onBeginReadModelGraph(Event<FieldIterativeModelReader> event);

        public void onEndReadModel(Event<FieldIterativeModelReader> event);

        public void onEndReadModelGraph(Event<FieldIterativeModelReader> event);

        public enum EventNames {
            beginReadModelGraph,
            endReadModelGraph,
            beginReadModel,
            endReadModel
        }
    }

    private static class ModelGraphScopeEventListener extends DefaultEventListener {

        private final ModelGraph _ModelGraph;

        public ModelGraphScopeEventListener(ModelGraph modelGraph) {
            _ModelGraph = modelGraph;
        }

        @Override
        public void onEndReadModel(Event<FieldIterativeModelReader> event) {
            _ModelGraph.popInitCursorBack();
        }

    }

}
