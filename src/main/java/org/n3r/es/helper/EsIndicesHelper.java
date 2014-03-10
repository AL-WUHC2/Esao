package org.n3r.es.helper;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.delete.DeleteMappingRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.n3r.es.exception.EsaoRuntimeException;
import org.n3r.es.schema.EsSchema;

public class EsIndicesHelper {

    private IndicesAdminClient client;

    public EsIndicesHelper(TransportClient client) {
        this.client = client.admin().indices();
    }

////Indices Exists//////////////////////////////////////////////////////////////

    public boolean indicesExists(EsSchema schema) {
        return indicesExists(schema.getIndex());
    }

    public IndicesExistsRequestBuilder prepareExists(EsSchema schema) {
        return prepareExists(schema.getIndex());
    }

    /**
     * return true if all of the indexes were exists
     */
    public boolean indicesExists(String index) {
        return prepareExists(index).execute().actionGet().isExists();
    }

    public IndicesExistsRequestBuilder prepareExists(String index) {
        return client.prepareExists(index.toLowerCase());
    }

////Create Index////////////////////////////////////////////////////////////////

    public boolean createIndex(EsSchema schema) {
        return createIndex(schema.getIndex());
    }

    public CreateIndexRequestBuilder prepareCreateIndex(EsSchema schema) {
        return prepareCreateIndex(schema.getIndex());
    }

    public boolean createIndex(String index) {
        return indicesExists(index) || createIndexNoCheck(index);
    }

    public boolean createIndexNoCheck(String index) {
        return prepareCreateIndex(index).execute().actionGet().isAcknowledged();
    }

    public CreateIndexRequestBuilder prepareCreateIndex(String index) {
        return client.prepareCreate(index.toLowerCase());
    }

////Delete Index////////////////////////////////////////////////////////////////

    public boolean deleteIndex(EsSchema schema) {
        return deleteIndex(schema.getIndex());
    }

    public DeleteIndexRequestBuilder prepareDeleteIndex(EsSchema schema) {
        return prepareDeleteIndex(schema.getIndex());
    }

    public boolean deleteIndex(String index) {
        return !indicesExists(index) || deleteIndexNoCheck(index);
    }

    public boolean deleteIndexNoCheck(String index) {
        return prepareDeleteIndex(index).execute().actionGet().isAcknowledged();
    }

    public DeleteIndexRequestBuilder prepareDeleteIndex(String index) {
        return client.prepareDelete(index.toLowerCase());
    }

////Types Exists////////////////////////////////////////////////////////////////

    public boolean typeExists(EsSchema schema) {
        return typeExists(schema.getIndex(), schema.getType());
    }

    public TypesExistsRequestBuilder prepareTypeExists(EsSchema schema) {
        return prepareTypeExists(schema.getIndex(), schema.getType());
    }

    public boolean typeExists(String index, String type) {
        return indicesExists(index) && typeExistsNoCheck(index, type);
    }

    public boolean typeExistsNoCheck(String index, String type) {
        return prepareTypeExists(index, type).execute().actionGet().isExists();
    }

    public TypesExistsRequestBuilder prepareTypeExists(String index, String type) {
        return client.prepareTypesExists(index.toLowerCase()).setTypes(type);
    }

////Put Mapping/////////////////////////////////////////////////////////////////

    public boolean putMapping(EsSchema schema) {
        return putMapping(schema.getIndex(), schema.getType(), schema.getSource());
    }

    public PutMappingRequestBuilder preparePutMapping(EsSchema schema) {
        return preparePutMapping(schema.getIndex(), schema.getType(), schema.getSource());
    }

    public boolean putMapping(String index, String type, String source) {
        return createIndex(index) && (typeExistsNoCheck(index, type)
                || putMappingNoCheck(index, type, source));
    }

    public boolean putMappingNoCheck(String index, String type, String source) {
        return preparePutMapping(index, type, source).execute().actionGet().isAcknowledged();
    }

    public PutMappingRequestBuilder preparePutMapping(String index, String type, String source) {
        return client.preparePutMapping(index.toLowerCase()).setType(type).setSource(source);
    }

////Delete Mapping//////////////////////////////////////////////////////////////

    public boolean deleteMapping(EsSchema schema) {
        return deleteMapping(schema.getIndex(), schema.getType());
    }

    public DeleteMappingRequestBuilder prepareDeleteMapping(EsSchema schema) {
        return prepareDeleteMapping(schema.getIndex(), schema.getType());
    }

    public boolean deleteMapping(String index, String type) {
        return !indicesExists(index) || !typeExistsNoCheck(index, type)
                || deleteMappingNoCheck(index, type);
    }

    public boolean deleteMappingNoCheck(String index, String type) {
        return prepareDeleteMapping(index, type).execute().actionGet().isAcknowledged();
    }

    public DeleteMappingRequestBuilder prepareDeleteMapping(String index, String type) {
        return client.prepareDeleteMapping(index.toLowerCase()).setType(type);
    }

////Get Mapping/////////////////////////////////////////////////////////////////

    public Map<String, Object> getMapping(EsSchema schema) {
        return getMapping(schema.getIndex(), schema.getType());
    }

    public GetMappingsRequestBuilder prepareGetMapping(EsSchema schema) {
        return prepareGetMapping(schema.getIndex(), schema.getType());
    }

    public Map<String, Object> getMapping(String index, String type) {
        if (!indicesExists(index)) return null;
        if (!typeExistsNoCheck(index, type)) return null;
        return getMappingNoCheck(index, type);
    }

    public Map<String, Object> getMappingNoCheck(String index, String type) {
        try {
            return prepareGetMapping(index, type).execute().actionGet().getMappings()
                    .get(index.toLowerCase()).get(type).sourceAsMap();
        } catch (IOException e) {
            throw new EsaoRuntimeException("GetMapping for index:"
                    + index.toLowerCase() + " type:" + type + " Exception!", e);
        }
    }

    public GetMappingsRequestBuilder prepareGetMapping(String index, String type) {
        return client.prepareGetMappings(index.toLowerCase()).setTypes(type);
    }

}
