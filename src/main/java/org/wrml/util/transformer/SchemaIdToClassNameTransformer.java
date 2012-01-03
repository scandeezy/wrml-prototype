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

import java.net.URI;

import org.wrml.runtime.Context;

public class SchemaIdToClassNameTransformer extends AbstractTransformer<URI, String> {

    public final URI _BaseUri;

    public SchemaIdToClassNameTransformer(Context context, URI baseUri) {
        super(context);
        _BaseUri = baseUri;
    }

    public String aToB(URI aValue) {
        return getBaseUri().relativize(aValue).toString().replace('/', '.');
    }

    public URI bToA(String bValue) {
        return getBaseUri().resolve(bValue.toString().replace('.', '/'));
    }

    public URI getBaseUri() {
        return _BaseUri;
    }

}
