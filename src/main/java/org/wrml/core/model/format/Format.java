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

package org.wrml.core.model.format;

import java.net.URI;

import org.wrml.core.Model;
import org.wrml.core.model.CodeOnDemand;
import org.wrml.core.model.Descriptive;
import org.wrml.core.model.Titled;
import org.wrml.core.model.Versioned;
import org.wrml.core.util.observable.ObservableList;
import org.wrml.core.www.MediaType;

public interface Format extends Titled, Versioned, Descriptive, Model {

    public ObservableList<CodeOnDemand> getFormatters();

    public URI getHomeUri();

    public MediaType getMediaType();

    public URI getRfcUri();

}
