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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.wrml.model.communication.MediaType;
import org.wrml.runtime.Context;
import org.wrml.util.observable.Observables;

public class MediaTypeToStringTransformer extends AbstractTransformer<MediaType, String> {

    public static final String MEDIA_TYPE_REGEX_STRING = "^" + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)/"
            + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)(;\\s+"
            + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)=\"([^\"]*)\")*$";

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
            return null;
        }

        return createMediaTypeString(bValue.getType(), bValue.getSubtype(), bValue.getParameters());
    }

    public MediaType bToA(String aValue) {

        if (aValue == null) {
            return null;
        }

        final Matcher matcher = MEDIA_TYPE_REGEX_PATTERN.matcher(aValue);

        if (!matcher.matches()) {
            return null;
        }

        final String type = matcher.group(1);
        final String subtype = matcher.group(2);

        // TODO: Finish regex parsing for parameters

        final SortedMap<String, String> parameters = null;

        MediaType mediaType = (MediaType) getContext().instantiateModel(MediaType.class, null, null)
                .getStaticInterface();
        mediaType.setFieldValue("type", type);
        mediaType.setFieldValue("subtype", subtype);
        mediaType.setFieldValue("parameters", (parameters != null) ? Observables.observableMap(parameters) : null);

        return mediaType;
    }

}
