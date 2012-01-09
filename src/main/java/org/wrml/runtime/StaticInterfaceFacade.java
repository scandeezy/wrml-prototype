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

package org.wrml.runtime;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

import org.wrml.Model;
import org.wrml.util.DelegatingInvocationHandler;

public class StaticInterfaceFacade extends DelegatingInvocationHandler {

    public StaticInterfaceFacade(Model delegate) {
        super(delegate);
    }

    @Override
    protected Object subInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        Class<?> declaringClass = method.getDeclaringClass();

        // If the method was declared in Model or one of its ancestors, delegate the call.
        if (declaringClass.isAssignableFrom(Model.class)) {
            return super.subInvoke(proxy, method, args);
        }

        Model model = getDelegateModel().getStaticInterface();
        final Context context = model.getContext();
        final URI schemaId = model.getSchemaId();
        final Prototype prototype = context.getPrototype(schemaId);

        String methodKey = getMethodKey(method);
        String methodName = method.getName();

        FieldPrototype fieldPrototype = prototype.getFieldPrototype(methodKey, methodName);
        if (fieldPrototype != null) {
            Object fieldValue = null;
            if (fieldPrototype.getAccessType() == FieldAccessType.SET && args != null && args.length > 0) {
                fieldValue = args[0];
            }
            return fieldPrototype.accessField(model, fieldValue);
        }

        LinkPrototype linkPrototype = prototype.getLinkPrototype(methodKey, methodName, method.getReturnType());
        if (linkPrototype != null) {

            Object requestEntity = (args != null && args.length > 0) ? args[0] : null;

            // TODO: Deal with additional params
            @SuppressWarnings("unchecked")
            Map<String, String> hrefArgs = (Map<String, String>) ((args != null && args.length > 1) ? args[1] : null);

            return linkPrototype.clickLink(model, requestEntity, hrefArgs);
        }

        return null;
    }

    private Model getDelegateModel() {
        return (Model) getDelegate();
    }

}
