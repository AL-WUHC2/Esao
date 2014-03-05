package org.n3r.es.schema.builder;

import org.n3r.es.annotation.EsTypeName;

public class EsTypeNameBuilder {

    private String typeName;

    public EsTypeNameBuilder(Class<?> clazz) {
        typeName = clazz.isAnnotationPresent(EsTypeName.class)
                ? clazz.getAnnotation(EsTypeName.class).value()
                        : clazz.getCanonicalName();
    }

    public String typeName() {
        return typeName;
    }

}
