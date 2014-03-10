package org.n3r.es.helper;

import static org.elasticsearch.client.Requests.createIndexRequest;
import static org.elasticsearch.client.Requests.deleteIndexRequest;
import static org.elasticsearch.client.Requests.deleteMappingRequest;
import static org.elasticsearch.client.Requests.indicesExistsRequest;
import static org.elasticsearch.client.Requests.putMappingRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.admin.indices.exists.types.TypesExistsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
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

    /**
     * return true if all of the indexes were exists
     */
    public boolean indicesExists(String... indexes) {
        return client.exists(indicesExistsRequest(indexToLower(indexes)))
                .actionGet().isExists();
    }

    public boolean indicesExists(String index) {
        return client.exists(indicesExistsRequest(indexToLower(index)))
                .actionGet().isExists();
    }

////Create Index////////////////////////////////////////////////////////////////

    public boolean createIndex(EsSchema schema) {
        return createIndex(schema.getIndexes());
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
        return client.create(createIndexRequest(indexToLower(index)))
                .actionGet().isAcknowledged();
    }

////Delete Index////////////////////////////////////////////////////////////////

    public boolean deleteIndex(EsSchema schema) {
        return deleteIndex(schema.getIndexes());
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
        return client.delete(deleteIndexRequest(indexToLower(index)))
                .actionGet().isAcknowledged();
    }

////Types Exists////////////////////////////////////////////////////////////////

    public boolean typeExists(EsSchema schema) {
        return typeExists(schema.getIndexes(), schema.getType());
    }

    public boolean typeExists(String[] indexes, String type) {
        return indicesExists(indexes) && typeExistsNoCheck(indexes, type);
    }

    public boolean typeExists(String index, String type) {
        return indicesExists(index) && typeExistsNoCheck(index, type);
    }

    private boolean typeExistsNoCheck(String[] indexes, String type) {
        return client.typesExists(new TypesExistsRequest(indexToLower(indexes),
                type)).actionGet().isExists();
    }

    private boolean typeExistsNoCheck(String index, String type) {
        return client.typesExists(new TypesExistsRequest(new String[]{indexToLower(index)},
                type)).actionGet().isExists();
    }

////Put Mapping/////////////////////////////////////////////////////////////////

    public boolean putMapping(EsSchema schema) {
        return putMapping(schema.getIndexes(), schema.getType(), schema.getSource());
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
        return client.putMapping(putMappingRequest(indexToLower(index))
                .type(type).source(source)).actionGet().isAcknowledged();
    }

////Delete Mapping//////////////////////////////////////////////////////////////

    public boolean deleteMapping(EsSchema schema) {
        return deleteMapping(schema.getIndexes(), schema.getType());
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
        return client.deleteMapping(deleteMappingRequest(indexToLower(index))
                .types(type)).actionGet().isAcknowledged();
    }

////Get Mapping/////////////////////////////////////////////////////////////////

    public Map<String, Map<String, Object>> getMapping(EsSchema schema) {
        return getMapping(schema.getIndexes(), schema.getType());
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
            String indexLower = indexToLower(index);
            return client.getMappings(new GetMappingsRequest()
                    .indices(indexLower).types(type)).actionGet()
                    .getMappings().get(indexLower).get(type).sourceAsMap();
        } catch (IOException e) {
            throw new EsaoRuntimeException("GetMapping for index:"
                    + index + " type:" + type + " Exception!", e);
        }
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
