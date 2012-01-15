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

import org.wrml.model.Descriptive;
import org.wrml.model.Document;
import org.wrml.model.Named;
import org.wrml.model.Versioned;
import org.wrml.util.observable.ObservableList;
import org.wrml.util.observable.ObservableMap;

public interface ChoiceMenu extends Named, Versioned, Descriptive, Document {

    public int getDefaultChoiceIndex();

    public ObservableList<String> getItems();

    public ObservableMap<String, String> getItemComment();

    public int setDefaultChoiceIndex(int defaultChoiceIndex);

}
