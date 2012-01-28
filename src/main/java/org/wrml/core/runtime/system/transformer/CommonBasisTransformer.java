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

import org.wrml.core.runtime.Context;
import org.wrml.core.runtime.RuntimeObject;
import org.wrml.core.transformer.Transformer;

public abstract class CommonBasisTransformer<A, B> extends RuntimeObject implements Transformer<A, B> {

    public CommonBasisTransformer(final Context context) {
        super(context);
    }

}