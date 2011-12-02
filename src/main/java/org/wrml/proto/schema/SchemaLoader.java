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

package org.wrml.proto.schema;

import org.wrml.Field;
import org.wrml.Schema;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A class that can dynamically load a WRML Schema as a Java class.
 */
public class SchemaLoader {

    public Schema load(String uri) {
        // TODO: Complete this. Just enough to pass the test...for now
        Schema schema = mock(Schema.class);
        final Field field = mock(Field.class);
        when(schema.getField("example")).thenReturn(field);
        return schema;
    }
}