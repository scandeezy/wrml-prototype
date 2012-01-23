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

package org.wrml.runtime.system.transformer;

import java.net.URI;
import java.util.WeakHashMap;

import org.wrml.model.format.Format;
import org.wrml.model.schema.Type;
import org.wrml.runtime.Context;
import org.wrml.runtime.Contextual;
import org.wrml.transformer.CachingTransformer;
import org.wrml.transformer.ConstantTransformation;
import org.wrml.transformer.Transformer;
import org.wrml.www.MediaType;

public final class SystemTransformers extends Contextual {

    public static final URI DEFAULT_FORMAT_API_DOCROOT = URI.create("http://api.formats.wrml.org/");
    public static final URI DEFAULT_SCHEMA_API_DOCROOT = URI.create("http://api.schemas.wrml.org/");

    private final Transformer<java.lang.reflect.Type, Class<?>> _NativeTypeToClassTransformer;
    private final Transformer<java.lang.reflect.Type, Type> _NativeTypeToTypeTransformer;
    private final Transformer<Class<?>, String> _ClassToNameTransformer;
    private final Transformer<Class<?>, URI> _ClassToSchemaIdTransformer;
    private final Transformer<MediaType, URI> _MediaTypeToFormatIdTransformer;
    private final Transformer<MediaType, Format> _MediaTypeToFormatTransformer;
    private final Transformer<MediaType, java.lang.reflect.Type> _MediaTypeToNativeTypeTransformer;
    private final Transformer<MediaType, URI> _MediaTypeToSchemaIdTransformer;
    private final Transformer<URI, String> _SchemaIdToFullNameTransformer;

    public SystemTransformers(Context context) {
        super(context);

        _NativeTypeToClassTransformer = new CachingTransformer<java.lang.reflect.Type, Class<?>, ConstantTransformation<java.lang.reflect.Type, Class<?>>>(
                new NativeTypeToClassTransformer(context), new WeakHashMap<java.lang.reflect.Type, Class<?>>(),
                new WeakHashMap<Class<?>, java.lang.reflect.Type>());

        _NativeTypeToTypeTransformer = new CachingTransformer<java.lang.reflect.Type, Type, ConstantTransformation<java.lang.reflect.Type, Type>>(
                new NativeTypeToTypeTransformer(context), new WeakHashMap<java.lang.reflect.Type, Type>(),
                new WeakHashMap<Type, java.lang.reflect.Type>());

        _ClassToNameTransformer = CachingTransformer.create(new ClassToNameTransformer(context),
                new WeakHashMap<Class<?>, String>(), new WeakHashMap<String, Class<?>>());

        _MediaTypeToNativeTypeTransformer = CachingTransformer.create(new MediaTypeToNativeTypeTransformer(context));
        _MediaTypeToSchemaIdTransformer = CachingTransformer.create(new MediaTypeToSchemaIdTransformer(context));

        // TODO: Check config for these docroots        

        _SchemaIdToFullNameTransformer = CachingTransformer.create(new SchemaIdToFullNameTransformer(
                DEFAULT_SCHEMA_API_DOCROOT));
        _MediaTypeToFormatIdTransformer = CachingTransformer.create(new MediaTypeToFormatIdTransformer(context,
                DEFAULT_FORMAT_API_DOCROOT));
        _ClassToSchemaIdTransformer = CachingTransformer.create(new ClassToSchemaIdTransformer(context));
        _MediaTypeToFormatTransformer = CachingTransformer.create(new MediaTypeToFormatTransformer(context));

    }

    public Transformer<Class<?>, String> getClassToNameTransformer() {
        return _ClassToNameTransformer;
    }

    public Transformer<Class<?>, URI> getClassToSchemaIdTransformer() {
        return _ClassToSchemaIdTransformer;
    }

    public Transformer<MediaType, URI> getMediaTypeToFormatIdTransformer() {
        return _MediaTypeToFormatIdTransformer;
    }

    public Transformer<MediaType, Format> getMediaTypeToFormatTransformer() {
        return _MediaTypeToFormatTransformer;
    }

    public Transformer<MediaType, java.lang.reflect.Type> getMediaTypeToNativeTypeTransformer() {
        return _MediaTypeToNativeTypeTransformer;
    }

    public Transformer<MediaType, URI> getMediaTypeToSchemaIdTransformer() {
        return _MediaTypeToSchemaIdTransformer;
    }

    public Transformer<java.lang.reflect.Type, Class<?>> getNativeTypeToClassTransformer() {
        return _NativeTypeToClassTransformer;
    }

    public Transformer<java.lang.reflect.Type, Type> getNativeTypeToTypeTransformer() {
        return _NativeTypeToTypeTransformer;
    }

    public Transformer<URI, String> getSchemaIdToFullNameTransformer() {
        return _SchemaIdToFullNameTransformer;
    }

}
