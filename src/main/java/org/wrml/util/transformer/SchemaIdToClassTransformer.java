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

package org.wrml.util.transformer;

import java.net.URI;

import org.wrml.runtime.Context;

public class SchemaIdToClassTransformer extends AbstractTransformer<URI, Class<?>> {

    public SchemaIdToClassTransformer(Context context) {
        super(context);
    }

    public Class<?> aToB(URI aValue) {

        Context context = getContext();

        String className = context.getSchemaIdToClassNameTransformer().aToB(aValue);

        System.out.println("Loading schema class: " + className);
        Class<?> schemaInterface = null;
        try {
            schemaInterface = context.loadClass(className);
        }
        catch (Throwable t) {
            // TODO Auto-generated catch block
            t.printStackTrace();
        }

        return schemaInterface;
    }

    public URI bToA(Class<?> bValue) {
        return getContext().getSchemaIdToClassNameTransformer().bToA(bValue.getCanonicalName());
    }
}