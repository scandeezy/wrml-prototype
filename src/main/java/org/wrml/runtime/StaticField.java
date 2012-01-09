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
import java.util.ArrayList;
import java.util.List;

import org.wrml.model.schema.Constraint;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Schema;
import org.wrml.model.schema.Type;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.Observables;

public class StaticField extends StaticModel<Field> {

    private static final long serialVersionUID = 1L;

    private final Type _Type;

    private final Schema _Owner;
    private String _Name;
    private String _Title;

    private String _Description;
    private Object _DefaultValue;

    private boolean _Hidden;
    private boolean _ReadOnly;
    private boolean _Required;
    private boolean _TransientFlag;

    private ObservableList<Constraint> _Constraints;

    public StaticField(Context context, Schema owner, final String name, final Type type) {
        super(context, Field.class);

        TypeSystem typeSystem = TypeSystem.instance;
        _Owner = owner;
        _Type = type;
        _Name = name;
        _DefaultValue = typeSystem.getDefaultValue(typeSystem.getJavaType(type));
    }

    public final ObservableList<Constraint> getConstraints() {

        if (_Constraints == null) {

            Context context = getContext();
            List<Constraint> constraintsList = new ArrayList<Constraint>();
            _Constraints = Observables.observableList(constraintsList);
        }

        return _Constraints;
    }

    public final Schema getDeclaredSchema() {
        // TODO: Are these methods needed? Should the "declared" schema be different than owner schema in this context?
        return getOwner();
    }

    public final URI getDeclaredSchemaId() {
        // TODO: Are these methods needed? Should the "declared" schema be different than owner schema in this context?
        return getOwnerId();
    }

    public final Object getDefaultValue() {
        return _DefaultValue;
    }

    public final String getDescription() {
        return _Description;
    }

    public final String getName() {
        return _Name;
    }

    public final Schema getOwner() {
        return _Owner;
    }

    public final URI getOwnerId() {
        return getOwner().getId();
    }

    public final String getTitle() {
        return _Title;
    }

    public final Type getType() {
        return _Type;
    }

    public final boolean isHidden() {
        return _Hidden;
    }

    public final boolean isReadOnly() {
        return _ReadOnly;
    }

    public final boolean isRequired() {
        return _Required;
    }

    public final boolean isTransient() {
        return _TransientFlag;
    }

    public final Object setDefaultValue(Object defaultValue) {
        Object oldDefaultValue = _DefaultValue;
        _DefaultValue = defaultValue;
        return oldDefaultValue;
    }

    public final String setDescription(String description) {
        String oldDescription = _Description;
        _Description = description;
        return oldDescription;
    }

    public final boolean setHidden(boolean hidden) {
        boolean oldHidden = _Hidden;
        _Hidden = hidden;
        return oldHidden;
    }

    public final String setName(String name) {
        String oldName = _Name;
        _Name = name;
        return oldName;
    }

    public final boolean setReadOnly(boolean readOnly) {
        boolean oldReadOnly = _ReadOnly;
        _ReadOnly = readOnly;
        return oldReadOnly;
    }

    public final boolean setRequired(boolean required) {
        boolean oldRequired = _Required;
        _Required = required;
        return oldRequired;
    }

    public final String setTitle(String title) {
        String oldTitle = _Title;
        _Title = title;
        return oldTitle;
    }

    public final boolean setTransient(boolean transientFlag) {
        boolean oldTransientFlag = _TransientFlag;
        _TransientFlag = transientFlag;
        return oldTransientFlag;
    }

}
