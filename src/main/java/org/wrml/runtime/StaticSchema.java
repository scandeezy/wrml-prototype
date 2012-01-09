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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.wrml.model.Document;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Schema;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;

/**
 * A web resource model, or model for short, has an associated schema to
 * describe its interface/form. Like a Java object has a class.
 * 
 * Schemas are modeled. Like Java's class Class<?>.
 * 
 * The interface of a model (fields, links, and constraints) conforms to the
 * Schema for schemas. Like how the Java reflection API for class Class<?>
 * describes a class's members.
 * 
 * The Schema for schemas is the "meta schema". The meta schema is modeled;
 * actually its a Document with (default) id:
 * http://api.schemas.wrml.org/org/wrml/schemas/Schema. Like the class
 * Class<Class<?>> has the "id" java.lang.Class<Class<?>>.
 * 
 * All models have a Schema, therefore the model for the meta schema has
 * itself as its Schema. Like the Object class has a method:
 * 
 * Class<?> getClass();
 * 
 * Which returns "this" when invoked on the class instance representing
 * Class<Class<?>>.
 */
public final class StaticSchema extends StaticDocument<Schema> {

    private static final long serialVersionUID = 1L;

    private ObservableList<Schema> _BaseSchemas;

    private ObservableMap<String, Field> _Fields;

    public StaticSchema(Context context) {
        super(context, Schema.class, null);
    }

    public ObservableList<Schema> getBaseSchemas() {

        if (_BaseSchemas == null) {

            Context context = getContext();
            List<Schema> baseSchemaList = new ArrayList<Schema>();

            Schema documentSchema = context.getSchema(context.getSchemaIdToClassTransformer().bToA(Document.class));
            baseSchemaList.add(documentSchema);

            _BaseSchemas = Observables.observableList(baseSchemaList);
        }

        return _BaseSchemas;
    }

    public ObservableMap<String, Field> getFields() {

        if (_Fields == null) {
            SortedMap<String, Field> fields = new TreeMap<String, Field>();
            _Fields = Observables.observableMap(fields);
        }

        return _Fields;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [BaseSchemas=" + _BaseSchemas + ", Fields=" + _Fields + "]";
    }

    public enum SchemaFields {
        baseSchemas,
        fields;
    }

}
