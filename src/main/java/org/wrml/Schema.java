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

import java.util.List;

/**
 * Schemas are one of WRML's main ideas. Like Java's generics, schemas add
 * parameterized type information to the representations (of various formats)
 * that are traded back and forth between programs on the Web.
 * 
 * So if application/json is like Java's pre-generics collection API where type
 * cfrom java.lang.Object to
 * 
 * Note: This is a metadata class - instances should be edited with tools and
 * persisted for reuse.
 */
public interface Schema extends Unique<String> { // TODO: What should schema's

    // key be (String or URI)?

    public List<Schema> getBaseSchemas();

    public String getDescription();

    public Field<?> getField(String fieldName);

    public Bag<String, Field<?>> getFields();

    public LinkFormula getLinkFormula(String relationName);

    public Bag<String, LinkFormula> getLinkFormulas();

    public String getName();

    public int getVersion();

}
