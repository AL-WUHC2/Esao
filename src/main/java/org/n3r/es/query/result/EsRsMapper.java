package org.n3r.es.query.result;

import org.elasticsearch.search.SearchHit;

public interface EsRsMapper {

    Object mapSearchHit(SearchHit hit);

}
