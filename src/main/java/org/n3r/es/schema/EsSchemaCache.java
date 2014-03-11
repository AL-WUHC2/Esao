package org.n3r.es.schema;

import static org.n3r.core.collection.RMap.newHashMap;

import java.util.Map;

import org.n3r.core.collection.RMap;
import org.n3r.es.schema.builder.EsClassPropsBuilder;
import org.n3r.es.schema.builder.EsIdSettingBuilder;
import org.n3r.es.schema.builder.EsIndexNameBuilder;
import org.n3r.es.schema.builder.EsTypeNameBuilder;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class EsSchemaCache {

    private static LoadingCache<Class<?>, EsSchema> schemaCache;

    private static Map<String, Class<?>> reflectMap = newHashMap();

    static {
        schemaCache = CacheBuilder.newBuilder().maximumSize(1000).build(
                new CacheLoader<Class<?>, EsSchema>() {
                    @Override
                    public EsSchema load(Class<?> key) throws Exception {
                        String indexName = new EsIndexNameBuilder(key).indexName();
                        String typeName = new EsTypeNameBuilder(key).typeName();
                        reflectMap.put(indexName.toLowerCase() + ":" + typeName, key);

                        Map<String, Object> typeMapping = RMap.<String, Object>of(
                                "properties", new EsClassPropsBuilder(key).props(),
                                "_id", new EsIdSettingBuilder(key).setting());
                        String source = JSON.toJSONString(RMap.<String, Object>of(typeName, typeMapping));
                        return new EsSchema(indexName, typeName, source);
                    }
                }
        );
    }

    public static EsSchema get(Class<?> clazz) {
        return schemaCache.getUnchecked(clazz);
    }

    public static Class<?> reflect(String reflectKey) {
        return reflectMap.get(reflectKey);
    }

}
