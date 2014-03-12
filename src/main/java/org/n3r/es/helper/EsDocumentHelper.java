package org.n3r.es.helper;

import java.nio.charset.Charset;
import java.util.Map;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.n3r.es.schema.EsSchema;
import org.n3r.es.schema.EsSchemaCache;
import org.n3r.es.schema.builder.EsSchemaBuilder;

import com.alibaba.fastjson.JSON;

public class EsDocumentHelper {

    private TransportClient client;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public EsDocumentHelper(TransportClient client) {
        this.client = client;
    }

////Index///////////////////////////////////////////////////////////////////////

    public IndexResponse index(Object obj) {
        return index(obj, null);
    }

    public IndexResponse index(Object obj, String id) {
        return prepareIndex(obj, id).execute().actionGet();
    }

    public IndexRequestBuilder prepareIndex(Object obj) {
        return prepareIndex(obj, null);
    }

    public IndexRequestBuilder prepareIndex(Object obj, String id) {
        EsSchema schema = new EsSchemaBuilder(obj).schema();
        return prepareIndex(schema.getIndex(), schema.getType(), id, JSON.toJSONString(obj));
    }

    public IndexResponse index(String index, String type, String source) {
        return index(index, type, null, source);
    }

    public IndexResponse index(String index, String type, String id, String source) {
        return prepareIndex(index, type, id, source).execute().actionGet();
    }

    public IndexRequestBuilder prepareIndex(String index, String type, String source) {
        return prepareIndex(index, type, null, source);
    }

    // If Mapping specify _id field to extract the id from a different location
    // in the source document, @param id will cover it without change the source.
    public IndexRequestBuilder prepareIndex(String index, String type, String id, String source) {
        return client.prepareIndex(index, type, id).setSource(source.getBytes(UTF8));
    }

////Get/////////////////////////////////////////////////////////////////////////

    public <T> T get(Class<T> clazz, String id) {
        return (T) parseRawGetResponse(getRaw(clazz, id), clazz);
    }

    public GetResponse getRaw(Class<?> clazz, String id) {
        return prepareGet(clazz, id).execute().actionGet();
    }

    public GetRequestBuilder prepareGet(Class<?> clazz, String id) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return prepareGet(schema.getIndex(), schema.getType(), id);
    }

    public Object get(String index, String type, String id) {
        return parseRawGetResponse(getRaw(index, type, id), null);
    }

    public GetResponse getRaw(String index, String type, String id) {
        return prepareGet(index, type, id).execute().actionGet();
    }

    public GetRequestBuilder prepareGet(String index, String type, String id) {
        return client.prepareGet(index, type, id);
    }

    /*
     * Reflect response source JSON to bean.
     * Type uncached return source JSON.
     */
    private Object parseRawGetResponse(GetResponse response, Class<?> clazz) {
        if (!response.isExists() || response.isSourceEmpty()) return null;

        String source = response.getSourceAsString();
        Class<?> reflectClazz = clazz != null ? clazz :
            EsSchemaCache.reflect(response.getIndex() + ":" + response.getType());
        if (reflectClazz == null) return source;

        return JSON.parseObject(source, reflectClazz);
    }

////Delete//////////////////////////////////////////////////////////////////////

    public DeleteResponse delete(Class<?> clazz, String id) {
        return prepareDelete(clazz, id).execute().actionGet();
    }

    public DeleteRequestBuilder prepareDelete(Class<?> clazz, String id) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return prepareDelete(schema.getIndex(), schema.getType(), id);
    }

    public DeleteResponse delete(String index, String type, String id) {
        return prepareDelete(index, type, id).execute().actionGet();
    }

    public DeleteRequestBuilder prepareDelete(String index, String type, String id) {
        return client.prepareDelete(index, type, id);
    }

////Update//////////////////////////////////////////////////////////////////////

    public UpdateResponse update(Object obj, String id) {
        return prepareUpdate(obj, id).execute().actionGet();
    }

    public UpdateResponse update(Class<?> clazz, String id, Map source) {
        return prepareUpdate(clazz, id, source).execute().actionGet();
    }

    public UpdateRequestBuilder prepareUpdate(Object obj, String id) {
        EsSchema schema = new EsSchemaBuilder(obj).schema();
        return prepareUpdate(schema.getIndex(), schema.getType(), id, JSON.toJSONString(obj));
    }

    private UpdateRequestBuilder prepareUpdate(Class<?> clazz, String id, Map source) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return prepareUpdate(schema.getIndex(), schema.getType(), id, JSON.toJSONString(source));
    }

    public UpdateResponse update(String index, String type, String id, Map source) {
        return prepareUpdate(index, type, id, source).execute().actionGet();
    }

    public UpdateResponse update(String index, String type, String id, String source) {
        return prepareUpdate(index, type, id, source).execute().actionGet();
    }

    public UpdateRequestBuilder prepareUpdate(String index, String type, String id, Map source) {
        return prepareUpdate(index, type, id, JSON.toJSONString(source));
    }

    // insert if not exists
    public UpdateRequestBuilder prepareUpdate(String index, String type, String id, String source) {
        return client.prepareUpdate(index, type, id).setDoc(source).setDocAsUpsert(true);
    }

}
