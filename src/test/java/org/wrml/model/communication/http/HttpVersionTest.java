package org.wrml.model.communication.http;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.wrml.model.communication.http.HttpVersion.*;

public class HttpVersionTest {

    @Test
    public void shouldParse10() {
        assertThat("parsed HTTP version", fromString("HTTP/1.0"), is(HTTP_1_0));
    }

    @Test
    public void shouldParse11() {
        assertThat("parsed HTTP version", fromString("HTTP/1.1"), is(HTTP_1_1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException() {
        fromString("1234");
        fail("Invalid HTTP version should throw exception.");
    }

}
