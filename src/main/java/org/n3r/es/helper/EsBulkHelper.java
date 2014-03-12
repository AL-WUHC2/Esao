package org.n3r.es.helper;

import java.util.Map;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;

public class EsBulkHelper {

    private BulkRequestBuilder bulkBuilder;

    private EsDocumentHelper documentHelper;

    public EsBulkHelper(TransportClient client) {
        this.bulkBuilder = client.prepareBulk();
        this.documentHelper = new EsDocumentHelper(client);
    }

////Index Bulk//////////////////////////////////////////////////////////////////

    public EsBulkHelper index(Object obj) {
        bulkBuilder.add(documentHelper.prepareIndex(obj));
        return this;
    }

    public EsBulkHelper index(Object obj, String id) {
        bulkBuilder.add(documentHelper.prepareIndex(obj, id));
        return this;
    }

    public EsBulkHelper index(String index, String type, String source) {
        bulkBuilder.add(documentHelper.prepareIndex(index, type, source));
        return this;
    }

    public EsBulkHelper index(String index, String type, String id, String source) {
        bulkBuilder.add(documentHelper.prepareIndex(index, type, id, source));
        return this;
    }

////Delete Bulk/////////////////////////////////////////////////////////////////

    public EsBulkHelper delete(Class<?> clazz, String id) {
        bulkBuilder.add(documentHelper.prepareDelete(clazz, id));
        return this;
    }

    public EsBulkHelper delete(String index, String type, String id) {
        bulkBuilder.add(documentHelper.prepareDelete(index, type, id));
        return this;
    }

////Update Bulk/////////////////////////////////////////////////////////////////

    public EsBulkHelper update(Object obj, String id) {
        bulkBuilder.add(documentHelper.prepareUpdate(obj, id));
        return this;
    }

    public EsBulkHelper update(Class<?> clazz, String id, Map source) {
        bulkBuilder.add(documentHelper.prepareUpdate(clazz, id, source));
        return this;
    }

    public EsBulkHelper update(String index, String type, String id, Map source) {
        bulkBuilder.add(documentHelper.prepareUpdate(index, type, id, source));
        return this;
    }

    public EsBulkHelper update(String index, String type, String id, String source) {
        bulkBuilder.add(documentHelper.prepareUpdate(index, type, id, source));
        return this;
    }

////Bulk Execute////////////////////////////////////////////////////////////////

    public BulkResponse execute() {
        BulkResponse response = bulkBuilder.execute().actionGet();
        bulkBuilder.request().requests().clear();
        return response;
    }

}
