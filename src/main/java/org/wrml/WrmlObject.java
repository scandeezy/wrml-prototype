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

public interface WrmlObject {

    public void addEventListener(WrmlObjectEventListener listener);

    public Object getFieldValue(String fieldName);

    public Bag<String, Object> getFieldValues();

    public Link getLink(String relationName);

    public LinkFormula getLinkFormula(String relationName);

    public Bag<String, LinkFormula> getLinkFormulas();

    public Bag<String, Link> getLinks();

    public ResourceTemplate getResourceTemplate();

    public Schema getSchema();

    public void removeEventListener(WrmlObjectEventListener listener);

    public void setFieldValue(String fieldName, Object fieldValue);

}
