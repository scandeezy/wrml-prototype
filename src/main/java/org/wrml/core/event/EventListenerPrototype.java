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

package org.wrml.core.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.wrml.core.util.Cancelable;

public class EventListenerPrototype<L extends java.util.EventListener> {

    public final static String EVENT_METHOD_NAME_PREFIX = "on";

    private final static int EVENT_NAME_FIRST_LETTER_INDEX = EVENT_METHOD_NAME_PREFIX.length();
    private final static int EVENT_NAME_SECOND_LETTER_INDEX = EVENT_NAME_FIRST_LETTER_INDEX + 1;

    private final static Map<Class<?>, EventListenerPrototype<?>> _Cache;

    static {
        _Cache = new WeakHashMap<Class<?>, EventListenerPrototype<?>>();
    }

    @SuppressWarnings("unchecked")
    public final static <L extends java.util.EventListener> EventListenerPrototype<L> forEventListenerClass(
            Class<L> eventListenerClass) {

        EventListenerPrototype<L> eventListenerPrototype = null;
        if (_Cache.containsKey(eventListenerClass)) {
            eventListenerPrototype = (EventListenerPrototype<L>) _Cache.get(eventListenerClass);
        }
        else {
            eventListenerPrototype = new EventListenerPrototype<L>(eventListenerClass);
            _Cache.put(eventListenerClass, eventListenerPrototype);
        }

        return eventListenerPrototype;
    }

    private final Class<L> _EventListenerClass;
    private Map<String, Method> _EventMethods;

    EventListenerPrototype(Class<L> eventListenerClass) {
        _EventListenerClass = eventListenerClass;
    }

    public void fireEvent(String eventName, EventObject event, List<L> listeners) {

        final Method onEventMethod = getEventMethod(eventName);

        if (onEventMethod == null) {
            throw new NullPointerException(getEventListenerClass() + " does not have an event named \"" + eventName
                    + "\"");
        }

        Cancelable cancelable = null;
        if (event instanceof Cancelable) {
            cancelable = (Cancelable) event;
        }

        final Object[] args = new Object[] { event };

        for (final L listener : listeners) {

            if ((cancelable != null) && cancelable.isCancelled()) {
                break;
            }

            try {

                onEventMethod.invoke(listener, args);

            }
            catch (final IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (final InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    public Class<L> getEventListenerClass() {
        return _EventListenerClass;
    }

    public Method getEventMethod(String eventName) {
        return getEventMethods().get(eventName);
    }

    public Map<String, Method> getEventMethods() {

        if (_EventMethods == null) {

            _EventMethods = new HashMap<String, Method>();

            final Method[] methods = _EventListenerClass.getMethods();
            for (final Method method : methods) {
                final String eventName = getEventName(method);
                if (eventName != null) {
                    _EventMethods.put(eventName, method);
                }
            }
        }

        return _EventMethods;
    }

    private String getEventName(Method method) {
        final String methodName = method.getName();

        if (!methodName.startsWith(EVENT_METHOD_NAME_PREFIX)) {
            //System.err.println("\"" + methodName + "\" does not start with: " + EVENT_METHOD_NAME_PREFIX);
            return null;
        }

        final String eventName = Character.toLowerCase(methodName.charAt(EVENT_NAME_FIRST_LETTER_INDEX))
                + methodName.substring(EVENT_NAME_SECOND_LETTER_INDEX);

        //System.err.println("\"" + eventName + "\" is the event name for method: " + method);

        return eventName;
    }

}
