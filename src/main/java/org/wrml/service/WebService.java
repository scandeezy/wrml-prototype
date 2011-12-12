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

package org.wrml.service;

import java.net.URI;

import org.wrml.Context;
import org.wrml.Model;
import org.wrml.model.resource.Action;
import org.wrml.model.schema.Schema;
import org.wrml.util.UriTransformer;

/*
 * TODO: Implement REST client
 */
public class WebService extends AbstractExecutableService {

    private final URI _DocrootUri;

    public WebService(Context context, URI docrootUri) {
        super(context);
        _DocrootUri = docrootUri;
    }

    public UriTransformer getIdTransformer(Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

    public Model put(URI id, Model modelToSave, Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

    public Model remove(URI id, Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

    public URI getDocrootUri() {
        return _DocrootUri;
    }

    public Model create(URI id, Model requestor) {
        return null;
    }

    public Model execute(URI id, Action action, Model requestor) {
        // TODO Auto-generated method stub
        return null;
    }

}
