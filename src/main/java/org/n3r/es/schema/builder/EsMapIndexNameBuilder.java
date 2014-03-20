package org.n3r.es.schema.builder;

import org.n3r.es.schema.anno.EsMapIndexName;

public class EsMapIndexNameBuilder {

    private String indexName;

    public EsMapIndexNameBuilder(Class<?> clazz) {
        indexName = clazz.isAnnotationPresent(EsMapIndexName.class)
                ? clazz.getAnnotation(EsMapIndexName.class).value().toLowerCase()
                        : clazz.getSimpleName().toLowerCase();
    }

    public String indexName() {
        return indexName;
    }

}
