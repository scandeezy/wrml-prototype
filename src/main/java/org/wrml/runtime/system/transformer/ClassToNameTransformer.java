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

package org.wrml.runtime.system.transformer;

import org.wrml.runtime.Context;

public class ClassToNameTransformer extends ConstantTransformer<Class<?>, String> {

    public ClassToNameTransformer(Context context) {
        super(context);
    }

    public String aToB(Class<?> clazz) {
        return clazz.getCanonicalName();
    }

    public Class<?> bToA(String className) {

        // TODO: Is className formatted correctly for this?
        // TODO: Do we need to deal with a wrml ClassLoader here?

        System.out.println(this + ": Loading schema class: " + className);

        if (className == null) {
            return null;
        }

        Class<?> schemaInterface = null;

        try {

            // TODO: Does this class loading make sense here?
            schemaInterface = getContext().loadClass(className);
            System.out.println(this + ": Loaded schema class: " + schemaInterface);

        }
        catch (final Throwable t) {

            // TODO: Handle this better

            t.printStackTrace();
            return null;
        }

        return schemaInterface;
    }

}
