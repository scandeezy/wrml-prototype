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

import org.wrml.AbstractWrmlObject;
import org.wrml.Context;

/**
 * An example class that will ultimately be auto-generated from a schema.
 * Decompile to help write the code generation code.
 */
public class StoryImpl extends AbstractWrmlObject implements Story {

    private static final long serialVersionUID = -3484324792379385843L;

    public StoryImpl(URI schemaId, URI resourceTemplateId, Context context) {
        super(schemaId, resourceTemplateId, context);
    }

    // Text Field example

    public String getHeadline() {
        return (String) getFieldValue("headline");
    }

    public String setHeadline(String headline) {
        return (String) setFieldValue("headline", headline);
    }

    // Boolean Field example

    public boolean isPremium() {
        return ((Boolean) getFieldValue("premium")).booleanValue();
    }

    public boolean setPremium(boolean premium) {
        return ((Boolean) setFieldValue("premium", premium ? Boolean.TRUE : Boolean.FALSE)).booleanValue();
    }

    // Schema Field example

    public Author getAuthor() {
        return (Author) getFieldValue("author");
    }

    public Author setAuthor(Author author) {
        return (Author) setFieldValue("author", author);
    }

}
