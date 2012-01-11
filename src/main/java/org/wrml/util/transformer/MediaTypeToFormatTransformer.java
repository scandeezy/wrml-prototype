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

import org.wrml.Model;
import org.wrml.model.format.Format;
import org.wrml.runtime.Context;
import org.wrml.service.Service;
import org.wrml.util.MediaType;

public class MediaTypeToFormatTransformer extends AbstractTransformer<MediaType, Format> {

    public MediaTypeToFormatTransformer(Context context) {
        super(context);
    }

    public Format aToB(MediaType bValue) {
        final Context context = getContext();

        URI formatId = null;
        final String formatIdString = bValue.getFormatIdString();
        if (formatIdString != null) {
            formatId = context.getUriToStringTransformer().bToA(formatIdString);
        }
        else {
            formatId = context.getFormatIdToMediaTypeTransformer().bToA(bValue);
        }

        final Service service = context.getService(Format.class);
        return (Format) ((Model) service.get(formatId, null, context.getMediaTypeToClassTransformer()
                .bToA(Format.class), null)).getStaticInterface();
    }

    public MediaType bToA(Format format) {

        if (format == null) {
            return null;
        }

        return format.getMediaType();
    }

}
