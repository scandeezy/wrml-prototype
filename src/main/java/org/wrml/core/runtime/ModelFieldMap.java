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

package org.wrml.core.runtime;

import java.net.URI;
import java.util.Map;

import org.wrml.core.Model;
import org.wrml.core.model.CodeOnDemand;
import org.wrml.core.model.schema.Constraint;
import org.wrml.core.model.schema.Field;
import org.wrml.core.runtime.system.Prototype;
import org.wrml.core.util.observable.ObservableList;

public class ModelFieldMap extends DelegatingFieldMap {

    private Model _Model;

    public ModelFieldMap(final Context context, final Map<String, Object> delegate) {
        super(context, delegate);
    }

    public Model getModel() {
        return _Model;
    }

    @Override
    protected java.lang.reflect.Type getFieldNativeType(String fieldName) {
        final java.lang.reflect.Type nativeType = getModel().getNativeType();
        final Prototype prototype = getContext().getPrototype(nativeType);
        return prototype.getFieldPrototype(fieldName).getNativeType();
    }

    @Override
    protected boolean isReadOnly(String fieldName) {

        // TODO: Check for a read-only model too.

        return false;
    }

    @Override
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

    void setModel(Model model) {
        if (_Model != null) {
            throw new IllegalStateException("This " + getClass().getCanonicalName() + " (" + this
                    + ") already has a model.");
        }
        _Model = model;
    }

    private void enforceFieldConstraints(Field field) {

        final ObservableList<Constraint<Field>> fieldConstraints = field.getConstraints();
        if ((fieldConstraints != null) && (fieldConstraints.size() > 0)) {
            for (final Constraint<Field> fieldConstraint : fieldConstraints) {
                final ObservableList<CodeOnDemand> enforcerScripts = fieldConstraint.getDefinition().getEnforcers();
                for (final CodeOnDemand enforcerScript : enforcerScripts) {

                    final URI codeUri = enforcerScript.getCodeUri();

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

}
