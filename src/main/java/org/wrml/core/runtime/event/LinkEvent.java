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

package org.wrml.core.runtime.event;

import org.wrml.core.Hyperlink;

/**
 * Used to communicate the change in a Link's state.
 */
public class LinkEvent extends ModelEvent {

    private static final long serialVersionUID = 1L;

    private final Hyperlink _Hyperlink;

    public LinkEvent(Hyperlink link) {
        super(link.getReferrer());
        _Hyperlink = link;
    }

    public Hyperlink getHyperlink() {
        return _Hyperlink;
    }

}
