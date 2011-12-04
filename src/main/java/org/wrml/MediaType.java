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

package org.wrml;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wrml.util.Identifiable;

/**
 * A media type. Instances are immutable.
 * 
 * message://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
 * 
 * media-type = type "/" subtype *( ";" parameter ) type = token subtype = token
 */
public final class MediaType extends Identifiable<String> {

    private static final long serialVersionUID = 928356234498155596L;

    private static final String MEDIA_TYPE_REGEX_STRING = "^" + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)/"
            + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)( +" + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)=\"([^\"]*)\";)*$";

    private static final Pattern MEDIA_TYPE_REGEX_PATTERN = Pattern.compile(MEDIA_TYPE_REGEX_STRING);

    private static final String WRML_TYPE = "application";

    private static final String WRML_SUBTYPE = "wrml";

    private static final String WRML_PARAMETER_SCHEMA = "schema";

    private static final String WRML_PARAMETER_FORMAT = "format";

    public static final MediaType create(String mediaTypeId) {

        final Matcher matcher = MEDIA_TYPE_REGEX_PATTERN.matcher(mediaTypeId);

        final String type = matcher.group(0);
        final String subtype = matcher.group(1);

        // TODO: Finish and test

        return null;
    }

    private static final SortedMap<String, String> createParameterMap() {
        return createParameterMap(null);
    }

    private static final SortedMap<String, String> createParameterMap(Map<String, String> input) {

        final SortedMap<String, String> output = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

        if (input != null) {
            output.putAll(input);
        }

        return output;
    }

    private static final Map<String, String> createWrmlParameters(URI schemaId, URI formatId) {
        final HashMap<String, String> params = new HashMap<String, String>();
        params.put(WRML_PARAMETER_SCHEMA, String.valueOf(schemaId));
        params.put(WRML_PARAMETER_FORMAT, String.valueOf(formatId));
        return params;
    }

    private final String _Name;
    private final String _Type;
    private final String _Subtype;
    private final SortedMap<String, String> _Parameters;

    private URI _SchemaId;
    private URI _FormatId;

    public MediaType(final String type, final String subtype) {
        this(type, subtype, null);
    }

    public MediaType(final String type, final String subtype, final Map<String, String> parameters) {

        _Type = type;
        _Subtype = subtype;
        _Parameters = (parameters == null) ? null : createParameterMap(parameters);

        final StringBuilder sb = new StringBuilder();
        sb.append(getType().trim()).append('/').append(getSubtype().trim());

        // TODO: Append _Parameters

        _Name = sb.toString().toLowerCase();

        setId(_Name);
    }

    public MediaType(URI schemaId, URI formatId) {
        this(WRML_TYPE, WRML_SUBTYPE, createWrmlParameters(schemaId, formatId));
        _SchemaId = schemaId;
        _FormatId = formatId;
    }

    public URI getFormatId() {
        // TODO: If null lazily initialize (if application/wrml)                
        return _FormatId;
    }

    public final String getName() {
        return _Name;
    }

    public final SortedMap<String, String> getParameters() {
        return _Parameters;
    }

    public URI getSchemaId() {
        // TODO: If null lazily initialize (if application/wrml)
        return _SchemaId;
    }

    public final String getSubtype() {
        return _Subtype;
    }

    public final String getType() {
        return _Type;
    }

}
