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

package org.wrml.service.runtime;

import java.net.URI;

import org.wrml.Context;
import org.wrml.service.ProxyService;
import org.wrml.service.Service;
import org.wrml.util.UriTransformer;

/**
 * The WRML equivalent of the SystemClassLoader
 */
public class SystemSchemaService extends ProxyService implements SchemaService {

    public static final URI DEFAULT_SCHEMA_API_DOCROOT = URI.create("http://api.schemas.wrml.org/");

    private UriTransformer<String> _UriTransformer;

    public SystemSchemaService(Context context, Service originService) {
        super(context, originService);
    }

    @Override
    public final UriTransformer<String> getIdTransformer() {
        if (_UriTransformer == null) {
            _UriTransformer = createIdTransformer();
        }
        return _UriTransformer;
    }

    public ClassLoader getSchemaInterfaceLoader() {
        //TODO: Replace with auto-generation class loader 
        return getClass().getClassLoader();
    }

    protected UriTransformer<String> createIdTransformer() {
        return new SchemaClassNameUriTransformer(DEFAULT_SCHEMA_API_DOCROOT);
    }

}
