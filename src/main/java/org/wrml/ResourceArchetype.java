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

package org.wrml;

/**
 * Enumeration that captures the resource archetypes.
 */
public enum ResourceArchetype {

    DOCUMENT("Document", "document"), COLLECTION("Collection", "collection"), STORE("Store", "store"), CONTROLLER(
            "Controller",
            "controller");

    private final String _Title;
    private final String _Keyword;

    private ResourceArchetype(String title, String keyword) {
        _Title = title;
        _Keyword = keyword;
    }

    public final String getKeyword() {
        return _Keyword;
    }

    public final String getTitle() {
        return _Title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
