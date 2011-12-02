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

package org.wrml.schema;

import java.net.URI;

import org.wrml.util.Identifiable;

/**
 * Base class for members of a schema.
 */
public abstract class Member<T extends Comparable<T>> extends Identifiable<T> {

    private static final long serialVersionUID = 7032315376220829674L;

    private final Schema _Schema;
    private final URI _DeclaredSchemaId;

    public Member(Schema schema, URI declaredSchemaId) {
        _Schema = schema;
        _DeclaredSchemaId = declaredSchemaId;
    }

    public final URI getDeclaredSchemaId() {
        return _DeclaredSchemaId;
    }

    public final Schema getSchema() {
        return _Schema;
    }

}
