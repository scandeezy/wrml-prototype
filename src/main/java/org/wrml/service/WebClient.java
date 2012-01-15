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

package org.wrml.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpTrace;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import org.wrml.Model;
import org.wrml.model.format.Format;
import org.wrml.runtime.Context;
import org.wrml.util.MediaType;
import org.wrml.util.formatter.Formatter;
import org.wrml.util.formatter.JsonFormatter;
import org.wrml.util.http.AcceptHeader;
import org.wrml.util.http.Body;
import org.wrml.util.http.Entity;
import org.wrml.util.http.Headers;
import org.wrml.util.http.HttpVersion;
import org.wrml.util.http.Message;
import org.wrml.util.http.MessageType;
import org.wrml.util.http.Method;
import org.wrml.util.http.RequestLine;
import org.wrml.util.http.StartLine;
import org.wrml.util.http.Status;
import org.wrml.util.http.StatusLine;
import org.wrml.util.transformer.PassthroughTransformer;
import org.wrml.util.transformer.Transformer;

/*
 * Implements the service interface with a REST API client.
 * 
 * TODO: This is all still very "alpha" - optimize at will.
 */
public class WebClient extends ServiceMap {

    private final HttpClient _HttpClient;
    private MediaType _DefaultFormatMediaType;
    private final JsonFormatter _TempJsonFormatter;

    public final static PassthroughTransformer<URI> ID_TRANSFORMER = new PassthroughTransformer<URI>();

    public WebClient(Context context) {
        super(context);

        _HttpClient = new DefaultHttpClient();
        _TempJsonFormatter = new JsonFormatter(context);
    }

    @Override
    public void clear() {

        /*
         * TODO: Design question - Does clear make any sense for a REST API? If
         * not, should we throw an exception?
         */

        super.clear();
    }

    @Override
    public boolean containsKey(Object key) {

        /*
         * TODO: Design proposal - A HEAD request and check for 200 success?
         */

        return super.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {

        /*
         * TODO: Design proposal - A HEAD request and check for 200 success?
         * Using the Model's (Document's) id?
         */

        return super.containsValue(value);
    }

    public Object create(URI collectionId, Object requestEntity, MediaType responseType, Model referrer) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<java.util.Map.Entry<URI, Object>> entrySet() {

        /*
         * TODO: Not really possible without a URI. Should we throw an
         * exception?
         */

        return super.entrySet();
    }

    /**
     * HTTP GET
     */
    public Object get(URI resourceId, Object cachedEntity, MediaType responseType, Model referrer) {

        System.out.println("WebClient: WWW GET request for: " + resourceId + " as: " + responseType);

        final Context context = getContext();

        // Create the request message
        final Message requestMessage = createRequestMessage(resourceId, cachedEntity, responseType, referrer,
                Method.GET);

        // Send the request message to get the response message
        final Message responseMessage = sendRequestMessage(requestMessage);

        final MediaType actualResponseType = responseMessage.getEntity().getHeaders().getContentType();

        final Entity responseEntity = responseMessage.getEntity();
        final Body responseBody = responseEntity.getBody();
        final InputStream responseInputStream = responseBody.getInputStream();

        if (actualResponseType.isWrml()) {

            // Default the format if need be
            final MediaType formatMediaType = (actualResponseType != null) ? actualResponseType
                    : getDefaultFormatMediaType();
            final Formatter formatter = getFormatter(formatMediaType);

            Model model = null;
            try {
                model = formatter.read(context, requestMessage, responseMessage);
            }
            catch (final Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return model;
        }
        else {
            // If responseType is *NOT* a WRML type, then return the response message body's InputStream.     
            return responseInputStream;
        }

    }

    public MediaType getDefaultFormatMediaType() {
        return _DefaultFormatMediaType;
    }

    public Format getFormat(MediaType mediaType) {
        return getContext().getMediaTypeToFormatTransformer().aToB(mediaType);
    }

    public Formatter getFormatter(MediaType mediaType) {

        // TODO: !!!!----- Deal with Formatter

        // Get the Format associated with the MediaType
        //Format format = getFormat(mediaType);

        // TODO: Get all formatter Code-On-Demand

        // TODO: Find the right formatter for Java

        // TODO: Download and return the formatter

        return _TempJsonFormatter;
    }

    public Transformer<URI, URI> getIdTransformer() {
        return ID_TRANSFORMER;
    }

    @Override
    public boolean isEmpty() {

        /*
         * TODO: Not really possible without a URI. Should we throw an
         * exception?
         */

        return super.isEmpty();
    }

    @Override
    public Set<URI> keySet() {

        /*
         * TODO: Not really possible without a URI. Should we throw an
         * exception?
         */

        return super.keySet();
    }

    public Object put(URI resourceId, Object requestEntity, MediaType responseType, Model referrer) {
        /*
         * TODO: HTTP PUT
         */

        return null;
    }

    @Override
    public void putAll(Map<? extends URI, ? extends Object> map) {

        /*
         * TODO: HTTP PUT (batch/transactional?)
         */

        super.putAll(map);
    }

    public Object remove(URI resourceId, MediaType responseType, Model referrer) {
        /*
         * TODO: HTTP DELETE
         */
        return null;
    }

    public void setDefaultFormatMediaType(MediaType defaultFormatMediaType) {
        _DefaultFormatMediaType = defaultFormatMediaType;
    }

    @Override
    public int size() {
        /*
         * TODO: Not really possible without a URI. Should we throw an
         * exception?
         */
        return super.size();
    }

    @Override
    public java.util.Collection<Object> values() {
        /*
         * TODO: Not really possible without a URI. Should we throw an
         * exception?
         */
        return super.values();
    }

    @Override
    protected void finalize() throws Throwable {

        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        _HttpClient.getConnectionManager().shutdown();

        super.finalize();
    }

    private Message createMessage(final MessageType messageType, final StartLine startLine) {

        // Create the message model
        final Message message = new DefaultMessage();

        // HTTP-message   = Request | Response
        message.setType(messageType);

        // start-line     = Request-Line | Status-Line
        message.setStartLine(startLine);

        // Create the entity with headers and a body
        final Entity entity = new DefaultEntity();
        message.setEntity(entity);

        final Headers headers = new DefaultHeaders();
        entity.setHeaders(headers);

        final Body body = new DefaultBody();
        entity.setBody(body);

        return message;
    }

    private Message createRequestMessage(final URI resourceId, final Object cachedEntity, final MediaType responseType,
            final Model referrer, final Method method) {

        final RequestLine requestLine = new DefaultRequestLine();
        // e.g. GET /pub/WWW/TheProject.html HTTP/1.1
        requestLine.setMethod(method);
        requestLine.setUri(resourceId);
        requestLine.setHttpVersion(HttpVersion.HTTP_1_1);

        // Create the request message
        final Message requestMessage = createMessage(MessageType.REQUEST, requestLine);

        return requestMessage;
    }

    private Message sendRequestMessage(Message requestMessage) {

        HttpUriRequest request = null;

        final RequestLine requestLine = (RequestLine) requestMessage.getStartLine();
        final Method method = requestLine.getMethod();
        final URI uri = requestLine.getUri();

        switch (method) {

        case GET:
            request = new HttpGet(uri);
            break;

        case HEAD:
            request = new HttpHead(uri);
            break;

        case POST:
            request = new HttpPost(uri);
            break;

        case PUT:
            request = new HttpPut(uri);
            break;

        case DELETE:
            request = new HttpDelete(uri);
            break;

        case OPTIONS:
            request = new HttpOptions(uri);
            break;

        case TRACE:
            request = new HttpTrace(uri);
            break;

        case CONNECT:
        default:
            break;
        }

        InputStream inputStream = null;

        MediaType responseMediaType = null;
        try {

            final HttpResponse response = _HttpClient.execute(request);
            final HttpEntity entity = response.getEntity();
            responseMediaType = getContext().getMediaTypeToStringTransformer().bToA(entity.getContentType().getValue());

            if (entity != null) {
                inputStream = entity.getContent();
            }
        }
        catch (final ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final StatusLine statusLine = new DefaultStatusLine();

        // TODO: Implement transformers for the string values to enums
        //statusLine.setHTTPVersion(httpVersion);
        //statusLine.setStatus(status);

        // Create the response message
        final Message responseMessage = createMessage(MessageType.RESPONSE, statusLine);
        Entity responseEntity = responseMessage.getEntity();

        responseEntity.getHeaders().setContentType(responseMediaType);
        responseEntity.getBody().setInputStream(inputStream);

        return responseMessage;
    }
}

abstract class AbstractStartLine implements StartLine {

    private HttpVersion _HttpVersion;

    public HttpVersion getHttpVersion() {
        return _HttpVersion;
    }

    public void setHttpVersion(HttpVersion httpVersion) {
        _HttpVersion = httpVersion;
    }

}

class DefaultAcceptHeader implements AcceptHeader {

}

class DefaultBody implements Body {

    private InputStream _InputStream;
    private OutputStream _OutputStream;

    public InputStream getInputStream() {
        return _InputStream;
    }

    public OutputStream getOutputStream() {
        return _OutputStream;
    }

    public void setInputStream(InputStream inputStream) {
        _InputStream = inputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        _OutputStream = outputStream;
    }

}

class DefaultEntity implements Entity {

    private Body _Body;
    private Headers _Headers;

    public Body getBody() {
        return _Body;
    }

    public Headers getHeaders() {
        return _Headers;
    }

    public void setBody(Body body) {
        _Body = body;
    }

    public void setHeaders(Headers headers) {
        _Headers = headers;
    }

}

class DefaultHeaders implements Headers {

    private AcceptHeader _AcceptHeader;
    private MediaType _ContentType;

    public AcceptHeader getAcceptHeader() {
        return _AcceptHeader;
    }

    public void setAcceptHeader(AcceptHeader acceptHeader) {
        _AcceptHeader = acceptHeader;
    }

    public MediaType getContentType() {
        return _ContentType;
    }

    public void setContentType(MediaType contentType) {
        _ContentType = contentType;
    }

}

class DefaultMessage implements Message {

    private Entity _Entity;
    private StartLine _StartLine;
    private MessageType _Type;

    public Entity getEntity() {
        return _Entity;
    }

    public StartLine getStartLine() {
        return _StartLine;
    }

    public MessageType getType() {
        return _Type;
    }

    public void setEntity(Entity entity) {
        _Entity = entity;
    }

    public void setStartLine(StartLine startLine) {
        _StartLine = startLine;
    }

    public void setType(MessageType type) {
        _Type = type;
    }

}

class DefaultRequestLine extends AbstractStartLine implements RequestLine {

    private Method _Method;
    private URI _RequestUri;

    public Method getMethod() {
        return _Method;
    }

    public URI getUri() {
        return _RequestUri;
    }

    public void setMethod(Method method) {
        _Method = method;
    }

    public void setUri(URI requestUri) {
        _RequestUri = requestUri;
    }

}

class DefaultStatusLine extends AbstractStartLine implements StatusLine {

    private Status _Status;

    public Status getStatus() {
        return _Status;
    }

    public void setStatus(Status status) {
        _Status = status;
    }

}
