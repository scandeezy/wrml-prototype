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

package org.wrml.util.transformer;

import org.wrml.runtime.Context;
import org.wrml.util.MediaType;

public class MediaTypeToStringTransformer extends AbstractTransformer<MediaType, String> {

    public MediaTypeToStringTransformer(Context context) {
        super(context);
    }

    public String aToB(MediaType bValue) {

        if (bValue == null) {
            System.out.println("MediaTypeToStringTransformer.aToB(" + bValue + ") returning: null");
            return null;
        }

        String aValue = bValue.toString();

        System.out.println("MediaTypeToStringTransformer.aToB(" + bValue + ") returning: " + aValue);
        return aValue;

    }

    public MediaType bToA(String aValue) {

        //System.out.println("MediaTypeToStringTransformer.bToA(" + aValue + ")");

        if (aValue == null) {
            //System.out.println("MediaTypeToStringTransformer.bToA(" + aValue + ") returning: null");
            return null;
        }

        return MediaType.create(aValue);
    }

}
