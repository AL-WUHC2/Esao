package org.n3r.es.helper;

import java.io.IOException;
import java.util.HashMap;
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
        return indicesExists(schema.getIndexes());
    }

    public IndicesExistsRequestBuilder prepareExists(EsSchema schema) {
        return prepareExists(schema.getIndexes());
    }

    /**
     * return true if all of the indexes were exists
     */
    public boolean indicesExists(String... indexes) {
        return prepareExists(indexes).execute().actionGet().isExists();
    }

    public IndicesExistsRequestBuilder prepareExists(String... indexes) {
        return client.prepareExists(indexToLower(indexes));
    }

////Create Index////////////////////////////////////////////////////////////////

    public boolean createIndex(EsSchema schema) {
        return createIndex(schema.getIndexes());
    }

    public CreateIndexRequestBuilder[] prepareCreateIndex(EsSchema schema) {
        String[] indexes = schema.getIndexes();
        CreateIndexRequestBuilder[] result = new CreateIndexRequestBuilder[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            result[i] = prepareCreateIndex(indexes[i]);
        }
        return result;
    }

    public boolean createIndex(String... indexes) {
        boolean result = true;
        for (String index : indexes) {
            result = result && createIndex(index);
        }
        return result;
    }

    public boolean createIndex(String index) {
        return indicesExists(index) || createIndexNoCheck(index);
    }

    private boolean createIndexNoCheck(String index) {
        return prepareCreateIndex(index).execute().actionGet().isAcknowledged();
    }

    public CreateIndexRequestBuilder prepareCreateIndex(String index) {
        return client.prepareCreate(indexToLower(index));
    }

////Delete Index////////////////////////////////////////////////////////////////

    public boolean deleteIndex(EsSchema schema) {
        return deleteIndex(schema.getIndexes());
    }

    public DeleteIndexRequestBuilder prepareDeleteIndex(EsSchema schema) {
        return prepareDeleteIndex(schema.getIndexes());
    }

    public boolean deleteIndex(String... indexes) {
        boolean result = true;
        for (String index : indexes) {
            result = result && deleteIndex(index);
        }
        return result;
    }

    public boolean deleteIndex(String index) {
        return !indicesExists(index) || deleteIndexNoCheck(index);
    }

    private boolean deleteIndexNoCheck(String index) {
        return prepareDeleteIndex(index).execute().actionGet().isAcknowledged();
    }

    public DeleteIndexRequestBuilder prepareDeleteIndex(String... indexes) {
        return client.prepareDelete(indexToLower(indexes));
    }

////Types Exists////////////////////////////////////////////////////////////////

    public boolean typeExists(EsSchema schema) {
        return typeExists(schema.getIndexes(), schema.getType());
    }

    public TypesExistsRequestBuilder prepareTypeExists(EsSchema schema) {
        return prepareTypeExists(schema.getIndexes(), schema.getType());
    }

    public boolean typeExists(String[] indexes, String type) {
        return indicesExists(indexes) && typeExistsNoCheck(indexes, type);
    }

    public boolean typeExists(String index, String type) {
        return indicesExists(index) && typeExistsNoCheck(index, type);
    }

    private boolean typeExistsNoCheck(String[] indexes, String... types) {
        return prepareTypeExists(indexes, types).execute().actionGet().isExists();
    }

    private boolean typeExistsNoCheck(String index, String... types) {
        return prepareTypeExists(new String[]{index}, types).execute().actionGet().isExists();
    }

    public TypesExistsRequestBuilder prepareTypeExists(String[] indexes, String... types) {
        return client.prepareTypesExists(indexToLower(indexes)).setTypes(types);
    }

////Put Mapping/////////////////////////////////////////////////////////////////

    public boolean putMapping(EsSchema schema) {
        return putMapping(schema.getIndexes(), schema.getType(), schema.getSource());
    }

    public PutMappingRequestBuilder preparePutMapping(EsSchema schema) {
        return preparePutMapping(schema.getIndexes(), schema.getType(), schema.getSource());
    }

    public boolean putMapping(String[] indexes, String type, String source) {
        boolean result = true;
        for (String index : indexes) {
            result = result && putMapping(index, type, source);
        }
        return result;
    }

    public boolean putMapping(String index, String type, String source) {
        return createIndex(index) && (typeExistsNoCheck(index, type)
                || putMappingNoCheck(index, type, source));
    }

    private boolean putMappingNoCheck(String index, String type, String source) {
        return preparePutMapping(new String[]{index}, type, source)
                .execute().actionGet().isAcknowledged();
    }

    public PutMappingRequestBuilder preparePutMapping(String[] indexes, String type, String source) {
        return client.preparePutMapping(indexToLower(indexes)).setType(type).setSource(source);
    }

////Delete Mapping//////////////////////////////////////////////////////////////

    public boolean deleteMapping(EsSchema schema) {
        return deleteMapping(schema.getIndexes(), schema.getType());
    }

    public DeleteMappingRequestBuilder prepareDeleteMapping(EsSchema schema) {
        return prepareDeleteMapping(schema.getIndexes(), schema.getType());
    }

    public boolean deleteMapping(String[] indexes, String type) {
        boolean result = true;
        for (String index : indexes) {
            result = result && deleteMapping(index, type);
        }
        return result;
    }

    public boolean deleteMapping(String index, String type) {
        return !indicesExists(index) || !typeExistsNoCheck(index, type)
                || deleteMappingNoCheck(index, type);
    }

    private boolean deleteMappingNoCheck(String index, String type) {
        return prepareDeleteMapping(new String[]{index}, type)
                .execute().actionGet().isAcknowledged();
    }

    public DeleteMappingRequestBuilder prepareDeleteMapping(String[] indexes, String... types) {
        return client.prepareDeleteMapping(indexToLower(indexes)).setType(types);
    }

////Get Mapping/////////////////////////////////////////////////////////////////

    public Map<String, Map<String, Object>> getMapping(EsSchema schema) {
        return getMapping(schema.getIndexes(), schema.getType());
    }

    public GetMappingsRequestBuilder prepareGetMapping(EsSchema schema) {
        return prepareGetMapping(schema.getIndexes(), schema.getType());
    }

    public Map<String, Map<String, Object>> getMapping(String[] indexes, String type) {
        Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        for (String index : indexes) {
            result.put(index, getMapping(index, type));
        }
        return result;
    }

    public Map<String, Object> getMapping(String index, String type) {
        if (!indicesExists(index)) return null;
        if (!typeExistsNoCheck(index, type)) return null;
        return getMappingNoCheck(index, type);
    }

    private Map<String, Object> getMappingNoCheck(String index, String type) {
        try {
            return prepareGetMapping(new String[]{index}, type)
                    .execute().actionGet().getMappings()
                    .get(indexToLower(index)).get(type).sourceAsMap();
        } catch (IOException e) {
            throw new EsaoRuntimeException("GetMapping for index:"
                    + index + " type:" + type + " Exception!", e);
        }
    }

    public GetMappingsRequestBuilder prepareGetMapping(String[] indexes, String... types) {
        return client.prepareGetMappings(indexToLower(indexes)).setTypes(types);
    }

////Private Utils///////////////////////////////////////////////////////////////

    private String[] indexToLower(String... indexes) {
        String[] result = new String[indexes.length];
        for (int i = 0; i < indexes.length; i++) {
            result[i] = indexes[i].toLowerCase();
        }
        return result;
    }

    private String indexToLower(String index) {
        return index.toLowerCase();
    }

}
