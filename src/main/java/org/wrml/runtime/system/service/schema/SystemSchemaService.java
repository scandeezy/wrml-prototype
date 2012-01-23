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

package org.wrml.runtime.system.service.schema;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.wrml.Model;
import org.wrml.model.schema.Field;
import org.wrml.runtime.Context;
import org.wrml.runtime.bootstrap.BootstrapSchema;
import org.wrml.runtime.bootstrap.FieldBootstrapSchema;
import org.wrml.runtime.bootstrap.FieldNames;
import org.wrml.runtime.bootstrap.SchemaBootstrapSchema;
import org.wrml.runtime.system.transformer.SystemTransformers;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.transformer.Transformer;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;
import org.wrml.www.MediaType;

/**
 * The WRML equivalent of the SystemClassLoader.
 */
public final class SystemSchemaService extends ProxyService implements Service {

    public static final String SCHEMA_NAMESPACE = "org.wrml.model.schema";

    public static final String SCHEMA_SCHEMA_NAME = "Schema";
    public static final String FIELD_SCHEMA_NAME = "Field";

    public static final String MODEL_SCHEMA_FULL_NAME = "org.wrml.Model";
    public static final String SCHEMA_SCHEMA_FULL_NAME = SCHEMA_NAMESPACE + "." + SCHEMA_SCHEMA_NAME;
    public static final String FIELD_SCHEMA_FULL_NAME = SCHEMA_NAMESPACE + "." + FIELD_SCHEMA_NAME;

    private final SchemaBootstrapSchema _SchemaBootstrapSchema;
    private final FieldBootstrapSchema _FieldBootstrapSchema;

    private Prototype _SchemaBootstrapPrototype;
    private Prototype _FieldBootstrapPrototype;

    private final ObservableMap<Type, Prototype> _Prototypes;

    public SystemSchemaService(Context context, Service originService) {
        super(context, originService);

        // TODO: Add ClassLoader segregated by API for "reloadablilty"

        _Prototypes = Observables.observableMap(new HashMap<Type, Prototype>());

        final Transformer<URI, String> idTransformer = getIdTransformer();

        _FieldBootstrapSchema = new FieldBootstrapSchema(context, idTransformer.bToA(FIELD_SCHEMA_FULL_NAME));
        _SchemaBootstrapSchema = new SchemaBootstrapSchema(context, idTransformer.bToA(SCHEMA_SCHEMA_FULL_NAME));
    }

    @Override
    public Object get(URI schemaId, Object cachedEntity, MediaType responseType, Model referrer) {

        //System.out.println("SystemSchemaService.get: \"" + schemaId + "\"");

        Object responseEntity = null;

        if (_SchemaBootstrapSchema.getId().equals(schemaId)) {

            /*
             * A Request to get "org.wrml.model.schema.Schema", return the
             * static (Schema) interface of the bootstrap meta schema.
             */

            responseEntity = _SchemaBootstrapSchema.getStaticInterface();
            //System.out.println("SystemSchemaService: Returning the bootstrap \"MetaSchema\" : \n" + responseEntity);
        }
        else if (_FieldBootstrapSchema.getId().equals(schemaId)) {

            /*
             * Since the Field schema is referenced by the (meta) Schema schema,
             * it also needs to be bootstrapped with a "local" static class
             * implementation.
             * 
             * A Request to get "org.wrml.model.schema.Field", return the
             * static (Schema) interface of the bootstrap field.
             */

            responseEntity = _FieldBootstrapSchema.getStaticInterface();
            //System.out.println("SystemSchemaService: Returning the bootstrap Field: \n" + responseEntity);
        }
        else {

            /*
             * Use the WWW or whatever service we can delegate to to actually go
             * and GET a new Schema right about now.
             */

            responseEntity = super.get(schemaId, cachedEntity, responseType, referrer);
            //System.out.println("SystemSchemaService: Returning remote schema: \n" + responseEntity);
        }

        return responseEntity;
    }

    @Override
    public final Transformer<URI, String> getIdTransformer() {
        final SystemTransformers systemTransformers = getContext().getSystemTransformers();
        return systemTransformers.getSchemaIdToFullNameTransformer();
    }

    public final Prototype getPrototype(Type staticInterfaceType) {

        if (_Prototypes.containsKey(staticInterfaceType)) {
            return _Prototypes.get(staticInterfaceType);
        }

        Prototype prototype = null;

        if (_SchemaBootstrapSchema.getId().equals(staticInterfaceType)) {

            if (_SchemaBootstrapPrototype == null) {
                _SchemaBootstrapPrototype = createBootstrapPrototype(staticInterfaceType, _SchemaBootstrapSchema);
            }

            prototype = _SchemaBootstrapPrototype;
        }
        else if (_FieldBootstrapSchema.getId().equals(staticInterfaceType)) {

            if (_FieldBootstrapPrototype == null) {
                _FieldBootstrapPrototype = createBootstrapPrototype(staticInterfaceType, _FieldBootstrapSchema);
            }

            prototype = _FieldBootstrapPrototype;

        }
        else if (!_Prototypes.containsKey(staticInterfaceType)) {
            prototype = createPrototype(staticInterfaceType);
        }

        _Prototypes.put(staticInterfaceType, prototype);
        prototype.init();
        return prototype;
    }

    private Prototype createBootstrapPrototype(Type staticInterfaceType, BootstrapSchema bootstrapSchema) {
        final Context context = getContext();
        final Prototype bootstrapPrototype = createPrototype(staticInterfaceType);
        final List<Field> schemaFields = bootstrapSchema.getFields();
        final Map<String, Field> prototypeFields = bootstrapPrototype.getFields();

        // Convert the bootstrap schema's list of fields to a map
        context.mapFieldsByFieldName(prototypeFields, schemaFields, FieldNames.Schema.fields.toString());

        return bootstrapPrototype;

    }

    private Prototype createPrototype(Type staticInterfaceType) {
        return new Prototype(getContext(), staticInterfaceType);
    }

}
