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

package org.wrml.util.transformer;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wrml.runtime.Context;
import org.wrml.util.MediaType;
import org.wrml.util.observable.Observables;

public class MediaTypeToStringTransformer extends AbstractTransformer<MediaType, String> {

    public static final String MEDIA_TYPE_REGEX_STRING = "^" + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)/"
            + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)(;\\s+"
            + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)=\"([^\"]*)\")*";

    public static final Pattern MEDIA_TYPE_REGEX_PATTERN = Pattern.compile(MEDIA_TYPE_REGEX_STRING);

    public static String createMediaTypeString(final String type, final String subtype,
            final Map<String, String> parameters) {

        final StringBuilder sb = new StringBuilder();
        sb.append(type.trim()).append('/').append(subtype.trim());
        appendParameters(sb, parameters);
        return sb.toString();
    }

    public static void appendParameters(final StringBuilder sb, final Map<String, String> parameters) {
        if (parameters == null || parameters.size() == 0) {
            return;
        }

        for (String parameterName : parameters.keySet()) {
            sb.append("; ").append(parameterName.trim()).append('=').append('\"')
                    .append(parameters.get(parameterName).trim()).append('\"');
        }
    }

    public MediaTypeToStringTransformer(Context context) {
        super(context);
    }

    public String aToB(MediaType bValue) {

        if (bValue == null) {
            System.out.println("MediaTypeToStringTransformer.aToB(" + bValue + ") returning: null");
            return null;
        }

        String aValue = createMediaTypeString(bValue.getType(), bValue.getSubtype(), bValue.getParameters());

        System.out.println("MediaTypeToStringTransformer.aToB(" + bValue + ") returning: " + aValue);
        return aValue;

    }

    public MediaType bToA(String aValue) {

        //System.out.println("MediaTypeToStringTransformer.bToA(" + aValue + ")");

        if (aValue == null) {
            //System.out.println("MediaTypeToStringTransformer.bToA(" + aValue + ") returning: null");
            return null;
        }

        final Matcher matcher = MEDIA_TYPE_REGEX_PATTERN.matcher(aValue);

        // TODO: NOTE: WARNING: I don't think the current regex works when there is more than one media type parameter
        if (!matcher.matches()) {
            //System.out.println("MediaTypeToStringTransformer.bToA(" + aValue + ") returning: null");
            return null;
        }

        final String type = matcher.group(1);
        final String subtype = matcher.group(2);

        //        System.out.println("matcher.group(0) - " + matcher.group(0));
        //        System.out.println("matcher.group(1) - " + matcher.group(1));
        //        System.out.println("matcher.group(2) - " + matcher.group(2));
        //        System.out.println("matcher.group(3) - " + matcher.group(3));
        //        System.out.println("matcher.group(4) - " + matcher.group(4));
        //        System.out.println("matcher.group(5) - " + matcher.group(5));

        final String schemaIdString = matcher.group(5);

        SortedMap<String, String> parameters = null;
        if (schemaIdString != null) {
            parameters = new TreeMap<String, String>();
            parameters.put(MediaTypeToClassTransformer.PARAMETER_NAME_SCHEMA, schemaIdString);
        }

        MediaType mediaType = new MediaType(type, subtype, (parameters != null) ? Observables.observableMap(parameters)
                : null);

        //System.out.println("MediaTypeToStringTransformer.bToA(" + aValue + ") returning: " + mediaType);

        return mediaType;
    }

}
