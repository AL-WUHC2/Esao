package org.n3r.es.query.result;

import java.util.Map;

public class EsMapSourceMapper implements EsSourceMapper {

    @Override
    public Object mapSource(Map<String, Object> source) {
        return source;
    }

}
