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

package org.wrml.runtime.service;

import java.net.URI;

import org.wrml.runtime.Context;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.util.transformer.Transformer;

/**
 * The WRML equivalent of the SystemClassLoader
 */
public class SystemSchemaService extends ProxyService implements Service {

    public SystemSchemaService(Context context, Service originService) {
        super(context, originService);
    }

    @Override
    public final Transformer<URI, String> getIdTransformer() {
        return getContext().getSchemaIdToClassNameTransformer();
    }

}
