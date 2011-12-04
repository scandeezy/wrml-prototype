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

package org.wrml.temp;

import java.net.URI;

import org.wrml.AbstractModel;
import org.wrml.Context;

/**
 * An example class that will ultimately be auto-generated from a schema.
 * Decompile to help write the code generation code.
 */
public class AuthorImpl extends AbstractModel implements Author {

    private static final long serialVersionUID = 7470607037261843521L;

    public AuthorImpl(URI schemaId, URI resourceTemplateId, Context context) {
        super(schemaId, resourceTemplateId, context);
    }

    public String getFirstName() {
        return (String) getFieldValue("firstName");
    }

    public String setFirstName(String firstName) {
        return (String) setFieldValue("firstName", firstName);
    }

    public String getLastName() {
        return (String) getFieldValue("lastName");
    }

    public String setLastName(String lastName) {
        return (String) setFieldValue("lastName", lastName);
    }

}
