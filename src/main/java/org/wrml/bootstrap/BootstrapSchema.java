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
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.wrml.model.schema.Field;
import org.wrml.model.schema.Link;
import org.wrml.model.schema.Schema;
import org.wrml.model.schema.Type;
import org.wrml.runtime.Context;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;

/**
 * <p>
 * A bootstrap proxy-based implementation of WRML's Schema schema.
 * </p>
 * 
 * <p>
 * A web resource model, or model for short, has an associated schema to
 * describe its interface/form. Like a Java object has a class.
 * </p>
 * 
 * <p>
 * Schemas are modeled. Like Java's class Class<?>.
 * </p>
 * 
 * <p>
 * The API of a model (fields, links, and constraints) conforms to the Schema
 * for schemas. Like how the Java reflection API for class Class<?> describes a
 * class's members.
 * </p>
 * 
 * <p>
 * The Schema for schemas is the "meta schema". The meta schema is modeled;
 * actually its a Document with (default) id:
 * http://api.schemas.wrml.org/org/wrml/schemas/Schema. Like the class
 * Class<Class<?>> has the "id" java.lang.Class<Class<?>>.
 * </p>
 * 
 * <p>
 * All models have a Schema, therefore the model for the meta schema has itself
 * as its Schema. Like the Object class has a method:
 * </p>
 * 
 * <code>
 *     Class<?> getClass();
 * </code>
 * 
 * <p>
 * Which returns "this" when invoked on the class instance representing
 * Class<Class<?>>.
 * </p>
 */
public class BootstrapSchema extends BootstrapDocument<Schema> {

    private static final long serialVersionUID = 1L;

    private final ObservableList<URI> _BaseSchemaIds;
    private final ObservableList<URI> _ConstraintIds;
    private String _Description;
    private final SortedMap<String, BootstrapField> _BootstrapFields;
    private String _Name;

    public BootstrapSchema(Context context, URI id) {
        super(context, Schema.class, null, id);

        _BaseSchemaIds = Observables.observableList(new ArrayList<URI>());
        _ConstraintIds = Observables.observableList(new ArrayList<URI>());
        _BootstrapFields = new TreeMap<String, BootstrapField>();
    }

    public final ObservableList<URI> getBaseSchemaIds() {
        return _BaseSchemaIds;
    }

    public final SortedMap<String, BootstrapField> getBootstrapFields() {
        return _BootstrapFields;
    }

    public final ObservableList<URI> getConstraintIds() {
        return _ConstraintIds;
    }

    public final String getDescription() {
        return _Description;
    }

    public final ObservableMap<String, Field> getFields() {

        if (_BootstrapFields.isEmpty()) {
            return Observables.emptyMap();
        }

        final SortedMap<String, Field> fields = new TreeMap<String, Field>();

        final Set<String> bootstrapFieldNames = _BootstrapFields.keySet();
        for (final String bootstrapFieldName : bootstrapFieldNames) {
            final BootstrapField bootstrapField = _BootstrapFields.get(bootstrapFieldName);
            fields.put(bootstrapFieldName, (Field) bootstrapField.getStaticInterface());
        }

        return Observables.observableMap(fields);
    }

    public final ObservableMap<URI, Link> getLinks() {
        // TODO
        System.out.println(this + " - TODO: getLinks()");
        return null;
    }

    public final String getName() {
        return _Name;
    }

    public final long getVersion() {
        return serialVersionUID;
    }

    public final String setDescription(String description) {
        final String oldDescription = _Description;
        _Description = description;
        return oldDescription;
    }

    public final String setName(String name) {
        final String oldName = _Name;
        _Name = name;
        return oldName;
    }

    @Override
    public String toString() {
        return getClass().getName() + " : { baseSchemaIds : [" + _BaseSchemaIds + "], fields : [" + _BootstrapFields
                + "] }";
    }

    protected final BootstrapField createBootstrapField(String name, Type type) {
        return new BootstrapField(getContext(), this, name, type);
    }

    protected final URI getSchemaId(String schemaFullName) {
        return getContext().getSchemaIdToFullNameTransformer().bToA(schemaFullName);
    }

}
