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

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A media type. Instances are immutable.
 * 
 * message://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
 * 
 * media-type = type "/" subtype *( ";" parameter ) type = token subtype = token
 */
public final class MediaType implements Unique<String>, Comparable<MediaType> {

    public static Comparator<MediaType> ALPHA_ORDER = new Comparator<MediaType>() {

        public int compare(final MediaType o1, final MediaType o2) {
            if (o1 == o2) {
                return 0;
            }

            final String id1 = o1.getId();
            final String id2 = o2.getId();

            return Compare.twoInsensitiveStrings(id1, id2);
        }

    };

    private static final String MEDIA_TYPE_REGEX_STRING = "^" + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)/"
            + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)( +" + "([a-zA-Z0-9!#$%^&\\*_\\-\\+{}\\|'.`~]+)=\"([^\"]*)\";)*$";

    private static final Pattern MEDIA_TYPE_REGEX_PATTERN = Pattern.compile(MEDIA_TYPE_REGEX_STRING);

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

    private final String _Type;
    private final String _Subtype;
    private final SortedMap<String, String> _Parameters;

    private String _Id;
    private String _IdPrefix;

    public MediaType(final String type, final String subtype) {

        this(type, subtype, null);
    }

    public MediaType(final String type, final String subtype, final Map<String, String> parameters) {

        _Type = type;
        _Subtype = subtype;
        _Parameters = (parameters == null) ? null : createParameterMap(parameters);
    }

    public final int compareTo(final MediaType o) {
        return ALPHA_ORDER.compare(this, o);
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        final MediaType that = (MediaType) o;

        if (!_Id.equals(that._Id)) {
            return false;
        }

        return true;
    }

    protected String generateIdParameterList() {
        final SortedMap<String, String> parameters = getParameters();
        final StringBuilder sb = new StringBuilder();
        if (parameters != null) {

        }
        return sb.toString();
    }

    public final String getId() {
        if (_Id == null) {
            _Id = getIdPrefix() + generateIdParameterList();
        }
        return _Id;
    }

    protected final String getIdPrefix() {
        if (_IdPrefix == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(getType().trim()).append('/').append(getSubtype().trim());
            _IdPrefix = sb.toString().toLowerCase();
        }
        return _IdPrefix;
    }

    public final SortedMap<String, String> getParameters() {
        return _Parameters;
    }

    public final String getSubtype() {
        return _Subtype;
    }

    public final String getType() {
        return _Type;
    }

    @Override
    public final int hashCode() {
        return _Id.hashCode();
    }

    @Override
    public final String toString() {
        return getId();
    }

}
