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

package org.wrml.communication.http;

import java.util.Comparator;
import java.util.Date;

import org.wrml.util.Compare;
import org.wrml.util.ObservableMap;

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

    public static final Comparator<Message> DATE_ORDER = new Comparator<Message>() {

        public int compare(final Message me, final Message you) {
            if (me == you) {
                return 0;
            }

            final ObservableMap<String, Header<?>> myHeaders = me.getEntity().getHeaders();
            final Header<Date> myDateHeader = (Header<Date>) myHeaders.get(CommonHeader.DATE.getName());
            final Date myDate = myDateHeader.getValue();

            final ObservableMap<String, Header<?>> yourHeaders = you.getEntity().getHeaders();
            final Header<Date> yourDateHeader = (Header<Date>) yourHeaders.get(CommonHeader.DATE.getName());
            final Date yourDate = yourDateHeader.getValue();

            if (myDate == yourDate) {
                return 0;
            }

            if (myDate == null) {
                return -1;
            }

            if (yourDate == null) {
                return 1;
            }

            return Compare.twoDates(myDate, yourDate);
        }

    };

    public Entity getEntity();

    public StartLine getStartLine();

    public MessageType getType();
}
