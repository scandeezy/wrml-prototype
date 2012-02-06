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

import java.lang.reflect.Method;
import java.util.Map;

import org.wrml.core.Model;
import org.wrml.core.runtime.system.FieldPrototype;
import org.wrml.core.runtime.system.LinkPrototype;
import org.wrml.core.runtime.system.Prototype;
import org.wrml.core.util.DelegatingInvocationHandler;

public class StaticInterfaceFacade extends DelegatingInvocationHandler {

    public StaticInterfaceFacade(Model delegate) {
        super(delegate);
    }

    @Override
    protected Object subInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        final Class<?> declaringClass = method.getDeclaringClass();

        // If the method was declared in Model or one of its ancestors, delegate the call.
        if (declaringClass.isAssignableFrom(Model.class)) {
            return super.subInvoke(proxy, method, args);
        }

        final Model model = getDelegateModel().getStaticInterface();
        final Context context = model.getContext();
        final Prototype prototype = context.getPrototype(model.getNativeType());

        final String fieldName = getFieldName(method);
        final FieldPrototype fieldPrototype = prototype.getFieldPrototype(fieldName);
        if (fieldPrototype != null) {
            FieldAccessType fieldAccessType = FieldAccessType.GET;
            Object fieldValue = null;
            if (method.getName().startsWith("set") && (args != null) && (args.length > 0)) {
                fieldAccessType = FieldAccessType.SET;
                fieldValue = args[0];
            }
            return fieldPrototype.accessField(model, fieldAccessType, fieldValue);
        }

        final String methodKey = getMethodKey(method);
        final LinkPrototype linkPrototype = prototype.getLinkPrototype(methodKey, method);
        if (linkPrototype != null) {

            final Object requestEntity = ((args != null) && (args.length > 0)) ? args[0] : null;

            // TODO: Deal with additional params
            @SuppressWarnings("unchecked")
            final Map<String, String> hrefArgs = (Map<String, String>) (((args != null) && (args.length > 1)) ? args[1]
                    : null);

            return linkPrototype.clickLink(model, requestEntity, hrefArgs);
        }

        return null;
    }

    private Model getDelegateModel() {
        return (Model) getDelegate();
    }

    private String getFieldName(Method method) {

        final String methodName = method.getName();
        String possibleFieldName = null;
        if (methodName.startsWith("get")) {
            possibleFieldName = methodName.substring(3);
        }
        else if (methodName.startsWith("is")) {
            possibleFieldName = methodName.substring(2);
        }
        else if (methodName.startsWith("set")) {
            possibleFieldName = methodName.substring(3);
        }

        if (possibleFieldName != null) {

            possibleFieldName = Character.toLowerCase(possibleFieldName.charAt(0)) + possibleFieldName.substring(1);
        }

        return possibleFieldName;
    }

}
