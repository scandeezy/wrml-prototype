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

package org.wrml.formatter.json;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import org.wrml.Model;
import org.wrml.event.Event;
import org.wrml.formatter.FieldIterativeModelReader;
import org.wrml.formatter.FieldIterativeModelReader.EventListener.EventNames;
import org.wrml.runtime.Context;
import org.wrml.runtime.ModelGraph;
import org.wrml.util.observable.ObservableMap;

public class JsonParserModelReader extends FieldIterativeModelReader {

    private JsonParser _JsonParser;
    private String _CurrentName;
    private Event<JsonParserModelReader> _Event;

    public void close() throws Exception {

        if (_JsonParser != null) {
            _JsonParser.close();
        }
    }

    public boolean hasNext() {
        return _JsonParser.hasCurrentToken();
    }

    public String next() {

        try {
            JsonToken token = getCurrentToken();
            while (token != null) {
                final String currentName = getCurrentName();
                token = getNextToken();

                if (token == JsonToken.END_OBJECT) {
                    return null;
                }

                if ((currentName != null) && !currentName.equals(_CurrentName)) {
                    _CurrentName = currentName;
                    return currentName;
                }
            }
        }
        catch (final JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public void open(InputStream inputStream) throws Exception {
        if ((_JsonParser != null) && (_JsonParser.getInputSource() == inputStream)) {
            return;
        }

        // TODO: reset other state?

        final JsonFactory jsonFactory = new JsonFactory();
        _JsonParser = jsonFactory.createJsonParser(inputStream);

        _Event = new Event<JsonParserModelReader>(this);

        getNextToken();
        _CurrentName = null;

    }

    protected String getCurrentName() throws JsonParseException, IOException {
        return _JsonParser.getCurrentName();
    }

    protected JsonToken getCurrentToken() {
        return _JsonParser.getCurrentToken();
    }

    protected JsonToken getNextToken() throws JsonParseException, IOException {
        final JsonToken token = _JsonParser.nextToken();

        // TODO: Call fireEndOfCursorEvent (or something?) to communicate to the event's ModelGraph-aware handler that the cursor should pop back. 

        final String currentName = getCurrentName();
        System.out.println("WAKA WAKA!! READING JSON TOKEN: \"" + token + "\" (\"" + _JsonParser.getText()
                + "\") following current name: " + currentName);

        if (token == JsonToken.END_OBJECT) {
            fireEvent(EventNames.endReadModel, _Event);
        }

        return token;
    }

    @Override
    protected Boolean readBooleanValue() throws Exception {
        final JsonToken token = getCurrentToken();
        if ((token == JsonToken.VALUE_TRUE) || (token == JsonToken.VALUE_FALSE)) {
            return _JsonParser.getValueAsBoolean();
        }

        throw new IllegalStateException("Cannot read a Boolean from a token of type: " + token + "\" (\""
                + _JsonParser.getText() + "\"");
    }

    @Override
    protected Date readDateTimeValue(Context context, java.lang.reflect.Type nativeType) throws Exception {
        // TODO: Change to Joda DateTime?
        return (Date) readTextValue(context, Date.class);
    }

    @Override
    protected Double readDoubleValue() throws Exception {
        final JsonToken token = getCurrentToken();
        if (token == JsonToken.VALUE_NUMBER_FLOAT) {
            return _JsonParser.getValueAsDouble();
        }

        throw new IllegalStateException("Cannot read a Double from a token of type: " + token + "\" (\""
                + _JsonParser.getText() + "\"");
    }

    @Override
    protected Integer readIntegerValue() throws Exception {
        final JsonToken token = getCurrentToken();
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return _JsonParser.getValueAsInt();
        }

        throw new IllegalStateException("Cannot read an Integer from a token of type: " + token + "\" (\""
                + _JsonParser.getText() + "\"");
    }

    @Override
    protected void readListElements(Context context, java.lang.reflect.Type elementNativeType, ModelGraph modelGraph,
            List<Object> list) throws Exception {

        JsonToken token = getCurrentToken();
        if (token != JsonToken.START_ARRAY) {
            return;
        }

        System.out.println("A List field COULD read an element value from JSON token: \"" + token + "\" (\""
                + _JsonParser.getText() + "\") while building a list of: " + elementNativeType);

        token = getNextToken();

        System.out.println("A List field COULD read an element value from JSON token: \"" + token + "\" (\""
                + _JsonParser.getText() + "\") while building a list of: " + elementNativeType);

        while ((token != null) && (token != JsonToken.END_ARRAY)) {

            System.out.println("A List field IS attempting to read an element value from JSON token: \"" + token
                    + "\" (\"" + _JsonParser.getText() + "\") while building a list of: " + elementNativeType);

            if (token == JsonToken.END_OBJECT) {
                continue;
            }

            final Object element = readValue(context, elementNativeType, modelGraph);

            if (element == null) {
                break;
            }

            list.add(element);

            token = getNextToken();

            System.out.println("A List field COULD read an element value from JSON token: \"" + token + "\" (\""
                    + _JsonParser.getText() + "\") while building a list of: " + elementNativeType);
        }
    }

    @Override
    protected Long readLongValue() throws Exception {
        final JsonToken token = getCurrentToken();
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return _JsonParser.getValueAsLong();
        }

        throw new IllegalStateException("Cannot read a Long from a token of type: " + token + "\" (\""
                + _JsonParser.getText() + "\"");
    }

    @Override
    protected ObservableMap<Object, Object> readMapValue(Context context, java.lang.reflect.Type nativeType,
            ModelGraph modelGraph) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Model readModelValue(Context context, Type nativeType, ModelGraph modelGraph) throws Exception {
        final JsonToken token = getCurrentToken();
        if (token == JsonToken.START_OBJECT) {
            return readModelToGraph(context, nativeType, modelGraph);
        }

        throw new IllegalStateException("Cannot read a Model of type \"" + nativeType + "\" from a token of type: "
                + token + "\" (\"" + _JsonParser.getText() + "\"");
    }

    @Override
    protected Object readNativeValue(Context context, java.lang.reflect.Type nativeType) throws Exception {
        final JsonToken token = getCurrentToken();
        if (token == JsonToken.VALUE_EMBEDDED_OBJECT) {
            return _JsonParser.getEmbeddedObject();
        }
        return null;
    }

    @Override
    protected String readRawTextValue() throws Exception {
        final JsonToken token = getCurrentToken();
        if (token == JsonToken.VALUE_STRING) {
            return _JsonParser.getText();
        }

        throw new IllegalStateException("Cannot read Text from a token of type: " + token + "\" (\""
                + _JsonParser.getText() + "\"");
    }

}
