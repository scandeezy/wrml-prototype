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

package org.wrml.core.runtime.system.transformer;

import java.net.URI;

import org.wrml.core.runtime.Context;
import org.wrml.core.transformer.Transformers;
import org.wrml.core.www.MediaType;

public class MediaTypeToSchemaIdTransformer extends ConstantTransformer<MediaType, URI> {

    public MediaTypeToSchemaIdTransformer(Context context) {
        super(context);
    }

    public URI aToB(MediaType mediaType) {
        if (mediaType == null) {
            return null;
        }

        final Transformers<String> stringTransformers = getContext().getStringTransformers();
        return stringTransformers.getTransformer(URI.class).bToA(mediaType.getSchemaIdString());
    }

    public MediaType bToA(URI schemaId) {
        if (schemaId == null) {
            return null;
        }

        final Transformers<String> stringTransformers = getContext().getStringTransformers();
        final String schemaIdString = stringTransformers.getTransformer(URI.class).aToB(schemaId);
        final String mediaTypeString = MediaType.createWrmlMediaTypeString(schemaIdString, null);
        return stringTransformers.getTransformer(MediaType.class).bToA(mediaTypeString);

    }

}
