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

package org.wrml.core.transformer.tostring;

import java.net.URI;

import org.wrml.core.transformer.ConstantTransformation;

public class UriToStringTransformer implements ConstantTransformation<URI, String> {

    public String aToB(URI uri) {
        if (uri == null) {
            return null;
        }
        return String.valueOf(uri.normalize());
    }

    public URI bToA(String uriString) {
        return URI.create(uriString).normalize();
    }

}
