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

/**
 * <a rel="rfc" href="message://www.w3.org/Protocols/rfc2616/rfc2616-sec4
 * .html#sec4">RFC</a> <code>
 * generic-message = start-line
 *                   *(message-header CRLF)
 *                   CRLF
 *                   [ message-body ]
 * start-line      = Request-Line | Status-Line
 * </code>
 */
public interface Message {

    public Entity getEntity();

    public StartLine getStartLine();

    public MessageType getType();

    public void setEntity(Entity entity);

    public void setStartLine(StartLine startLine);

    public void setType(MessageType messageType);
}
