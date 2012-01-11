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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.wrml.util.http.RequestLine;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;
import org.wrml.util.observable.Observables;

public class JsonFormatter extends AbstractFormatter {

    public JsonFormatter(Context context) {
        super(context);
    }

    @SuppressWarnings("unchecked")
    public <M extends Model> M read(Context context, Message requestMessage, URI schemaId, Message responseMessage)
            throws Exception {

        final RequestLine requestLine = (RequestLine) requestMessage.getStartLine();
        final URI id = requestLine.getUri();

        final Entity entity = responseMessage.getEntity();
        final Body body = entity.getBody();
        final InputStream inputStream = body.getInputStream();
        final JsonFactory jsonFactory = new JsonFactory();
        final JsonParser jsonParser = jsonFactory.createJsonParser(inputStream);
        final JsonToken jsonToken = jsonParser.nextToken();

        Model responseModel = readModel(context, jsonParser, schemaId, jsonToken);
        responseModel.setFieldValue("id", id);
        jsonParser.close();

        return (M) responseModel;
    }

    public void write(Context context, Message requestMessage, URI schemaId, Model requestModel) throws Exception {
        // TODO Write the flip side for PUT and POST

    }

    @SuppressWarnings("unchecked")
    private <E> ObservableList<E> readList(Context context, JsonParser jsonParser, JsonToken jsonToken, List<E> list)
            throws Exception {

        while (jsonToken != null) {
            jsonToken = jsonParser.nextToken();

            if (jsonToken.equals(JsonToken.END_ARRAY)) {
                break;
            }

            list.add((E) readWrmlValueFromJsonToken(context, jsonParser, jsonToken));

        }

        return Observables.observableList(list);
    }

    private <K, V> ObservableMap<K, V> readMap(Context context, JsonParser jsonParser, JsonToken jsonToken,
            Map<K, V> map) throws Exception {

        return Observables.observableMap(map);
    }

    @SuppressWarnings("unchecked")
    private <M extends Model> M readModel(Context context, JsonParser jsonParser, URI schemaId, JsonToken jsonToken)
            throws Exception {

        Model model = context.instantiateModel(schemaId);

        System.out.println("JsonFormatter - read: A JSON model with schema: \"" + schemaId + "\" for model: " + model);

        final Schema schema = context.getSchema(schemaId);
        final ObservableMap<String, Field> fields = schema.getFields();

        while (jsonToken != null) {

            String currentName = jsonParser.getCurrentName();

            if (currentName != null) {
                if ("links".equals(currentName)) {
                    // TODO: !!!!----- Deal with links
                }
                else {
                    jsonToken = jsonParser.nextToken();

                    currentName = jsonParser.getCurrentName();
                    if (currentName == null) {
                        continue;
                    }

                    if (!fields.containsKey(currentName)) {
                        System.err.println("JsonFormatter - read: Field  \"" + currentName
                                + "\" is unknown to schema: " + schemaId);
                        continue;
                    }

                    final Field field = fields.get(currentName);
                    Object fieldValue = readWrmlValueFromJsonToken(context, jsonParser, jsonToken, field);
                    model.setFieldValue(currentName, fieldValue);
                }
            }

            jsonToken = jsonParser.nextToken();
        }

        return (M) model;
    }

    private Object readWrmlValueFromJsonToken(Context context, final JsonParser jsonParser, final JsonToken jsonToken)
            throws Exception {

        String currentName = jsonParser.getCurrentName();

        System.out.println("JsonFormatter: currentName = \"" + currentName + "\", token = \"" + jsonToken + "\"");

        Object tokenValue = null;

        switch (jsonToken) {

        case START_ARRAY:

            tokenValue = readList(context, jsonParser, jsonToken, new ArrayList<Object>());

            break;

        case VALUE_EMBEDDED_OBJECT:

            // TODO: HACK temporary for debug/testing the boot sequence

            URI schemaId = context.getSchemaIdToClassTransformer().bToA(Field.class);
            tokenValue = readModel(context, jsonParser, schemaId, jsonToken);

            break;

        case VALUE_FALSE:
            tokenValue = Boolean.FALSE;
            break;

        case VALUE_TRUE:
            tokenValue = Boolean.TRUE;
            break;

        case VALUE_STRING:
            tokenValue = jsonParser.getText();
            break;

        case VALUE_NUMBER_FLOAT:
            tokenValue = jsonParser.getFloatValue();
            break;

        case VALUE_NUMBER_INT:
            tokenValue = jsonParser.getIntValue();
            break;

        default:
            tokenValue = null;
            break;
        }

        return tokenValue;

    }

    private Object readWrmlValueFromJsonToken(Context context, final JsonParser jsonParser, final JsonToken jsonToken,
            final Field field) throws Exception {

        final Type type = field.getType();
        String fieldName = field.getName();

        System.out.println("JsonFormatter: currentName = \"" + fieldName + "\", token = \"" + jsonToken + "\""
                + "\", WRML type = \"" + type);

        return readWrmlValueFromJsonToken(context, jsonParser, jsonToken);
    }

}
