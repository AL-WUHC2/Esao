package org.n3r.es.cache;

import org.n3r.core.joor.Reflect;
import org.n3r.es.query.result.EsBeanRsMapper;
import org.n3r.es.query.result.EsMapRsMapper;
import org.n3r.es.query.result.EsRsMapper;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class EsMapperCache {

    private static LoadingCache<Class<?>, EsRsMapper> mapperCache;

    static {
        mapperCache = CacheBuilder.newBuilder().maximumSize(1000).build(
                new CacheLoader<Class<?>, EsRsMapper>() {
                    @Override
                    public EsRsMapper load(Class<?> key) throws Exception {
                        return EsRsMapper.class.isAssignableFrom(key)
                                ? Reflect.on(key).create().<EsRsMapper>get()
                                        : new EsBeanRsMapper(key);
                    }
                }
        );
    }

    public static EsRsMapper get(Class<?> clazz) {
        if (clazz == null) return new EsMapRsMapper();
        return mapperCache.getUnchecked(clazz);
    }

}
