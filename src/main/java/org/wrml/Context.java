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

import java.net.URI;
import java.util.List;

import org.wrml.model.UriTemplateParameter;
import org.wrml.model.restapi.LinkTemplate;
import org.wrml.model.schema.Prototype;
import org.wrml.model.schema.Schema;
import org.wrml.util.ObservableList;
import org.wrml.util.ObservableMap;

/**
 * A stateful class that exposes wrml object-based services to wrml objects and
 * offers some utility functions.
 */
public abstract class Context {

    /*
     * 
     * Thread Dimension (Runtime Thread Local)
     * 
     * TODO: Consider making Context have a global static AND a thread local
     * instance, the latter of which takes hierarchically "scoped" precedence
     * over the former.
     * 
     * This will allow for the Context to be read "globally" within a scope that
     * makes logical sense in multithreaded programs, like an web resource
     * server.
     * 
     * That being said, by handing each Model its own Context there is the
     * opportunity to create some very fine-grained scopes using context
     * implementations, meaning that the thread local scope may be different but
     * "parented" by the global context.
     * 
     * Reference Dimension (Design-time Composition)
     * 
     * Parenting means making contexts hierarchical using object references,
     * meaning that a context could have a parent and children. On the server
     * side this might be used relate the thread local context to the parent
     * context, even to the point of the child delegating (cascading) to the
     * parent reference for a default value or behavior that doesn't need to be
     * overridden locally.
     * 
     * This hierarchy can also be a useful abstraction when building
     * applications such as user interfaces, which may have nested contexts for
     * data and associated actions (e.g. plug-ins). In these situations the
     * thread local may not be an effective slot to achieve context locality.
     * 
     * Polymorphism Dimension (Design-time Inheritance)
     * 
     * Context subclasses can alter the default behavior and values by
     * overriding certain methods (and obviously by implementing the absract
     * ones).
     */

    /*
     * TODO: Eventually need to figure out how to make LinkRelation and other
     * core types extend Model. This is the dog food that WRML must eat in
     * order to use itself to make itself (bootstrap).
     * 
     * This will allow WRML to treat these core type instance "loadings" just
     * like any other application using WRML. Meaning that there is an
     * associated service class for each core type. This association can be
     * formed at program start-up (via config/wiring) which results in a
     * registration by schema within the Context. The mapping can be changed as
     * needed at runtime. The type registration can be repeated for the same
     * service and may also include simple URI wildcard patterns to allow for a
     * single service to handle many schema types. For example an API that
     * stores schema instances in a flexible document store (e.g. mongoDB) could
     * handle requests for many different schema types and CRUD them with ease.
     * 
     * For the Java implementation of WRML to leverage itself (bootstrap), for
     * its own core APIs and tools, all core WRML classes must be stripped down
     * to interfaces, with all of their implementation responsibilities shifted
     * to the framework. This is what it will take to be a truly web resource
     * model-oriented, metadata-driven application framework.
     * 
     * For starters we could use Java interface definitions as placeholders for
     * these core types schema's being accessible via the schema REST API. Hand
     * coding the interface with a mindset of auto-generation, meaning simple
     * repeatable patterns for method signatures and that reference types that
     * can themselves be easily generated and fetched. This will allow these
     * interfaces to later be generated by code that is built into the runtime
     * and compile time environments to allow the eventual web based schemas to
     * be coded against directly.
     * 
     * The impl subclasses could be instantiated using a Proxy class that calls
     * the Model's getFieldValue or Link.click. This drives all
     * implementation concerns down into the framework's Model, its Links,
     * and the supporting Context. With the Context providing hooks for
     * application code.
     * 
     * This Proxy approach may prove to be the final solution or we may decide
     * to autogenerate impl classes via our own code generation.
     */

    // TODO: Moved from UriTemplate class. Refactor to make sense.
    public URI execute(final Link link) {

        final Model obj = link.getOwner();
        final LinkTemplate linkTemplate = link.getLinkTemplate();

        final ObservableMap<URI, ObservableList<UriTemplateParameter>> destinationUriTemplateParameters = linkTemplate
                .getDestinationUriTemplateParameters();

        final List<UriTemplateParameter> uriTemplateParameters = destinationUriTemplateParameters
                .get(obj.getSchemaId());

        return execute(obj, uriTemplateParameters);
    }

    public URI execute(final Model obj, final List<UriTemplateParameter> uriTemplateParameters) {
        // TODO Auto-generated method stub
        return null;
    }

    public Model getModel(Class<?> clazz, URI modelId, Model requestor) {
        return getServiceForClass(clazz).get(modelId, requestor);
    }

    public Prototype getPrototype(URI prototypeId, Model requestor) {
        return (Prototype) getModel(Prototype.class, prototypeId, requestor);
    }

    public Schema getSchema(URI schemaId, Model requestor) {
        return (Schema) getModel(Schema.class, schemaId, requestor);
    }

    public URI getSchemaIdForClass(Class<?> clazz) {
        return getSchemaIdForClassName(clazz.getCanonicalName());
    }

    public abstract URI getSchemaIdForClassName(String className);

    public abstract Service<?> getService(URI schemaId);

    public Service<?> getServiceForClass(Class<?> clazz) {
        return getService(getSchemaIdForClass(clazz));
    }

}
