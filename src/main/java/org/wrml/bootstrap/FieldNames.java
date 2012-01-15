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

package org.wrml.bootstrap;

public class FieldNames {

    public static enum Descriptive {
        description;
    }

    public static enum Document {
        etag,
        id,
        readOnly,
        secondsToLive;
    }

    public static enum Field {
        defaultValue,
        hidden,
        local,
        readOnly,
        required;
    }

    public static enum Link {
        href,
        relId,
        requestTypes,
        responseTypes;
    }

    public static enum Named {
        name;
    }

    public static enum Owned {
        owner;
    }

    public static enum Schema {
        baseSchemaIds,
        fields,
        links;
    }

    public static enum Constrainable {
        constraints;
    }

    public static enum Titled {
        title;
    }

    public static enum Typed {
        type;
    }

    public static enum Versioned {
        version;
    }

}
