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

package org.wrml.runtime;

import java.net.URI;
import java.util.Map;

import org.wrml.Model;
import org.wrml.TypeSystem;
import org.wrml.model.CodeOnDemand;
import org.wrml.model.schema.Constraint;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Type;
import org.wrml.util.DelegatingFieldMap;
import org.wrml.util.MediaType;
import org.wrml.util.observable.ObservableList;

public class ModelFieldMap extends DelegatingFieldMap {

    private Model _Model;

    public ModelFieldMap(final Map<String, Object> delegate) {
        super(delegate);

    }

    public final Context getContext() {
        return getModel().getContext();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <V> Class<V> getFieldType(String fieldName) {

        final Prototype prototype = getContext().getPrototype(getMediaType());
        final Type type = prototype.getFieldPrototype(fieldName).getType();
        return (Class<V>) TypeSystem.instance.getTypeToClassTransformer().aToB(type);

    }

    private MediaType getMediaType() {
        return getModel().getMediaType();
    }

    @Override
    protected boolean isReadOnly(String fieldName) {

        // TODO: Check for a read-only model too.

        return false;
    }

    @SuppressWarnings("unchecked")
    protected Object setRawFieldValue(final String fieldName, Object newValue) {

        /*
         * TODO:
         * 
         * final Prototype prototype =
         * getContext().getPrototype(getMediaType());
         * final Field field = (Field)
         * prototype.getFields().get(fieldName).getStaticInterface();
         * 
         * if (field != null) {
         * if (field.isReadOnly()) {
         * throw new IllegalAccessError("Field \"" + fieldName +
         * "\" is read only in \""
         * + prototype.getMediaType() + "\"");
         * }
         * 
         * if (field.isRequired() && (newValue == null)) {
         * throw new NullPointerException("Field \"" + fieldName +
         * "\" is requires a value in \""
         * + prototype.getMediaType() + "\"");
         * }
         * 
         * final Object currentValue = getFieldValue(fieldName);
         * if (currentValue != null) {
         * if (Collection.class.isInstance(currentValue)) {
         * Collection<Object> collection = (Collection<Object>) currentValue;
         * collection.clear();
         * collection.addAll((Collection<Object>) newValue);
         * enforceFieldConstraints(field);
         * return currentValue;
         * }
         * }
         * 
         * enforceFieldConstraints(field);
         * }
         */

        return super.setRawFieldValue(fieldName, newValue);
    }

    private void enforceFieldConstraints(Field field) {

        final ObservableList<Constraint<Field>> fieldConstraints = field.getConstraints();
        if ((fieldConstraints != null) && (fieldConstraints.size() > 0)) {
            for (Constraint<Field> fieldConstraint : fieldConstraints) {
                ObservableList<CodeOnDemand> enforcerScripts = fieldConstraint.getDefinition().getEnforcers();
                for (CodeOnDemand enforcerScript : enforcerScripts) {

                    URI codeUri = enforcerScript.getCodeUri();

                    /*
                     * TODO: Download and execute:
                     * 
                     * FieldConstraintEnforcer {
                     * 
                     * public EnforcementResult
                     * enforceConstraint(
                     * fieldConstraint,
                     * Model model, <--------------- TODO: How to get model
                     * here?
                     * field,
                     * newValue);
                     * 
                     * }
                     */
                }
            }
        }

    }

    public Model getModel() {
        return _Model;
    }

    void setModel(Model model) {
        if (_Model != null) {
            throw new IllegalStateException("This " + getClass().getCanonicalName() + " (" + this
                    + ") already has a model.");
        }
        _Model = model;
    }

}
