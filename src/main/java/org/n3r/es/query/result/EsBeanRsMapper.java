package org.n3r.es.query.result;

import org.elasticsearch.search.SearchHit;
import org.n3r.es.source.from.BeanFromSource;

public class EsBeanRsMapper implements EsRsMapper {

    protected Class<?> mappedClass;

    public EsBeanRsMapper(Class<?> returnType) {
        this.mappedClass = returnType;
    }

    @Override
    public Object mapSearchHit(SearchHit hit) {
        return new BeanFromSource().fromSource(
                hit.getSource(), mappedClass);
    }

}
