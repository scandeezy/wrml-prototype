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

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.Map;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.wrml.util.observable.DelegatingObservableMap;
import org.wrml.util.observable.MapEvent;
import org.wrml.util.observable.MapEventListener;
import org.wrml.util.observable.ObservableMap;

public class DelegatingObservableMapTest {

    @Test
    public void shouldCancelClear() {
        final Map backingMap = mock(Map.class);
        final ObservableMap<String, Integer> observableMap = new DelegatingObservableMap<String, Integer>(backingMap);
        final MapEventListener<String, Integer> listener = mock(MapEventListener.class);

        // Cancel the event during a call to listener.updatingEntry(...)
        doAnswer(new Answer() {

            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                final MapEvent<String, Integer> event = (MapEvent<String, Integer>) invocationOnMock.getArguments()[0];
                event.setCancelled(true);
                return null;
            }
        }).when(listener).clearing(Matchers.<MapEvent<String, Integer>> any());

        observableMap.addMapEventListener(listener);

        observableMap.clear();

        verify(listener).clearing(Matchers.<MapEvent<String, Integer>> any());
        verifyNoMoreInteractions(listener);

        verify(backingMap, times(0)).clear();
    }

    @Test
    public void shouldCancelPut() {
        final Map backingMap = mock(Map.class);
        final ObservableMap<String, Integer> observableMap = new DelegatingObservableMap<String, Integer>(backingMap);
        final MapEventListener<String, Integer> listener = mock(MapEventListener.class);

        // Cancel the event during a call to listener.updatingEntry(...)
        doAnswer(new Answer() {

            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                final MapEvent<String, Integer> event = (MapEvent<String, Integer>) invocationOnMock.getArguments()[0];
                event.setCancelled(true);
                return null;
            }
        }).when(listener).updatingEntry(Matchers.<MapEvent<String, Integer>> any());

        observableMap.addMapEventListener(listener);

        observableMap.put("test", 123);

        verify(listener).updatingEntry(Matchers.<MapEvent<String, Integer>> any());
        verifyNoMoreInteractions(listener);

        verify(backingMap, times(0)).put(any(), any());
    }

    @Test
    public void shouldCancelRemove() {
        final Map backingMap = mock(Map.class);
        final ObservableMap<String, Integer> observableMap = new DelegatingObservableMap<String, Integer>(backingMap);
        final MapEventListener<String, Integer> listener = mock(MapEventListener.class);

        // Cancel the event during a call to listener.updatingEntry(...)
        doAnswer(new Answer() {

            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                final MapEvent<String, Integer> event = (MapEvent<String, Integer>) invocationOnMock.getArguments()[0];
                event.setCancelled(true);
                return null;
            }
        }).when(listener).removingEntry(Matchers.<MapEvent<String, Integer>> any());

        observableMap.addMapEventListener(listener);

        observableMap.remove("test");

        verify(listener).removingEntry(Matchers.<MapEvent<String, Integer>> any());
        verifyNoMoreInteractions(listener);

        verify(backingMap, times(0)).remove(any());
    }

    @Test
    public void shouldFireClearedEvent() {
        final Map backingMap = mock(Map.class);
        final ObservableMap<String, Integer> observableMap = new DelegatingObservableMap<String, Integer>(backingMap);
        final MapEventListener<String, Integer> listener = mock(MapEventListener.class);

        observableMap.addMapEventListener(listener);

        observableMap.clear();

        verify(listener).clearing(Matchers.<MapEvent<String, Integer>> any());
        verify(listener).cleared(Matchers.<MapEvent<String, Integer>> any());
        verifyNoMoreInteractions(listener);

        verify(backingMap).clear();
    }

    @Test
    public void shouldFireRemovedEvent() {
        final Map backingMap = mock(Map.class);
        final ObservableMap<String, Integer> observableMap = new DelegatingObservableMap<String, Integer>(backingMap);
        final MapEventListener<String, Integer> listener = mock(MapEventListener.class);

        observableMap.addMapEventListener(listener);

        observableMap.remove("test");

        verify(listener).removingEntry(Matchers.<MapEvent<String, Integer>> any());
        verify(listener).entryRemoved(Matchers.<MapEvent<String, Integer>> any());
        verifyNoMoreInteractions(listener);

        verify(backingMap).remove(any());
    }

    @Test
    public void shouldFireUpdatedEventOnPut() {
        final Map backingMap = mock(Map.class);
        final ObservableMap<String, Integer> observableMap = new DelegatingObservableMap<String, Integer>(backingMap);
        final MapEventListener<String, Integer> listener = mock(MapEventListener.class);

        observableMap.addMapEventListener(listener);

        observableMap.put("test", 123);

        verify(listener).updatingEntry(Matchers.<MapEvent<String, Integer>> any());
        verify(listener).entryUpdated(Matchers.<MapEvent<String, Integer>> any());
        verifyNoMoreInteractions(listener);

        verify(backingMap).put(any(), any());
    }

}
