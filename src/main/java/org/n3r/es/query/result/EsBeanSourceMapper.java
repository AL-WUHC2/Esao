package org.n3r.es.query.result;

import java.util.Map;

import org.n3r.es.source.from.BeanFromSource;

public class EsBeanSourceMapper implements EsSourceMapper {

    protected Class<?> mappedClass;

    public EsBeanSourceMapper(Class<?> returnType) {
        this.mappedClass = returnType;
    }

    @Override
    public Object mapSource(Map<String, Object> source) {
        return new BeanFromSource().fromSource(source, mappedClass);
    }

}
