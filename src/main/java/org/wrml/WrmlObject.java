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
 * The main object that that is exposed to  clients design.   
 */
public interface WrmlObject extends Unique<String> {

    public void addEventListener(WrmlObjectEventListener listener);

    public void addFieldEventListener(Field<?> field, FieldEventListener<?> listener);

    public Object getFieldValue(String fieldName);

    public Bag<String, Object> getFieldValues();

    public Link getLink(String relationName);

    public Bag<String, Link> getLinks();

    public ResourceTemplate getResourceTemplate();

    public Schema getSchema();

    public void removeEventListener(WrmlObjectEventListener listener);

    public void removeFieldEventListener(Field<?> field, FieldEventListener<?> listener);

    public void setFieldValue(String fieldName, Object fieldValue);

    // Links may suffice on the server side (or not) but perhaps, at least a
    // "client-side" wrapper object should make the links appear more
    // like abstract "actions"? Meaning if the client already
    // owns the http connection, it can pretty well implement the hypermedia
    // engine of "clicking" links to trigger remote API calls via WRML-compliant
    // REST.

}
