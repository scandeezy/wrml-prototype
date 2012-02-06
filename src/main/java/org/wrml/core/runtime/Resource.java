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

package org.wrml.core.runtime;

import java.net.URI;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.wrml.core.model.api.LinkTemplate;

final class Resource {

    private final HypermediaEngine _HypermediaEngine;
    private final URI _ResourceTemplateId;

    private SortedMap<URI, LinkTemplate> _Links;
    private SortedMap<URI, LinkTemplate> _References;

    public Resource(HypermediaEngine hypermediaEngine, URI resourceTemplateId) {
        _HypermediaEngine = hypermediaEngine;
        _ResourceTemplateId = resourceTemplateId;
    }

    public HypermediaEngine getHypermediaEngine() {
        return _HypermediaEngine;
    }

    public SortedMap<URI, LinkTemplate> getLinks() {

        if (_Links == null) {
            _Links = new TreeMap<URI, LinkTemplate>();

            final HypermediaEngine hypermediaEngine = getHypermediaEngine();
            final List<LinkTemplate> linkTemplates = hypermediaEngine.getLinkTemplates();
            for (final LinkTemplate linkTemplate : linkTemplates) {
                if (linkTemplate.getReferrerId().equals(_ResourceTemplateId)) {
                    _Links.put(linkTemplate.getLinkRelationId(), linkTemplate);
                }
            }

        }

        return _Links;
    }

    public SortedMap<URI, LinkTemplate> getReferences() {

        if (_References == null) {
            _References = new TreeMap<URI, LinkTemplate>();

            final HypermediaEngine hypermediaEngine = getHypermediaEngine();
            final List<LinkTemplate> linkTemplates = hypermediaEngine.getLinkTemplates();
            for (final LinkTemplate linkTemplate : linkTemplates) {
                if (linkTemplate.getEndPointId().equals(_ResourceTemplateId)) {
                    _References.put(linkTemplate.getLinkRelationId(), linkTemplate);
                }
            }
        }

        return _References;
    }

    public URI getResourceTemplateId() {
        return _ResourceTemplateId;
    }
}
