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

package org.wrml.runtime.system.service.schema;

import java.net.URI;
import java.util.Map;

import org.wrml.Model;

public final class LinkPrototype {

    private final URI _Rel;
    private final java.lang.reflect.Type _NativeReturnType;

    public LinkPrototype(final URI rel, java.lang.reflect.Type nativeReturnType) {
        _Rel = rel;
        _NativeReturnType = nativeReturnType;
    }

    public Object clickLink(final Model targetModel, final Object requestEntity, final Map<String, String> hrefParams) {
        return targetModel.clickLink(_Rel, _NativeReturnType, requestEntity, hrefParams);
    }

    public final java.lang.reflect.Type getNativeReturnType() {
        return _NativeReturnType;
    }

    public final URI getRel() {
        return _Rel;
    }
}
