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

package org.wrml.core.model.schema;

import org.wrml.core.model.Owned;

/**
 * A common base interface for the components of a {@link Schema}, which are
 * {@link Constrainable} and {@link Owned} (by a Schema).
 * 
 * @param <M>
 *            a sub-interface of this interface
 */
public interface SchemaMember<M extends SchemaMember<M>> extends Constrainable<M>, Owned<Schema> {

}
