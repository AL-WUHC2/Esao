package org.n3r.es.query;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.n3r.es.cache.EsMapperCache;
import org.n3r.es.query.result.EsSourceMapper;

public class EsSourceConverter {

    private int rsSize = EsQueryHelper.DEFAULT_SIZE;

    private Class<?> returnType;

    private EsSourceMapper mapper;

    public Object convert(SearchHits searchHits) {
        mapper = EsMapperCache.get(returnType);
        return rsSize <= 1 || searchHits.totalHits() <= 1
                ? convertSingle(searchHits) : convertList(searchHits);
    }

    private Object convertSingle(SearchHits searchHits) {
        if (searchHits.totalHits() == 0) return null;
        return mapper.mapSource(searchHits.getAt(0).getSource());
    }

    private Object convertList(SearchHits searchHits) {
        List<Object> result = new ArrayList<Object>();

        SearchHit[] hits = searchHits.getHits();
        for (int i = 0; i < hits.length; i++) {
            Object hitObject = mapper.mapSource(hits[i].getSource());
            if (hitObject != null) result.add(hitObject);
        }

        return result;
    }

    public void setRsSize(int rsSize) {
        this.rsSize = rsSize;
    }

    public void resetRsSize() {
        this.rsSize = EsQueryHelper.DEFAULT_SIZE;
    }

    public void setReturnType(Class<?> returnType) {
        this.returnType = returnType;
    }

}
