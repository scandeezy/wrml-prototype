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

package org.wrml.core.www.http;

import java.io.InputStream;
import java.lang.reflect.Type;

import org.wrml.core.Model;
import org.wrml.core.io.ModelGraphReader;
import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.system.transformer.SystemTransformers;
import org.wrml.core.www.MediaType;

public class DefaultFormatter implements Formatter {

    public Model read(Context context, Message requestMessage, Message responseMessage) throws Exception {

        //final RequestLine requestLine = (RequestLine) requestMessage.getStartLine();
        //final URI id = requestLine.getUri();

        final Entity entity = responseMessage.getEntity();
        final Body body = entity.getBody();
        final InputStream inputStream = body.getInputStream();
        final MediaType mediaType = entity.getHeaders().getContentType();

        final ModelGraphReader reader = context.createModelReader(mediaType, inputStream);

        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final Type nativeType = systemTransformers.getMediaTypeToNativeTypeTransformer().aToB(mediaType);
        final Model responseModel = reader.readModelGraph(context, nativeType).getRoot();

        reader.close();

        return responseModel;
    }

    public void write(Context context, Message requestMessage, Model model) throws Exception {
        // TODO Auto-generated method stub

    }

}
