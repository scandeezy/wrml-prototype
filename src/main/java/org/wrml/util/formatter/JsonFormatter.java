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

package org.wrml.util.formatter;

import java.io.InputStream;
import java.net.URI;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import org.wrml.Model;
import org.wrml.model.schema.Field;
import org.wrml.model.schema.Schema;
import org.wrml.model.schema.Type;
import org.wrml.runtime.Context;
import org.wrml.util.http.Body;
import org.wrml.util.http.Entity;
import org.wrml.util.http.Message;
import org.wrml.util.observable.ObservableMap;

public class JsonFormatter extends AbstractFormatter {

    public JsonFormatter(Context context) {
        super(context);
    }

    public Model read(Message requestMessage, Message responseMessage, Context context, URI schemaId) throws Exception {

        Model responseModel = context.instantiateModel(schemaId);

        System.out.println("A JSON formatting of: " + responseModel + " of type: " + schemaId);

        Schema schema = context.getSchema(schemaId);
        ObservableMap<String, Field> fields = schema.getFields();

        Entity entity = responseMessage.getEntity();
        Body body = entity.getBody();
        InputStream inputStream = body.getInputStream();
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = jsonFactory.createJsonParser(inputStream);

        JsonToken token = jsonParser.nextToken();

        while (token != null) {

            String currentName = jsonParser.getCurrentName();

            if (currentName != null) {
                if ("links".equals(currentName)) {
                    // TODO: !!!!----- Deal with links
                }
                else {
                    token = jsonParser.nextToken();
                    System.out.println("A JSON field named: " + currentName + " : " + token);

                    if (fields != null) {
                        Field field = fields.get(currentName);
                        System.out.println("To fill a schema field: " + field);
                        if (field != null) {

                        }
                    }

                    Object fieldValue = null;

                    switch (token) {

                    case START_ARRAY:

                        //Map<K, V> map = new HashMap<K, V>;
                        //fieldValue = Observables.observableMap(map);

                        break;

                    case VALUE_EMBEDDED_OBJECT:
                        //fieldValue = context.instantiateModel(innerSchemaId);

                        break;

                    case VALUE_FALSE:
                        fieldValue = Boolean.FALSE;
                        break;

                    case VALUE_TRUE:
                        fieldValue = Boolean.TRUE;
                        break;

                    case VALUE_STRING:
                        fieldValue = jsonParser.getText();
                        break;

                    case VALUE_NUMBER_FLOAT:
                        fieldValue = jsonParser.getFloatValue();
                        break;

                    case VALUE_NUMBER_INT:
                        fieldValue = jsonParser.getIntValue();
                        break;

                    default:
                        fieldValue = null;
                        break;
                    }

                    responseModel.setFieldValue(currentName, fieldValue);
                }
            }

            token = jsonParser.nextToken();
        }

        jsonParser.close();

        return responseModel;
    }

    public Model write(Message requestMessage, Context context, URI schemaId) throws Exception {
        // TODO Write the flip side
        return null;
    }

    public Type getWrmlType(JsonToken jsonToken) {

        Type type;

        switch (jsonToken) {

        case START_ARRAY:
            type = Type.List;
            break;

        case START_OBJECT:
        case VALUE_EMBEDDED_OBJECT:
            type = Type.Model;
            break;

        case VALUE_FALSE:
        case VALUE_TRUE:
            type = Type.Boolean;
            break;

        case VALUE_STRING:
            type = Type.Text;
            break;

        case VALUE_NUMBER_FLOAT:
            type = Type.Double;
            break;

        case VALUE_NUMBER_INT:
            type = Type.Integer;
            break;

        default:
            type = Type.Native;
            break;
        }

        return type;

    }

}
