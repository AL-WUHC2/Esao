package org.n3r.es.source.base;

import static org.n3r.core.lang.RType.getActualTypeArgument;

import java.lang.reflect.Field;

import org.n3r.es.exception.EsaoRuntimeException;

public class ListSourceConverter extends BaseSourceConverter {

    @Override
    public void parseOptions(Field field) {
        super.parseOptions(field);
        Class<?> clazz = getActualTypeArgument(field.getGenericType());
        if (clazz != Void.class) {
            addOption("itemType", clazz);
            return;
        }
        throw new EsaoRuntimeException("Unkown List Item Class for field " + field.getName());
    }

}
