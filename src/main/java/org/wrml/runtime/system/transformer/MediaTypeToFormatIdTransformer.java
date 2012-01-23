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

package org.wrml.runtime.system.transformer;

import java.net.URI;

import org.wrml.runtime.Context;
import org.wrml.transformer.Transformers;
import org.wrml.www.MediaType;

public class MediaTypeToFormatIdTransformer extends ConstantTransformer<MediaType, URI> {

    public final URI _BaseUri;

    public MediaTypeToFormatIdTransformer(Context context, URI baseUri) {
        super(context);
        _BaseUri = baseUri;
    }

    public URI aToB(MediaType wrmlMediaType) {
        if (wrmlMediaType == null) {
            return null;
        }

        // TODO: This doesnt look finished...
        return getBaseUri().resolve(wrmlMediaType.getType() + '/' + wrmlMediaType.getSubtype());
    }

    public MediaType bToA(URI formatId) {
        if (formatId == null) {
            return null;
        }

        final String mediaTypeString = getBaseUri().relativize(formatId).toString();
        final Transformers<String> stringTransformers = getContext().getStringTransformers();
        return stringTransformers.getTransformer(MediaType.class).bToA(mediaTypeString);
    }

    public URI getBaseUri() {
        return _BaseUri;
    }

}
