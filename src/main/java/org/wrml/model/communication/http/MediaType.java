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

package org.wrml.model.communication.http;

import java.util.SortedMap;

import org.wrml.Model;

/**
 * A media type. Instances are immutable.
 * 
 * message://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.7
 * 
 * media-type = type "/" subtype *( ";" parameter ) type = token subtype = token
 */
public interface MediaType extends Model {

    public String getType();

    public String getSubtype();

    public SortedMap<String, String> getParameters();

}
