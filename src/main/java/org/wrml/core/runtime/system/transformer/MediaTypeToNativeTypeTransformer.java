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

package org.wrml.core.runtime.system.transformer;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URI;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.wrml.core.Model;
import org.wrml.core.runtime.Context;
import org.wrml.core.transformer.Transformer;
import org.wrml.core.transformer.Transformers;
import org.wrml.core.www.MediaType;

public class MediaTypeToNativeTypeTransformer extends ConstantTransformer<MediaType, java.lang.reflect.Type> {

    public MediaTypeToNativeTypeTransformer(Context context) {
        super(context);
    }

    public java.lang.reflect.Type aToB(MediaType mediaType) {

        final Context context = getContext();
        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final URI schemaId = systemTransformers.getMediaTypeToSchemaIdTransformer().aToB(mediaType);

        /*
         * TODO: Does this code need to consider a possible parameterized media
         * type? Should the returned Type also be generic (parameterized) in a
         * way that mirrors the media type? If so, how can we map the media type
         * to a accurately "generic" (parameterized) java.lang.reflect.Type?
         * Is it possible for us to instantiate or look up a ParameterizedType
         * based on media types that describe schemas like "SchemaA<SchemaB>" ?
         * 
         * As it stands this method will return the non-generic schema
         * interface (e.g. will always return SchemaA, not SchemaA<SchemaB>).
         */

        return systemTransformers.getClassToSchemaIdTransformer().bToA(schemaId);
    }

    public MediaType bToA(java.lang.reflect.Type type) {

        if (type == null) {
            return null;
        }

        MediaType mediaType = null;

        final Context context = getContext();
        final SystemTransformers systemTransformers = context.getSystemTransformers();
        final Transformers<String> stringTransformers = context.getStringTransformers();

        final Transformer<Type, Class<?>> nativeTypeToClassTransformer = systemTransformers
                .getNativeTypeToClassTransformer();
        final Class<?> clazz = nativeTypeToClassTransformer.aToB(type);

        if (Model.class.isAssignableFrom(clazz)) {

            String mediaTypeString = null;

            if (type.equals(Model.class)) {
                // Return the vanilla application/wrml type for base models                    
                mediaTypeString = MediaType.MEDIA_TYPE_STRING_WRML;
            }
            else {

                final Transformer<Class<?>, URI> classToSchemaIdTransformer = systemTransformers
                        .getClassToSchemaIdTransformer();
                final Transformer<URI, String> uriToStringTransformer = stringTransformers.getTransformer(URI.class);

                // Return the application/wrml with schema param for derived models
                final URI schemaId = classToSchemaIdTransformer.aToB(clazz);

                final SortedMap<String, String> parameters = new TreeMap<String, String>();

                // Allow for parameterized types via Constraints
                final Type[] typeParameters = context.getTypeSystem().getNativeTypeParameters(type);
                if (typeParameters != null) {

                    final TypeVariable<?>[] typeVars = clazz.getTypeParameters();
                    if ((typeVars == null) || (typeVars.length != typeParameters.length)) {
                        throw new IllegalStateException();
                    }

                    final int paramCount = typeParameters.length;

                    for (int i = 0; i < paramCount; i++) {

                        // Get the type param name (<T> ---> "T")
                        final TypeVariable<?> typeVar = typeVars[i];
                        final String typeVarName = typeVar.getName();

                        // Get the type param value (<Foo> ---> Foo)
                        final Type typeParam = typeParameters[i];
                        final Class<?> typeParamSchemaClass = nativeTypeToClassTransformer.aToB(typeParam);
                        final URI typeParamSchemaId = classToSchemaIdTransformer.aToB(typeParamSchemaClass);
                        final String typeParamSchemaIdString = uriToStringTransformer.aToB(typeParamSchemaId);

                        parameters.put(typeVarName, typeParamSchemaIdString);
                    }
                }

                mediaTypeString = createWrmlMediaTypeString(schemaId, parameters);
            }

            mediaType = stringTransformers.getTransformer(MediaType.class).bToA(mediaTypeString);
        }
        else {
            // TODO: Get MediaType associated with non-WRML class
        }

        return mediaType;
    }

    public String createWrmlMediaTypeString(final URI schemaId) {
        return createWrmlMediaTypeString(schemaId, null);
    }

    public String createWrmlMediaTypeString(final URI schemaId, final Map<String, String> parameters) {
        final Context context = getContext();
        final Transformers<String> stringTransformers = context.getStringTransformers();
        final String schemaIdString = stringTransformers.getTransformer(URI.class).aToB(schemaId);
        return MediaType.createWrmlMediaTypeString(schemaIdString, parameters);
    }

}
