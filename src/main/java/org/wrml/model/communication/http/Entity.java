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

import org.wrml.Model;
import org.wrml.util.ObservableMap;

/**
 * <blockquote cite="message://www.w3.org/Protocols/rfc2616/rfc2616-sec4
 * .html#sec4.2 "> The order in which header fields with differing field names
 * are received is not significant. However, it is "good practice" to send
 * general-header fields first, followed by request-header or response- header
 * fields, and ending with the entity-header fields. </blockquote>
 */
// Generated from a Web Resource Schema
public interface Entity extends Model {

    public Body getBody();

    public ObservableMap<String, Header<?>> getHeaders();
}
