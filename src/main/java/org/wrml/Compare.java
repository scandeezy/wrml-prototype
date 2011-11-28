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

import java.util.Date;

/**
 * Simple utility class that provides a clean, consistent syntax for
 * comparisons.
 */
public abstract class Compare {

    public static int twoComparables(Comparable easy, Comparable peasy) {
        return easy.compareTo(peasy);
    }

    public static int twoDates(Date me, Date you) {
        return me.compareTo(you);
    }

    public static int twoDoubles(double me, double you) {
        return Double.compare(me, you);
    }

    public static int twoEnums(Enum me, Enum you) {
        return me.compareTo(you);
    }

    public static int twoFloats(float me, float you) {
        return Float.compare(me, you);
    }

    public static int twoInsensitiveStrings(String jerk, String meanie) {
        return String.CASE_INSENSITIVE_ORDER.compare(jerk, meanie);
    }

    public static int twoInts(int me, int you) {
        return Integer.signum(me - you);
    }

    public static int twoLongs(long me, long you) {
        return Long.signum(me - you);
    }

    public static int twoStrings(String me, String you) {
        return me.compareTo(you);
    }

}
