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

import org.wrml.model.Owned;
import org.wrml.model.relation.LinkRelation;
import org.wrml.util.MediaType;
import org.wrml.util.observable.ObservableList;

public interface Link extends Owned<Schema> {

    public URI getHref();

    public LinkRelation getRel();

    public URI getRelId();

    // Generated from Field
    //     Name: requestTypes 
    //     Value: List[Text[MediaType]]
    public ObservableList<MediaType> getRequestTypes();

    // Generated from Field
    //     Name: responseTypes 
    //     Value: List[Text[MediaType]]    
    public ObservableList<MediaType> getResponseTypes();

    public String getStateExpression();

    public URI setHref(URI href);

    /**
     * 
     * 
     * @param stateExpression
     *            The (optional) boolean expression that can be evaluated to
     *            determine the state of a link.
     * @return
     */
    public String setStateExpression(String stateExpression);

}
