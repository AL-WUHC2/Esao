package org.n3r.es.cache;

import org.n3r.core.joor.Reflect;
import org.n3r.es.query.result.EsBeanSourceMapper;
import org.n3r.es.query.result.EsMapSourceMapper;
import org.n3r.es.query.result.EsSourceMapper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class EsMapperCache {

    private static LoadingCache<Class<?>, EsSourceMapper> mapperCache;

    static {
        mapperCache = CacheBuilder.newBuilder().maximumSize(1000).build(
                new CacheLoader<Class<?>, EsSourceMapper>() {
                    @Override
                    public EsSourceMapper load(Class<?> key) throws Exception {
                        return EsSourceMapper.class.isAssignableFrom(key)
                                ? Reflect.on(key).create().<EsSourceMapper>get()
                                        : new EsBeanSourceMapper(key);
                    }
                }
        );
    }

    public static EsSourceMapper get(Class<?> clazz) {
        if (clazz == null) return new EsMapSourceMapper();
        return mapperCache.getUnchecked(clazz);
    }

}
