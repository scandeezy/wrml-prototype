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

package org.wrml.formatter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.wrml.Model;
import org.wrml.model.schema.Link;
import org.wrml.model.schema.Type;
import org.wrml.runtime.Context;
import org.wrml.runtime.TypeSystem;
import org.wrml.runtime.system.service.schema.FieldPrototype;
import org.wrml.runtime.system.service.schema.Prototype;
import org.wrml.runtime.system.transformer.SystemTransformers;
import org.wrml.transformer.Transformer;
import org.wrml.transformer.Transformers;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;

/*
 * Reads a Model by iterating over a sequence of fields.
 */
public abstract class AbstractModelReader implements ModelReader, Iterator<String> {

    private Stack<Model> _ModelStack;

    public void close() throws Exception {
        _ModelStack = null;
    }

    public Stack<Model> getModelStack() {
        return _ModelStack;
    }

    public void open(InputStream inputStream) throws Exception {
        _ModelStack = new Stack<Model>();
    }

    public Model readModel(Context context, java.lang.reflect.Type nativeType) throws Exception {

        final Model model = context.instantiateModel(nativeType);
        final Stack<Model> modelStack = getModelStack();
        modelStack.push(model);

        System.out.println(this.getClass().getCanonicalName() + " - readModel: " + nativeType);

        // TODO: Use a Stack to track the model depth. Pass the stack down.

        while (hasNext()) {

            final String fieldName = next();
            if (fieldName == null) {
                break;
            }

            final Model currentModel = modelStack.peek();

            if ("links".equals(fieldName)) {

                final List<Object> list = new ArrayList<Object>();
                readListElements(context, Link.class, list);
                final ObservableList<Object> links = Observables.observableList(list);

                // TODO: Set the links on the model. 
                //currentModel.getHyperLinks()
                continue;
            }

            final FieldPrototype fieldPrototype = getFieldPrototype(context, currentModel, fieldName);

            if (fieldPrototype == null) {
                throw new NullPointerException("There is no FieldPrototype for a field named: \"" + fieldName
                        + "\" in a type called: \"" + currentModel.getNativeType() + "\" (schema : \""
                        + currentModel.getSchemaId() + "\")");
            }

            final Object fieldValue = readValue(context, fieldPrototype.getNativeType());

            currentModel.setFieldValue(fieldName, fieldValue);
        }

        return model;
    }

    public void remove() {
    }

    protected FieldPrototype getFieldPrototype(Context context, Model model, String fieldName) {
        final java.lang.reflect.Type nativeType = model.getNativeType();
        final Prototype prototype = context.getPrototype(nativeType);
        final FieldPrototype fieldPrototype = prototype.getFieldPrototype(fieldName);
        return fieldPrototype;
    }

    protected abstract Boolean readBooleanValue() throws Exception;

    protected Enum<?> readChoiceValue(Context context, java.lang.reflect.Type nativeType) throws Exception {
        // TODO: Match the string value against an enum choice
        return null;
    }

    // TODO: Change to Joda DateTime?
    protected abstract Date readDateTimeValue(Context context, java.lang.reflect.Type nativeType) throws Exception;

    protected abstract Double readDoubleValue() throws Exception;

    protected abstract Integer readIntegerValue() throws Exception;

    protected abstract void readListElements(Context context, java.lang.reflect.Type elementNativeType,
            List<Object> list) throws Exception;

    protected ObservableList<?> readListValue(Context context, java.lang.reflect.Type nativeType) throws Exception {

        final TypeSystem typeSystem = context.getTypeSystem();
        final java.lang.reflect.Type[] nativeTypeParameters = typeSystem.getNativeTypeParameters(nativeType);

        if ((nativeTypeParameters == null) || (nativeTypeParameters.length != 1)) {
            throw new IllegalStateException("No type params in the List value \"" + nativeType + "\"");
        }

        final java.lang.reflect.Type parameterizedType = nativeTypeParameters[0];
        final List<Object> list = new ArrayList<Object>();
        readListElements(context, parameterizedType, list);
        return Observables.observableList(list);
    }

    protected abstract Long readLongValue() throws Exception;

    protected abstract ObservableMap<?, ?> readMapValue(Context context, java.lang.reflect.Type nativeType)
            throws Exception;

    protected abstract Model readModelValue(Context context, java.lang.reflect.Type nativeType) throws Exception;

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

    protected Object readValue(Context context, java.lang.reflect.Type nativeType) throws Exception {

        Object value = null;

        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final Type type = systemTransformers.getNativeTypeToTypeTransformer().aToB(nativeType);

        switch (type) {

        case Text:
            value = readTextValue(context, nativeType);
            break;

        case Model:
            value = readModelValue(context, nativeType);
            break;

        case List:
            value = readListValue(context, nativeType);
            break;

        case Map:
            value = readMapValue(context, nativeType);
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

}
