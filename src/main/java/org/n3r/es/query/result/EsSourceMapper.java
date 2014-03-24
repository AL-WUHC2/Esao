package org.n3r.es.query.result;

import java.util.Map;

public interface EsSourceMapper {

    Object mapSource(Map<String, Object> source);

}
