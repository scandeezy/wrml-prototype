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

package org.wrml.util.formatter;

import java.net.URI;

import org.wrml.Model;
import org.wrml.runtime.Context;
import org.wrml.util.http.Message;

public interface Formatter {

    public <M extends Model> M read(Context context, Message requestMessage, URI schemaId, Message responseMessage) throws Exception;

    public void write(Context context, Message requestMessage, URI schemaId, Model requestModel) throws Exception;
}
