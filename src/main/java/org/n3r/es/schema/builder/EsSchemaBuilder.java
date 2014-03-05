package org.n3r.es.schema.builder;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.n3r.core.collection.RMap;
import org.n3r.es.exception.EsaoRuntimeException;
import org.n3r.es.schema.EsSchema;

import com.alibaba.fastjson.JSON;

public class EsSchemaBuilder {

    private EsSchema schema;

    public EsSchemaBuilder() {
        schema = new EsSchema();
    }

    public EsSchemaBuilder(String index, String type, String source) {
        schema = new EsSchema(index, type, source);
    }

    public EsSchemaBuilder(String[] indexs, String type, String source) {
        schema = new EsSchema(indexs, type, source);
    }

    public EsSchemaBuilder(Class<?> clazz) {
        this.schema(clazz);
    }

    public String[] indexs() {
        return schema.getIndexs();
    }

    public EsSchemaBuilder index(String index) {
        return indexs(new String[] { index });
    }

    public EsSchemaBuilder indexs(String[] indexs) {
        schema.setIndexs(indexs);
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
        String[] indexNames = new EsIndexNameBuilder(clazz).indexNames();
        String typeName = new EsTypeNameBuilder(clazz).typeName();
        Map<String, Object> typeMapping = RMap.<String, Object>of(
                "properties", new EsClassPropsBuilder(clazz).props(),
                "_id", new EsIdSettingBuilder(clazz).setting());
        String source = JSON.toJSONString(RMap.<String, Object>of(typeName, typeMapping));
        schema = new EsSchema(indexNames, typeName, source);
        return this;
    }

    public EsSchemaBuilder schema(Object object) {
        return schema(object.getClass());
    }

}
