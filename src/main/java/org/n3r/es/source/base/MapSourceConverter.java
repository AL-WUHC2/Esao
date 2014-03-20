package org.n3r.es.source.base;

import static org.n3r.core.lang.RType.getActualTypeArguments;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.n3r.es.exception.EsaoRuntimeException;

public class MapSourceConverter extends BaseSourceConverter {

    @Override
    public void parseOptions(Field field) {
        super.parseOptions(field);
        Type[] types = getActualTypeArguments(field.getGenericType());
        if (types.length == 2 && (Class<?>) types[0] != Void.class
                && (Class<?>) types[1] != Void.class) {
            addOption("kType", types[0]);
            addOption("vType", types[1]);
            return;
        }
        throw new EsaoRuntimeException("Unkown List Item Class for field " + field.getName());
    }

}
