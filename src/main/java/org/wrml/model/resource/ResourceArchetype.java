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

package org.wrml.model.resource;

/**
 * Enumeration that captures the resource archetypes.
 */
//Generated from a Web Resource Schema
public enum ResourceArchetype {

    DOCUMENT("Document"),
    COLLECTION("Collection"),
    STORE("Store"),
    ACTION("Action");

    private final String _Title;

    private ResourceArchetype(String title) {
        _Title = title;
    }

    public final String getTitle() {
        return _Title;
    }

    @Override
    public String toString() {
        return getTitle();
    }
}
