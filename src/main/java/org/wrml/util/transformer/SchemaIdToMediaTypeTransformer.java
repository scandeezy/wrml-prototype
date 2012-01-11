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
import org.wrml.util.MediaType;

public class SchemaIdToMediaTypeTransformer extends AbstractTransformer<URI, MediaType> {

    public SchemaIdToMediaTypeTransformer(Context context) {
        super(context);
    }

    public MediaType aToB(URI aValue) {
        if (aValue == null) {
            return null;
        }

        final String schemaIdString = getContext().getUriToStringTransformer().aToB(aValue);
        final String mediaTypeString = MediaType.createWrmlMediaTypeString(schemaIdString, null);
        return getContext().getMediaTypeToStringTransformer().bToA(mediaTypeString);
    }

    public URI bToA(MediaType bValue) {
        if (bValue == null) {
            return null;
        }

        return getContext().getUriToStringTransformer().bToA(bValue.getSchemaIdString());
    }

}
