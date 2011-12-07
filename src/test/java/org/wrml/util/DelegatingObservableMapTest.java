package org.wrml.util;

import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Map;

import static org.mockito.Mockito.*;

public class DelegatingObservableMapTest {

    @Test
    public void shouldFireClearedEvent() {
        ObservableMap<String, Integer> observableMap = new DelegatingObservableMap<String, Integer>(mock(Map.class));
        MapEventListener<String, Integer> listener = mock(MapEventListener.class);

        observableMap.addEventListener(listener);

        observableMap.clear();

        verify(listener).clearing(Matchers.<MapEvent<String, Integer>>any());
        verify(listener).cleared(Matchers.<MapEvent<String, Integer>>any());
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldCancelPut() {
        final Map backingMap = mock(Map.class);
        ObservableMap<String, Integer> observableMap = new DelegatingObservableMap<String, Integer>(backingMap);
        MapEventListener<String, Integer> listener = mock(MapEventListener.class);

        // Cancel the event during a call to listener.updatingEntry(...)
        doAnswer(new Answer() {
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                MapEvent<String, Integer> event = (MapEvent<String, Integer>) invocationOnMock.getArguments()[0];
                event.setCancelled(true);
                return null;
            }
        }).when(listener).updatingEntry(Matchers.<MapEvent<String, Integer>>any());

        observableMap.addEventListener(listener);

        observableMap.put("test", 123);

        verify(listener).updatingEntry(Matchers.<MapEvent<String, Integer>>any());
        verifyNoMoreInteractions(listener);

        verify(backingMap, times(0)).put(any(), any());
    }

}
