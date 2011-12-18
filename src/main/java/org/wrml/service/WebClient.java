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

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import org.wrml.Context;
import org.wrml.Model;
import org.wrml.model.resource.Action;
import org.wrml.model.resource.Collection;
import org.wrml.model.resource.Document;
import org.wrml.util.UriTransformer;

/*
 * Implements the service interface with a REST API client.
 * 
 * TODO: This is all still very "alpha" - optimize at will.
 */
public class WebClient extends AbstractExecutableService {

    private HttpClient _HttpClient;

    public WebClient(Context context) {
        super(context);

        _HttpClient = new DefaultHttpClient();
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

    @Override
    public Model create() {

        /*
         * TODO: Not really possible without a URI. Should we throw an
         * exception?
         */

        return super.create();
    }

    @Override
    public Model create(URI id) {

        /*
         * TODO: HTTP POST to a collection
         */

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Model create(Model requestor) {

        /*
         * TODO: Design proposal - A POST request using the requestor Model's
         * (Collection's) id?
         */

        return create(((Collection<Document>) requestor).getId(), requestor);
    }

    public Model create(URI id, Model requestor) {

        /*
         * TODO: HTTP POST to a collection
         */

        return null;
    }

    @Override
    public Set<java.util.Map.Entry<URI, Model>> entrySet() {

        /*
         * TODO: Not really possible without a URI. Should we throw an
         * exception?
         */

        return super.entrySet();
    }

    @Override
    public Model execute(URI id) {

        /*
         * TODO: HTTP POST
         */

        return super.execute(id);
    }

    @Override
    public Model execute(URI id, Action action) {

        /*
         * TODO: HTTP POST an action. Note that the Action should be built by
         * model framework's Link clicking
         */

        return super.execute(id, action);
    }

    public Model execute(URI id, Action action, Model requestor) {

        /*
         * TODO: HTTP POST an action
         */

        return null;
    }

    @Override
    public Model execute(URI id, Model requestor) {

        /*
         * TODO: HTTP POST
         */

        return super.execute(id, requestor);
    }

    @Override
    public Model get(Object key) {

        /*
         * TODO: HTTP GET
         */

        /*
         * try {
         * HttpGet httpget = new HttpGet("http://www.google.com/");
         * 
         * System.out.println("executing request " + httpget.getURI());
         * 
         * // Create a response handler
         * ResponseHandler<String> responseHandler = new BasicResponseHandler();
         * String responseBody = httpclient.execute(httpget, responseHandler);
         * System.out.println("----------------------------------------");
         * System.out.println(responseBody);
         * System.out.println("----------------------------------------");
         * 
         * }
         * finally {
         * // When HttpClient instance is no longer needed,
         * // shut down the connection manager to ensure
         * // immediate deallocation of all system resources
         * httpclient.getConnectionManager().shutdown();
         * }
         */
        // TODO Auto-generated method stub
        return super.get(key);
    }

    public Model get(URI id, Model requestor) {

        /*
         * TODO: HTTP GET
         */

        return null;
    }

    public UriTransformer getIdTransformer(Model requestor) {

        // By default, this service treat's URIs as native identifiers, 
        // therefore we can use a passthrough transformer.

        return PassthroughUriTransformer.Instance;
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

    @Override
    public Model put(URI id, Model modelToSave) {

        /*
         * TODO: HTTP PUT
         */

        return super.put(id, modelToSave);
    }

    public Model put(URI id, Model modelToSave, Model requestor) {
        /*
         * TODO: HTTP PUT
         */
        return null;
    }

    @Override
    public void putAll(Map<? extends URI, ? extends Model> map) {

        /*
         * TODO: HTTP PUT (batch/transactional?)
         */

        super.putAll(map);
    }

    @Override
    public Model remove(Object key) {
        /*
         * TODO: HTTP DELETE
         */
        return super.remove(key);
    }

    public Model remove(URI id, Model requestor) {
        /*
         * TODO: HTTP DELETE
         */
        return null;
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
    public java.util.Collection<Model> values() {
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

    public static class PassthroughUriTransformer implements UriTransformer {

        public static final PassthroughUriTransformer Instance = new PassthroughUriTransformer();

        public Object aToB(URI aValue) {
            return aValue;
        }

        public URI bToA(Object bValue) {
            return (URI) bValue;
        }

    }

}
