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

import org.wrml.Model;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Schema;
import org.wrml.model.schema.Type;
import org.wrml.runtime.StaticSchema.SchemaFields;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.util.MediaType;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.transformer.Transformer;

/**
 * The WRML equivalent of the SystemClassLoader
 */
public class SystemSchemaService extends ProxyService implements Service {

    public static final String META_SCHEMA_NAME = "Schema";

    public static final String META_SCHEMA_DESCRIPTION = "A schema describes the structure of a model independent of its format. Schemas provide contractual resource type definitions, which are a crucial component of the interface that binds a server and its clients together.";

    private Schema _MetaSchema;

    public SystemSchemaService(Context context, Service originService) {
        super(context, originService);
    }

    @Override
    public Object get(URI resourceId, Object cachedEntity, MediaType responseType, Model referrer) {

        Context context = getContext();
        Object responseEntity = null;

        URI metaSchemaId = context.getSchemaIdToClassTransformer().bToA(Schema.class);
        if (metaSchemaId.equals(resourceId)) {
            responseEntity = getMetaSchema();
        }
        else {
            responseEntity = super.get(resourceId, cachedEntity, responseType, referrer);
        }

        return responseEntity;
    }

    @Override
    public final Transformer<URI, String> getIdTransformer() {
        return getContext().getSchemaIdToClassNameTransformer();
    }

    public Schema getMetaSchema() {

        if (_MetaSchema == null) {
            
            System.out.println("!!!!!! Creating the MetaSchema");
            
            _MetaSchema = new StaticSchema(getContext()).getStaticInterface();

            ObservableMap<String, Field> fields = _MetaSchema.getFields();

            _MetaSchema.setName(META_SCHEMA_NAME);
            _MetaSchema.setDescription(META_SCHEMA_DESCRIPTION);

            System.out.println("MetaSchema created: " + _MetaSchema);
            
            
            Field baseSchemaField = createField(_MetaSchema, SchemaFields.baseSchemas.toString(), Type.List);

            // TODO: Add a List<T> constraint (with a <T> of <Schmea>) to make this a field like: List<Schema>
            fields.put(baseSchemaField.getName(), baseSchemaField);

            Field fieldsField = createField(_MetaSchema, SchemaFields.fields.toString(), Type.Map);

            // TODO: Add a Map<K, V> constraint (with a 'K' of Type.Text and 'V' of Field (Schema)) to make this a field like: Map<String, Field>            
            fields.put(fieldsField.getName(), fieldsField);

            System.out.println("MetaSchema fields: " + fields);
        }

        return _MetaSchema;
    }
    
    private Field createField(Schema owner, String name, Type type) {
        return new StaticField(getContext(), owner, name, type).getStaticInterface();
    }


}
