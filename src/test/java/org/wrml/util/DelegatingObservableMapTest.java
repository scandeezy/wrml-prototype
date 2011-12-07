package org.wrml.util;

import org.junit.Test;
import org.mockito.Matchers;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
}
