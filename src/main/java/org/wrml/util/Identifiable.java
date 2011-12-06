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

package org.wrml.util;

import java.io.Serializable;
import java.util.Comparator;

public abstract class Identifiable<I extends Comparable<I>> implements Unique<I>, Comparable<Identifiable<I>>,
        Serializable {

    private static final long serialVersionUID = 6794910140369730199L;

    public final static Comparator<Identifiable<?>> ID_COMPARATOR = new Comparator<Identifiable<?>>() {

        public int compare(final Identifiable<?> o1, final Identifiable<?> o2) {

            if (o1 == o2) {
                return 0;
            }

            return Compare.twoComparables(o1.getId(), o2.getId());
        }

    };

    public Identifiable() {

    }

    public Identifiable(I id) {
        setId(id);
    }

    public int compareTo(Identifiable<I> other) {

        if (other == null) {
            return 1;
        }

        return ID_COMPARATOR.compare(this, other);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Identifiable<?> other = (Identifiable<?>) obj;

        final I id = getId();
        if (id == null) {
            if (other.getId() != null) {
                return false;
            }
        }
        else
            if (!id.equals(other.getId())) {
                return false;
            }
        return true;
    }

    public abstract I getId();

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final I id = getId();
        result = (prime * result) + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public abstract I setId(I id);

    @Override
    public String toString() {
        return getClass().getName() + " [id=" + getId() + "]";
    }

}
