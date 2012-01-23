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
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import org.wrml.Model;
import org.wrml.formatter.AbstractModelReader;
import org.wrml.runtime.Context;
import org.wrml.util.observable.ObservableMap;

public class JsonModelReader extends AbstractModelReader {

    private JsonParser _JsonParser;
    private String _CurrentName;

    @Override
    public void close() throws Exception {

        super.close();

        if (_JsonParser != null) {
            _JsonParser.close();
        }
    }

    public boolean hasNext() {
        return _JsonParser.hasCurrentToken();
    }

    public String next() {

        try {
            JsonToken token = _JsonParser.getCurrentToken();
            while (token != null) {

                if (token == JsonToken.END_OBJECT) {
                    getModelStack().pop();
                    token = _JsonParser.nextToken();
                    continue;
                }

                final String currentName = _JsonParser.getCurrentName();
                if ((currentName != null) && !currentName.equals(_CurrentName)) {
                    _CurrentName = currentName;
                    token = _JsonParser.nextToken();
                    return currentName;
                }
                token = _JsonParser.nextToken();
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

    @Override
    public void open(InputStream inputStream) throws Exception {
        if ((_JsonParser != null) && (_JsonParser.getInputSource() == inputStream)) {
            return;
        }

        super.open(inputStream);

        // TODO: reset other state?

        final JsonFactory jsonFactory = new JsonFactory();
        _JsonParser = jsonFactory.createJsonParser(inputStream);
        _JsonParser.nextToken();
        _CurrentName = null;

    }

    @Override
    protected Boolean readBooleanValue() throws Exception {
        final JsonToken token = _JsonParser.getCurrentToken();
        if ((token == JsonToken.VALUE_TRUE) || (token == JsonToken.VALUE_FALSE)) {
            return _JsonParser.getValueAsBoolean();
        }
        return null;
    }

    @Override
    protected Date readDateTimeValue(Context context, java.lang.reflect.Type nativeType) throws Exception {
        // TODO: Change to Joda DateTime?
        return (Date) readTextValue(context, Date.class);
    }

    @Override
    protected Double readDoubleValue() throws Exception {
        final JsonToken token = _JsonParser.getCurrentToken();
        if (token == JsonToken.VALUE_NUMBER_FLOAT) {
            return _JsonParser.getValueAsDouble();
        }
        return null;
    }

    @Override
    protected Integer readIntegerValue() throws Exception {
        final JsonToken token = _JsonParser.getCurrentToken();
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return _JsonParser.getValueAsInt();
        }
        return null;
    }

    @Override
    protected void readListElements(Context context, java.lang.reflect.Type elementNativeType, List<Object> list)
            throws Exception {

        JsonToken token = _JsonParser.getCurrentToken();
        if (token != JsonToken.START_ARRAY) {
            return;
        }

        token = _JsonParser.nextToken();

        while ((token != null) && (token != JsonToken.END_ARRAY)) {

            final Object element = readValue(context, elementNativeType);
            token = _JsonParser.nextToken();
            
            if (element == null) {
                break;
            }

            list.add(element);            
        }
    }

    @Override
    protected Long readLongValue() throws Exception {
        final JsonToken token = _JsonParser.getCurrentToken();
        if (token == JsonToken.VALUE_NUMBER_INT) {
            return _JsonParser.getValueAsLong();
        }
        return null;
    }

    @Override
    protected ObservableMap<Object, Object> readMapValue(Context context, java.lang.reflect.Type nativeType)
            throws Exception {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected Model readModelValue(Context context, java.lang.reflect.Type nativeType) throws Exception {

        final JsonToken token = _JsonParser.getCurrentToken();
        if (token == JsonToken.START_OBJECT) {
            //_JsonParser.nextToken();
            return readModel(context, nativeType);
        }

        return null;

    }

    @Override
    protected Object readNativeValue(Context context, java.lang.reflect.Type nativeType) throws Exception {
        final JsonToken token = _JsonParser.getCurrentToken();
        if (token == JsonToken.VALUE_EMBEDDED_OBJECT) {
            return _JsonParser.getEmbeddedObject();
        }
        return null;
    }

    @Override
    protected String readRawTextValue() throws Exception {
        final JsonToken token = _JsonParser.getCurrentToken();
        if (token == JsonToken.VALUE_STRING) {
            return _JsonParser.getText();
        }

        return null;
    }

}
