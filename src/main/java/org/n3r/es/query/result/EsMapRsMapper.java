package org.n3r.es.query.result;

import org.elasticsearch.search.SearchHit;

public class EsMapRsMapper implements EsRsMapper {

    @Override
    public Object mapSearchHit(SearchHit hit) {
        return hit.getSource();
    }

}
