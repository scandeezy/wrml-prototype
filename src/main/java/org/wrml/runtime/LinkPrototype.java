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

package org.wrml.runtime;

import java.net.URI;
import java.util.Map;

import org.wrml.Model;
import org.wrml.model.communication.MediaType;

final class LinkPrototype {

    private final URI _Rel;
    private final MediaType _ResponseMediaType;

    public LinkPrototype(final URI rel, final MediaType responseMediaType) {
        _Rel = rel;
        _ResponseMediaType = responseMediaType;
    }

    public Object clickLink(final Model targetModel, final Object requestEntity, final Map<String, String> hrefParams) {
        return targetModel.clickLink(_Rel, _ResponseMediaType, requestEntity, hrefParams);
    }

    public final URI getRel() {
        return _Rel;
    }

    public final MediaType getResponseMediaType() {
        return _ResponseMediaType;
    }
}
