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

package org.wrml.util;

import org.wrml.util.observable.ObservableMap;
import org.wrml.util.transformer.MediaTypeToStringTransformer;

/**
 * A media type. Instances are immutable.
 * 
 * message://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
 * 
 * media-type = type "/" subtype *( ";" parameter ) type = token subtype = token
 */
public class MediaType {

    private final String _Type;
    private final String _Subtype;
    private final ObservableMap<String, String> _Parameters;

    private String _String;

    public MediaType(String type, String subtype, ObservableMap<String, String> parameters) {
        _Type = type;
        _Subtype = subtype;
        _Parameters = parameters;
    }

    public String getType() {
        return _Type;
    }

    public String getSubtype() {
        return _Subtype;
    }

    public ObservableMap<String, String> getParameters() {
        return _Parameters;
    }

    @Override
    public String toString() {
        if (_String == null) {
            _String = MediaTypeToStringTransformer.createMediaTypeString(getType(), getSubtype(), getParameters());
        }

        return _String;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_Parameters == null) ? 0 : _Parameters.hashCode());
        result = prime * result + ((_Subtype == null) ? 0 : _Subtype.hashCode());
        result = prime * result + ((_Type == null) ? 0 : _Type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MediaType other = (MediaType) obj;
        if (_Parameters == null) {
            if (other._Parameters != null)
                return false;
        }
        else if (!_Parameters.equals(other._Parameters))
            return false;
        if (_Subtype == null) {
            if (other._Subtype != null)
                return false;
        }
        else if (!_Subtype.equals(other._Subtype))
            return false;
        if (_Type == null) {
            if (other._Type != null)
                return false;
        }
        else if (!_Type.equals(other._Type))
            return false;
        return true;
    }



}
