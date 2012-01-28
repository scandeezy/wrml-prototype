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
 * message://www.w3.org/Protocols/rfc2616/rfc2616-sec9.html
 */
// Generated from a Web Resource Schema
public enum Method {

    GET("GET", true, true),
    HEAD("HEAD", true, true),
    POST("POST", false, false),
    PUT("PUT", false, true),
    DELETE("DELETE", false, true),
    OPTIONS("OPTIONS", false, true),
    TRACE("TRACE", false, true),
    CONNECT("CONNECT", false, false);

    private final String _Name;
    private final boolean _Safe;
    private final boolean _Idempotent;

    private Method(final String name, final boolean safe, final boolean idempotent) {

        _Name = name;
        _Safe = safe;
        _Idempotent = idempotent;
    }

    public String getName() {
        return _Name;
    }

    public boolean isIdempotent() {
        return _Idempotent;
    }

    public boolean isSafe() {
        return _Safe;
    }

    @Override
    public String toString() {
        return getName();
    }

}
