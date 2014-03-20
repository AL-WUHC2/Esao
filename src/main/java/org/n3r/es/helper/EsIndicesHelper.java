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
import org.elasticsearch.action.admin.indices.refresh.RefreshRequestBuilder;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.n3r.es.exception.EsaoRuntimeException;
import org.n3r.es.schema.EsSchema;
import org.n3r.es.schema.builder.EsSchemaBuilder;

public class EsIndicesHelper {

    private IndicesAdminClient client;

    public EsIndicesHelper(TransportClient client) {
        this.client = client.admin().indices();
    }

////Indices Exists//////////////////////////////////////////////////////////////

    public boolean indicesExists(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return indicesExists(schema.getIndex());
    }

    public IndicesExistsRequestBuilder prepareExists(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return prepareExists(schema.getIndex());
    }

    /**
     * return true if all of the indexes were exists
     */
    public boolean indicesExists(String... indexes) {
        return prepareExists(indexes).execute().actionGet().isExists();
    }

    public IndicesExistsRequestBuilder prepareExists(String... indexes) {
        return client.prepareExists(lowerCase(indexes));
    }

////Create Index////////////////////////////////////////////////////////////////

    public boolean createIndex(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return createIndex(schema.getIndex());
    }

    public CreateIndexRequestBuilder prepareCreateIndex(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
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

    public boolean deleteIndex(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return deleteIndex(schema.getIndex());
    }

    public DeleteIndexRequestBuilder prepareDeleteIndex(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return prepareDeleteIndex(schema.getIndex());
    }

    public boolean deleteIndex(String... indexes) {
        boolean result = true;
        for (String index : indexes) {
            result &= deleteIndex(index);
        }
        return result;
    }

    public boolean deleteIndex(String index) {
        return !indicesExists(index) || deleteIndexNoCheck(index);
    }

    public boolean deleteIndexNoCheck(String index) {
        return prepareDeleteIndex(index).execute().actionGet().isAcknowledged();
    }

    public DeleteIndexRequestBuilder prepareDeleteIndex(String... indexes) {
        return client.prepareDelete(lowerCase(indexes));
    }

////Refresh Index///////////////////////////////////////////////////////////////

    public void refreshIndex(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        refreshIndex(schema.getIndex());
    }

    public RefreshRequestBuilder prepareRefreshIndex(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return prepareRefreshIndex(schema.getIndex());
    }

    public void refreshIndex(String... indexes) {
        for (String index : indexes) {
            refreshIndex(index);
        }
    }

    public void refreshIndex(String index) {
        if (indicesExists(index)) refreshIndexNoCheck(index);
    }

    public void refreshIndexNoCheck(String index) {
        prepareRefreshIndex(index).execute();
    }

    public RefreshRequestBuilder prepareRefreshIndex(String... indexes) {
        return client.prepareRefresh(lowerCase(indexes));
    }

////Types Exists////////////////////////////////////////////////////////////////

    public boolean typeExists(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return typeExists(schema.getIndex(), schema.getType());
    }

    public TypesExistsRequestBuilder prepareTypeExists(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
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

    public boolean putMapping(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return putMapping(schema.getIndex(), schema.getType(), schema.getSource());
    }

    public PutMappingRequestBuilder preparePutMapping(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
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

    public boolean deleteMapping(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return deleteMapping(schema.getIndex(), schema.getType());
    }

    public DeleteMappingRequestBuilder prepareDeleteMapping(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
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

    public Map<String, Object> getMapping(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        return getMapping(schema.getIndex(), schema.getType());
    }

    public GetMappingsRequestBuilder prepareGetMapping(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
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

////Private/////////////////////////////////////////////////////////////////////

    // Auto lower case indexName.
    // ES is not accept indexNames with UPPERCASE.
    private String[] lowerCase(String... strings) {
        String[] result = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            result[i] = strings[i].toLowerCase();
        }
        return result;
    }

}
