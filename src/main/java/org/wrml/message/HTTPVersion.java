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

package org.wrml.message;

import org.wrml.Unique;

/**
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1
 */
public enum HTTPVersion implements Unique<HTTPVersion> {

    HTTP_1_0(1, 0), HTTP_1_1(1, 1);

    public final static HTTPVersion fromString(String httpVersionString) {

        // MSMTODO: Implement with regex parsing
        // MSMTODO: If 1.0 return the HTTP_1_0 const
        // MSMTODO: Else If 1.1 return the HTTP_1_1 const
        // MSMTODO: Else throw an unsupported version exception or something

        return null;
    }

    private final long _MajorNumber;
    private final long _MinorNumber;

    private HTTPVersion(final long majorNumber, final long minorNumber) {

        _MajorNumber = majorNumber;
        _MinorNumber = minorNumber;
    }

    public final HTTPVersion getId() {
        return this;
    }

    public final long getMajorNumber() {
        return _MajorNumber;
    }

    public final long getMinorNumber() {
        return _MinorNumber;
    }

    @Override
    public final String toString() {
        return new StringBuilder().append("HTTP/").append(getMajorNumber()).append('.').append(getMinorNumber())
                .toString();
    }

}
