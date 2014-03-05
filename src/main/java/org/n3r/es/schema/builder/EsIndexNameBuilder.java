package org.n3r.es.schema.builder;

import org.n3r.es.annotation.EsIndexName;

public class EsIndexNameBuilder {

    private String[] indexNames;

    public EsIndexNameBuilder(Class<?> clazz) {
        indexNames = clazz.isAnnotationPresent(EsIndexName.class)
                ? clazz.getAnnotation(EsIndexName.class).value()
                        : new String[] { clazz.getSimpleName() };
    }

    public String[] indexNames() {
        return indexNames;
    }

}
