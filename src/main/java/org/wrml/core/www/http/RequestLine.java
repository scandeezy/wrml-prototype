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

package org.wrml.core.www.http;

import java.net.URI;

/**
 * <blockquote cite="message://www.w3.org/Protocols/rfc2616/rfc2616-sec5
 * .html#sec5.1"> Request-Line = Method SP Request-URI SP HTTP-HttpVersion CRLF
 * </blockquote>
 */
// Generated from a Web Resource Schema
public interface RequestLine extends StartLine {

    public Method getMethod();

    public URI getUri();

    public void setMethod(Method method);

    public void setUri(URI uri);
}
