package org.n3r.es.schema.builder;

import org.n3r.es.schema.anno.EsMapTypeName;

public class EsMapTypeNameBuilder {

    private String typeName;

    public EsMapTypeNameBuilder(Class<?> clazz) {
        typeName = clazz.isAnnotationPresent(EsMapTypeName.class)
                ? clazz.getAnnotation(EsMapTypeName.class).value()
                        : clazz.getCanonicalName();
    }

    public String typeName() {
        return typeName;
    }

}
