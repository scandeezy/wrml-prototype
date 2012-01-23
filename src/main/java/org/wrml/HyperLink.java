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

import java.net.URI;
import java.util.Map;

import org.wrml.event.LinkEventListener;
import org.wrml.model.api.LinkTemplate;
import org.wrml.model.schema.Link;
import org.wrml.model.schema.LinkRelation;

public interface HyperLink {

    public void addEventListener(LinkEventListener listener);

    public Object click(java.lang.reflect.Type nativeReturnType, Object requestEntity, Map<String, String> hrefParams);

    public URI getHref();

    public Link getLink();

    public LinkRelation getLinkRelation();

    public URI getLinkRelationId();

    public LinkTemplate getLinkTemplate();

    public Model getReferrer();

    public boolean isEnabled();

    public void removeEventListener(LinkEventListener listener);

}
