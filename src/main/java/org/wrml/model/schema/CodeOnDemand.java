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

package org.wrml.model.schema;

import java.net.URI;

import org.wrml.Model;
import org.wrml.model.Descriptive;
import org.wrml.model.Named;
import org.wrml.model.Titled;
import org.wrml.model.Versioned;

/*
 * WRML's Java framework should have a generalized design for how to
 * incorporate hooks to non-model (executable) Java code. Everything from
 * how it is packaged (Jar, Manifest file, etc) and design a REST API-based
 * exchange hidden behind WRML's Service JAVA API.
 */
public interface CodeOnDemand extends Named, Titled, Versioned, Descriptive, Model {

    public URI getCodeLocation();

    public URI setCodeLocation(URI codeLocation);

}
