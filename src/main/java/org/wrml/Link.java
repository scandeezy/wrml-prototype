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

import org.wrml.api.LinkTemplate;
import org.wrml.api.ResourceTemplate;
import org.wrml.util.Identifiable;

/**
 * A WRMLObject instance's Link. This class represents a link "instance", that
 * is a link with a fully qualified href URI value that can be used to interact.
 * 
 * A link is enabled if the WrmlObject's schema's associated link formula
 * evaluates to true. Instances of this class are responsible for managing their
 * enabled state changes by listening to events from the fields that their
 * LinkFormula relies upon.
 */
public final class Link extends Identifiable<URI> {

    private static final long serialVersionUID = -6235652220661484935L;

    private final WrmlObject _Owner;
    private final URI _LinkRelationId;
    private URI _Href;
    private boolean _Enabled;

    public Link(WrmlObject owner, URI linkRelationId) {
        _Owner = owner;
        _LinkRelationId = linkRelationId;
        setId(linkRelationId);
    }

    public void addEventListener(LinkEventListener listener) {
        // TODO
    }

    public void fireHrefChangedEvent() {

    }

    public URI getHref() {
        return _Href;
    }

    public URI getLinkRelationId() {
        return _LinkRelationId;
    }

    public LinkTemplate getLinkTemplate() {
        final WrmlObject owner = getOwner();
        final ResourceTemplate resourceTemplate = getOwner().getResourceTemplate();
        final URI linkTemplateId = resourceTemplate.getHereToThereLinkTemplateId(getLinkRelationId());
        return owner.getContext().getLinkTemplate(linkTemplateId);
    }

    public WrmlObject getOwner() {
        return _Owner;

    }

    public boolean isEnabled() {
        return _Enabled;
    }

    public void removeEventListener(LinkEventListener listener) {
        // TODO
    }

    /*
     * 
     * public void hrefFieldValueChanged(FieldEvent event) { updateHref(); }
     * public void setHref (String href) { if (_Href !.equal href) { final _
     * Href = href; fireHrefChangedEvent(); } }
     * 
     * public void updateHref() { final LinkTemplate linkTemplate =
     * getLinkTemplate(); final ResourceTemplate destination =
     * linkTemplate.getDestination(); final UriTemplate uriTemplate =
     * destination.getUriTemplate(); setHref(uriTemplate.execute(this)); }
     */

}
