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

package org.wrml.bootstrap;

import java.net.URI;
import java.util.ArrayList;

import org.wrml.model.schema.Constraint;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Schema;
import org.wrml.model.schema.Type;
import org.wrml.runtime.Context;
import org.wrml.runtime.TypeSystem;
import org.wrml.runtime.system.transformer.SystemTransformers;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.Observables;

/**
 * A bootstrap proxy-based implementation of WRML's Field schema.
 */
public class BootstrapField extends BootstrapModel<Field> {

    private static final long serialVersionUID = 1L;

    private final Type _Type;

    private final BootstrapSchema _Owner;
    private String _Name;
    private String _Title;

    private String _Description;
    private Object _DefaultValue;

    private boolean _Hidden;
    private boolean _ReadOnly;
    private boolean _Required;
    private boolean _Local;
    private final ObservableList<Constraint<Field>> _Constraints;

    public BootstrapField(Context context, BootstrapSchema owner, final String name, final Type type) {
        super(context, Field.class);

        _Owner = owner;
        _Type = type;
        _Name = name;

        _Constraints = Observables.observableList(new ArrayList<Constraint<Field>>());

        final TypeSystem typeSystem = context.getTypeSystem();
        final SystemTransformers systemTransformers = context.getSystemTransformers();
        _DefaultValue = typeSystem.getDefaultValue(systemTransformers.getNativeTypeToTypeTransformer().bToA(type));
    }

    public final ObservableList<Constraint<Field>> getConstraints() {
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
        return (Schema) _Owner.getStaticInterface();
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

    public final boolean isLocal() {
        return _Local;
    }

    public final boolean isReadOnly() {
        return _ReadOnly;
    }

    public final boolean isRequired() {
        return _Required;
    }

    public final Object setDefaultValue(Object defaultValue) {
        final Object oldDefaultValue = _DefaultValue;
        _DefaultValue = defaultValue;
        return oldDefaultValue;
    }

    public final String setDescription(String description) {
        final String oldDescription = _Description;
        _Description = description;
        return oldDescription;
    }

    public final boolean setHidden(boolean hidden) {
        final boolean oldHidden = _Hidden;
        _Hidden = hidden;
        return oldHidden;
    }

    public final boolean setLocal(boolean local) {
        final boolean oldLocal = _Local;
        _Local = local;
        return oldLocal;
    }

    public final String setName(String name) {
        final String oldName = _Name;
        _Name = name;
        return oldName;
    }

    public final boolean setReadOnly(boolean readOnly) {
        final boolean oldReadOnly = _ReadOnly;
        _ReadOnly = readOnly;
        return oldReadOnly;
    }

    public final boolean setRequired(boolean required) {
        final boolean oldRequired = _Required;
        _Required = required;
        return oldRequired;
    }

    public final String setTitle(String title) {
        final String oldTitle = _Title;
        _Title = title;
        return oldTitle;
    }

    @Override
    public String toString() {
        return getClass().getName() + " { name : \"" + _Name + "\", type : \"" + _Type + "\" }";
    }

}
