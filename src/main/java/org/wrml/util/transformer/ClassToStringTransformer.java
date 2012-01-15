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

public class ClassToStringTransformer implements ConstantTransformation<Class<?>, String> {

    public String aToB(Class<?> clazz) {
        // TODO: Is this unique enough? Does parameterization matter and if so does this string include the <T>?
        return clazz.getCanonicalName();
    }

    public Class<?> bToA(String className) {

        // TODO: Is className formatted correctly for this?
        // TODO: Do we need to deal with a wrml ClassLoader here?

        try {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
