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

import java.util.List;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.wrml.util.observable.DelegatingObservableList;
import org.wrml.util.observable.ListEvent;
import org.wrml.util.observable.ListEventListener;
import org.wrml.util.observable.ObservableList;

public class DelegatingObservableListTest {

    @Test
    public void shouldCancelAdd() {
        final List backingList = mock(List.class);
        final ObservableList<String> observableList = new DelegatingObservableList<String>(backingList);
        final ListEventListener<String> listener = mock(ListEventListener.class);

        // Cancel the event during a call to listener.updatingEntry(...)
        doAnswer(new Answer() {

            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                final ListEvent<String> event = (ListEvent<String>) invocationOnMock.getArguments()[0];
                event.setCancelled(true);
                return null;
            }
        }).when(listener).insertingElement(Matchers.<ListEvent<String>> any());

        observableList.addListEventListener(listener);

        observableList.add("test");

        verify(listener).insertingElement(Matchers.<ListEvent<String>> any());
        verifyNoMoreInteractions(listener);

        verify(backingList, times(0)).add(any());
    }

    @Test
    public void shouldCancelClear() {
        final List backingList = mock(List.class);
        final ObservableList<String> observableList = new DelegatingObservableList<String>(backingList);
        final ListEventListener<String> listener = mock(ListEventListener.class);

        // Cancel the event during a call to listener.updatingEntry(...)
        doAnswer(new Answer() {

            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                final ListEvent<String> event = (ListEvent<String>) invocationOnMock.getArguments()[0];
                event.setCancelled(true);
                return null;
            }
        }).when(listener).clearing(Matchers.<ListEvent<String>> any());

        observableList.addListEventListener(listener);

        observableList.clear();

        verify(listener).clearing(Matchers.<ListEvent<String>> any());
        verifyNoMoreInteractions(listener);

        verify(backingList, times(0)).clear();
    }

    @Test
    public void shouldCancelRemove() {
        final List backingList = mock(List.class);
        final ObservableList<String> observableList = new DelegatingObservableList<String>(backingList);
        final ListEventListener<String> listener = mock(ListEventListener.class);

        // Cancel the event during a call to listener.updatingEntry(...)
        doAnswer(new Answer() {

            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                final ListEvent<String> event = (ListEvent<String>) invocationOnMock.getArguments()[0];
                event.setCancelled(true);
                return null;
            }
        }).when(listener).removingElement(Matchers.<ListEvent<String>> any());

        observableList.addListEventListener(listener);

        observableList.remove("test");

        verify(listener).removingElement(Matchers.<ListEvent<String>> any());
        verifyNoMoreInteractions(listener);

        verify(backingList, times(0)).remove(any());
    }

    @Test
    public void shouldFireClearedEvent() {
        final List backingList = mock(List.class);
        final ObservableList<String> observableList = new DelegatingObservableList<String>(backingList);
        final ListEventListener<String> listener = mock(ListEventListener.class);

        observableList.addListEventListener(listener);

        observableList.clear();

        verify(listener).clearing(Matchers.<ListEvent<String>> any());
        verify(listener).cleared(Matchers.<ListEvent<String>> any());
        verifyNoMoreInteractions(listener);

        verify(backingList).clear();
    }

    @Test
    public void shouldFireInsertEventOnAdd() {
        final List backingList = mock(List.class);
        final ObservableList<String> observableList = new DelegatingObservableList<String>(backingList);
        final ListEventListener<String> listener = mock(ListEventListener.class);

        observableList.addListEventListener(listener);

        observableList.add("test");

        verify(listener).insertingElement(Matchers.<ListEvent<String>> any());
        verify(listener).elementInserted(Matchers.<ListEvent<String>> any());
        verifyNoMoreInteractions(listener);

        verify(backingList).add(any());
    }

    @Test
    public void shouldFireRemovedEvent() {
        final List backingList = mock(List.class);
        final ObservableList<String> observableList = new DelegatingObservableList<String>(backingList);
        final ListEventListener<String> listener = mock(ListEventListener.class);

        observableList.addListEventListener(listener);

        observableList.remove("test");

        verify(listener).removingElement(Matchers.<ListEvent<String>> any());
        verify(listener).elementRemoved(Matchers.<ListEvent<String>> any());
        verifyNoMoreInteractions(listener);

        verify(backingList).remove(any());
    }

}
