package org.n3r.es.schema.builder;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.n3r.es.cache.EsSchemaCache;
import org.n3r.es.exception.EsaoRuntimeException;
import org.n3r.es.schema.EsSchema;

public class EsSchemaBuilder {

    private EsSchema schema;

    public EsSchemaBuilder() {
        schema = new EsSchema();
    }

    public EsSchemaBuilder(String index, String type, String source, String idFieldPath) {
        schema = new EsSchema(index, type, source, idFieldPath);
    }

    public EsSchemaBuilder(Class<?> clazz) {
        this.schema(clazz);
    }

    public EsSchemaBuilder(Object object) {
        this.schema(object);
    }

    public String index() {
        return schema.getIndex();
    }

    public EsSchemaBuilder index(String index) {
        schema.setIndex(index);
        return this;
    }

    public String type() {
        return schema.getType();
    }

    public EsSchemaBuilder type(String type) {
        schema.setType(type);
        return this;
    }

    public String source() {
        return schema.getSource();
    }

    public EsSchemaBuilder source(String source) {
        schema.setSource(source);
        return this;
    }

    public EsSchemaBuilder source(XContentBuilder source) {
        try {
            schema.setSource(source.string());
            return this;
        } catch (IOException e) {
            throw new EsaoRuntimeException("Failed to build json for mapping request", e);
        }
    }

    public EsSchemaBuilder source(Map source) {
        try {
            schema.setSource(XContentFactory.jsonBuilder().map(source).string());
            return this;
        } catch (IOException e) {
            throw new EsaoRuntimeException("Failed to generate [" + source + "]", e);
        }
    }

    public EsSchema schema() {
        return schema;
    }

    public EsSchemaBuilder schema(Class<?> clazz) {
        schema = EsSchemaCache.get(clazz);
        return this;
    }

    public EsSchemaBuilder schema(Object object) {
        return schema(object.getClass());
    }

}
