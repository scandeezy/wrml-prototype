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

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;

import org.wrml.util.DelegatingInvocationHandler;

class StaticModelProxy {

    public final static Model newProxyInstance(Model dynamicModel) {
        
        URI schemaId = dynamicModel.getSchemaId();
        Context context = dynamicModel.getContext();
                
        String className = context.getClassName(schemaId);
        System.out.println("Loading schema class: " + className);
        ClassLoader schemaInterfaceLoader = context.getSchemaService().getSchemaInterfaceLoader();
        Class<?> schemaInterface = null;
        try {
            
            schemaInterface = schemaInterfaceLoader.loadClass(className);
        }
        catch (Throwable t) {
            // TODO Auto-generated catch block
            t.printStackTrace();
        }
        
        Class<?>[] schemaInterfaceArray = new Class<?>[] { schemaInterface };
        StaticModelHandler invocationHandler = new StaticModelHandler(dynamicModel);
        return (Model) Proxy.newProxyInstance(schemaInterfaceLoader, schemaInterfaceArray, invocationHandler);
    }
    
    
    
   /* 
    * TODO: This approach adds all base schema interfaces. Only need to add the model's actual schema interface, since the interface inheritance should cover the rest. 
    */
    
    /*
    public final static Model newProxyInstance(Model dynamicModel) {

        URI schemaId = dynamicModel.getSchemaId();
        Context context = dynamicModel.getContext();
        Prototype prototype = dynamicModel.getPrototype();

        List<Class<?>> schemaInterfaces = new ArrayList<Class<?>>();
        
        Map<URI, Schema> allBaseSchemas = prototype.getAllBaseSchemas();
        if (allBaseSchemas != null) {
            for (URI baseSchemaId : allBaseSchemas.keySet()) {
                addInterface(schemaInterfaces, dynamicModel, baseSchemaId);
            }
        }

        addInterface(schemaInterfaces, dynamicModel, schemaId);
        schemaInterfaces.add(Model.class);

        Class<?>[] schemaInterfaceArray = new Class<?>[schemaInterfaces.size()];
        schemaInterfaceArray = schemaInterfaces.toArray(schemaInterfaceArray);

        StaticModelHandler invocationHandler = new StaticModelHandler(dynamicModel);

        ClassLoader schemaInterfaceLoader = context.getSchemaService().getSchemaInterfaceLoader();
        return (Model) Proxy.newProxyInstance(schemaInterfaceLoader, schemaInterfaceArray, invocationHandler);
    }

    private final static void addInterface(List<Class<?>> schemaInterfaces, Model dynamicModel, URI schemaId) {
        Context context = dynamicModel.getContext();
        String className = context.getClassName(schemaId);
        System.out.println("Loading schema class: " + className);
        Class<?> schemaInterface = null;
        try {
            ClassLoader schemaInterfaceLoader = context.getSchemaService().getSchemaInterfaceLoader();
            schemaInterface = schemaInterfaceLoader.loadClass(className);
        }
        catch (Throwable t) {
            // TODO Auto-generated catch block
            t.printStackTrace();
        }

        if (schemaInterface != null) {
            schemaInterfaces.add(schemaInterface);
        }
    }
*/
    
    private static class StaticModelHandler extends DelegatingInvocationHandler {

        public StaticModelHandler(Model dyanmicModel) {
            super(dyanmicModel);
        }

        public Model getDyanmicModel() {
            return (Model) getDelegate();
        }

        @Override
        protected Object subInvoke(Object proxy, Method method, Object[] args) throws Throwable {

            if (method.getDeclaringClass().equals(Model.class)) {
                return super.subInvoke(proxy, method, args);
            }

            final String methodName = method.getName();

            // TODO: This can all be optimized

            String fieldName = null;
            Object fieldValue = null;

            if (methodName.startsWith("get")) {
                fieldName = methodName.substring(3);
            }
            else
                if (methodName.startsWith("is")) {
                    fieldName = methodName.substring(2);
                }

            if (fieldName != null) {
                fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
                                            
                
                // TODO: Refactor this to Handle booleans (see how ReadOnly is done in RuntimeModel)
                
                fieldValue = getDyanmicModel().getFieldValue(fieldName);
                if (fieldValue != null) {
                    return fieldValue;
                }

                // TODO: Handle Links that look like "get" methods
            }
            else
                if (methodName.startsWith("set")) {
                    fieldName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                    fieldValue = getDyanmicModel().setFieldValue(fieldName, args[0]);
                    if (fieldValue != null) {
                        return fieldValue;
                    }

                    // TODO: Handle Links that look like "set" methods
                }

            return null;
        }

        
        
        
    }

}
