package org.n3r.es.schema.builder;

import org.n3r.es.annotation.EsIndexName;

public class EsIndexNameBuilder {

    private String indexName;

    public EsIndexNameBuilder(Class<?> clazz) {
        indexName = clazz.isAnnotationPresent(EsIndexName.class)
                ? clazz.getAnnotation(EsIndexName.class).value()
                        : clazz.getSimpleName();
    }

    public String indexName() {
        return indexName;
    }

}
