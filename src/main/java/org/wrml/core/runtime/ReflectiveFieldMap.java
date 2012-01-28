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

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.wrml.core.util.Delegating;
import org.wrml.core.util.DelegatingInvocationHandler;

public class ReflectiveFieldMap<T> extends FieldMap implements Delegating<Object>, InvocationHandler {

    private final Object _Delegate;
    private final Class<T> _StaticInterfaceClass;
    private final DelegatingInvocationHandler _DelegatingInvocationHandler;

    private T _StaticInterface;
    private SortedSet<String> _FieldNames;

    private SortedMap<String, Method> _FieldNameToGetMethod;
    private SortedMap<String, Method> _FieldNameToSetMethod;

    public ReflectiveFieldMap(final Context context, final Object delegate, final Class<T> staticInterfaceClass) {
        super(context);
        _Delegate = delegate;
        _StaticInterfaceClass = staticInterfaceClass;
        _DelegatingInvocationHandler = new DelegatingInvocationHandler(delegate);
    }

    public final Object getDelegate() {
        return _Delegate;
    }

    public final DelegatingInvocationHandler getDelegatingInvocationHandler() {
        return _DelegatingInvocationHandler;
    }

    @SuppressWarnings("unchecked")
    public final T getStaticInterface() {

        if (Proxy.isProxyClass(this.getClass())) {
            return (T) this;
        }

        if (_StaticInterface == null) {

            final Object delegate = getDelegate();
            final Class<?> delegateClass = delegate.getClass();
            final ClassLoader classLoader = delegateClass.getClassLoader();

            final Class<T>[] staticTypeArrayOfOneElement = (Class<T>[]) Array.newInstance(Class.class, 1);
            staticTypeArrayOfOneElement[0] = getStaticType();

            _StaticInterface = (T) Proxy.newProxyInstance(classLoader, staticTypeArrayOfOneElement,
                    _DelegatingInvocationHandler);

        }

        return _StaticInterface;
    }

    public final Class<T> getStaticType() {
        return _StaticInterfaceClass;
    }

    public final Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        return _DelegatingInvocationHandler.invoke(proxy, method, args);
    }

    @Override
    public String toString() {
        return getClass().getName() + " : { delegate : " + _Delegate + "}, staticType : {" + _StaticInterfaceClass
                + "}, staticInterface : {" + _StaticInterface + "}, fieldNames : {" + _FieldNames + "} }";
    }

    protected Object accessField(T staticInterface, Method method, FieldAccessType fieldAccessType, Object newValue) {

        Object[] args = null;
        if (fieldAccessType == FieldAccessType.SET) {
            args = new Object[] { newValue };
        }

        try {
            return _DelegatingInvocationHandler.invoke(staticInterface, method, args);
        }
        catch (final Throwable e) {

            final String debugMethodPrint = method.getDeclaringClass().getCanonicalName() + " - "
                    + DelegatingInvocationHandler.baseGetMethodKey(method);

            final String fieldTypeString = (newValue != null) ? newValue.getClass().getCanonicalName() : "?";

            final String message = "A problem occured when trying to access a field using the method \""
                    + debugMethodPrint + "\" with value: \"" + newValue + "\" (of type: \"" + fieldTypeString
                    + "\") invoked on the static interface: \"" + staticInterface + "\".";

            throw new RuntimeException(message, e);
        }
    }

    protected final String getFieldName(Method method) {

        if (Object.class.equals(method.getDeclaringClass())) {
            return null;
        }

        final String methodName = method.getName();
        String fieldName = null;
        if (methodName.startsWith("get") || methodName.startsWith("set")) {
            fieldName = methodName.substring(3);
        }
        else if (methodName.startsWith("is")) {
            fieldName = methodName.substring(2);
        }

        if (fieldName != null) {
            fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
        }

        return fieldName;
    }

    @Override
    protected final SortedSet<String> getFieldNames() {

        if (_FieldNames == null) {
            final Map<String, Method> delegateMethods = getDelegatingInvocationHandler().getDelegateMethods();
            _FieldNames = getFieldNames(delegateMethods.values());
        }

        return _FieldNames;
    }

    protected final SortedSet<String> getFieldNames(Collection<Method> methods) {
        final SortedSet<String> fieldNames = new TreeSet<String>();
        if (methods != null) {
            for (final Method method : methods) {

                if (Object.class.equals(method.getDeclaringClass())) {
                    continue;
                }

                final String fieldName = getFieldName(method);
                if (fieldName != null) {
                    fieldNames.add(fieldName);

                }
                else {
                    // TODO: Handle this 
                    //System.out.println("The field name was null for method, \"" + method + "\".");
                }

            }
        }
        return fieldNames;
    }

    @Override
    protected final java.lang.reflect.Type getFieldNativeType(String fieldName) {

        final Method method = getMethod(FieldAccessType.GET, fieldName);

        if (method == null) {
            throw new NullPointerException("There is no \"getter\" for field: \"" + fieldName + "\" in delegate: "
                    + getDelegate() + "\".");
        }

        final Context context = getContext();
        final TypeSystem typeSystem = context.getTypeSystem();
        return typeSystem.getNativeReturnType(method, _StaticInterfaceClass);
    }

    protected final Method getMethod(FieldAccessType fieldAccessType, String fieldName) {

        if (fieldAccessType == FieldAccessType.GET) {
            if (_FieldNameToGetMethod == null) {
                _FieldNameToGetMethod = new TreeMap<String, Method>();
            }

            if (_FieldNameToGetMethod.containsKey(fieldName)) {
                return _FieldNameToGetMethod.get(fieldName);
            }
        }
        else if (fieldAccessType == FieldAccessType.SET) {
            if (_FieldNameToSetMethod == null) {
                _FieldNameToSetMethod = new TreeMap<String, Method>();
            }

            if (_FieldNameToSetMethod.containsKey(fieldName)) {
                return _FieldNameToSetMethod.get(fieldName);
            }
        }

        final String fieldNameAsMixedUpperCase = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        final String methodName = fieldAccessType.toString() + fieldNameAsMixedUpperCase;
        final String booleanIsMethodName = "is" + fieldNameAsMixedUpperCase;
        /*
         * First, leverage the mapping work already done by the
         * DelegatingInvocationHandler
         */

        final Map<String, Method> delegateMethods = getDelegatingInvocationHandler().getDelegateMethods();

        Method method = null;

        if (fieldAccessType == FieldAccessType.GET) {
            method = delegateMethods.get(methodName + "()");
            if (method == null) {
                method = delegateMethods.get(booleanIsMethodName + "()");
            }
        }
        else if (fieldAccessType == FieldAccessType.SET) {
            final Set<String> methodKeys = delegateMethods.keySet();
            for (final String fullMethodKey : methodKeys) {
                if (fullMethodKey.startsWith(methodName)) {
                    method = delegateMethods.get(fullMethodKey);
                    break;
                }
            }
        }

        final T staticInterface = getStaticInterface();

        if (method == null) {

            /*
             * Reflect on the static interface to find the method there
             */

            final Method[] staticInterfaceMethods = staticInterface.getClass().getMethods();
            for (final Method staticInterfaceMethod : staticInterfaceMethods) {

                final String staticInterfaceMethodName = staticInterfaceMethod.getName();

                if (staticInterfaceMethodName.equals(methodName)
                        || staticInterfaceMethodName.equals(booleanIsMethodName)) {

                    method = staticInterfaceMethod;
                    break;
                }
            }
        }

        if (fieldAccessType == FieldAccessType.GET) {
            _FieldNameToGetMethod.put(fieldName, method);

        }
        else if (fieldAccessType == FieldAccessType.SET) {
            _FieldNameToSetMethod.put(fieldName, method);
        }

        return method;
    }

    @Override
    protected Object getRawFieldValue(String fieldName) {

        final T staticInterface = getStaticInterface();
        if (staticInterface == null) {
            throw new NullPointerException("Could not get the static interface for delegate: \"" + getDelegate()
                    + "\".");
        }

        final Method method = getMethod(FieldAccessType.GET, fieldName);

        if (method == null) {
            /*
             * throw new
             * NullPointerException("There is no \"getter\" for field: " +
             * fieldName + " in static interface: "
             * + staticInterface);
             * 
             * An Exception initially seemed logical here but then I realized
             * that it killed the dynamic interface's ability to accept random
             * field name inquiry's and respond with Java's version of
             * "Go Fish!"
             */
            return null;
        }

        return accessField(staticInterface, method, FieldAccessType.GET, null);
    }

    @Override
    protected boolean isReadOnly(String fieldName) {
        return getMethod(FieldAccessType.SET, fieldName) == null;
    }

    @Override
    protected Object setRawFieldValue(String fieldName, Object newValue) {

        final T staticInterface = getStaticInterface();
        if (staticInterface == null) {
            throw new NullPointerException("Could not get the static interface for delegate: \"" + getDelegate()
                    + "\".");
        }

        final Method method = getMethod(FieldAccessType.SET, fieldName);

        if (method == null) {
            throw new NullPointerException("There is no \"setter\" for field: \"" + fieldName
                    + "\" in static interface: " + staticInterface);
        }

        return accessField(staticInterface, method, FieldAccessType.SET, newValue);
    }

}
