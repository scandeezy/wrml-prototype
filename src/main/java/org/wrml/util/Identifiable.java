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

public class Identifiable<T extends Comparable<T>> implements Unique<T>, Comparable<Identifiable<T>>, Serializable {

    private static final long serialVersionUID = 6794910140369730199L;

    public final static Comparator<Identifiable<?>> ID_COMPARATOR = new Comparator<Identifiable<?>>() {

        public int compare(final Identifiable<?> o1, final Identifiable<?> o2) {

            if (o1 == o2) {
                return 0;
            }

            return Compare.twoComparables(o1.getId(), o2.getId());
        }

    };

    private T _Id;

    public Identifiable() {

    }

    public Identifiable(T id) {
        setId(id);
    }

    public int compareTo(Identifiable<T> other) {

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
        if (_Id == null) {
            if (other._Id != null) {
                return false;
            }
        } else if (!_Id.equals(other._Id)) {
            return false;
        }
        return true;
    }

    public final T getId() {
        return _Id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((_Id == null) ? 0 : _Id.hashCode());
        return result;
    }

    public final void setId(T id) {
        _Id = id;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [id=" + _Id + "]";
    }

}
