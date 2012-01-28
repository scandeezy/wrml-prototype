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
 * http://www.w3.org/Protocols/rfc2616/rfc2616-sec3.html#sec3.1
 */

/*
 * TODO: Refactor into a model (here) and a
 * service (org.wrml.core.service.communication.http.HttpVersionService)
 */

// Generated from a Web Resource Schema
public enum HttpVersion {

    HTTP_1_0(1, 0),
    HTTP_1_1(1, 1);

    public static HttpVersion fromString(String httpVersionString) {

        // Quick, dirty but simple. No regex needed.
        if ((httpVersionString == null) || (httpVersionString.length() != 8)
                || !httpVersionString.startsWith("HTTP/1.")) {
            throw new IllegalArgumentException("Invalid HTTP version.");
        }

        if (httpVersionString.endsWith("0")) {
            return HTTP_1_0;
        }

        if (httpVersionString.endsWith("1")) {
            return HTTP_1_1;
        }
        else {
            throw new IllegalArgumentException("Unsupported HTTP version.");
        }
    }

    private final long _MajorNumber;
    private final long _MinorNumber;

    private HttpVersion(final long majorNumber, final long minorNumber) {

        _MajorNumber = majorNumber;
        _MinorNumber = minorNumber;
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
