package org.n3r.es.source.from;

import static org.n3r.core.joor.Reflect.on;
import static org.n3r.es.source.utils.ConverterScanner.getBindFromSource;

import java.lang.reflect.Field;
import java.util.Map;

import org.n3r.core.lang.RField;
import org.n3r.es.schema.anno.EsMapTransient;
import org.n3r.es.source.FromSourceConverter;
import org.n3r.es.source.base.BaseSourceConverter;

public class BeanFromSource extends BaseSourceConverter implements FromSourceConverter {

    @Override
    public <T> T fromSource(Object source, Class<T> clazz) {
        if (source == null) return null;

        FromSourceConverter fromSource = getBindFromSource(clazz);
        if (fromSource != null) return fromSource.fromSource(source, clazz);

        T obj = on(clazz).create().get();
        Map sourceMap = (Map) source;

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(EsMapTransient.class)) continue;
            if (RField.isNotNormal(fields[i])) continue;

            String fieldName = fields[i].getName();
            if (!sourceMap.containsKey(fieldName)) continue;
            Object fieldSource = sourceMap.get(fieldName);
            if (fieldSource == null) continue;

            Class<?> fieldType = fields[i].getType();
            fromSource = getBindFromSource(fieldType);
            if (fromSource == null) fromSource = new BeanFromSource();
            fromSource.parseOptions(fields[i]);

            Object fieldValue = fromSource.fromSource(fieldSource, fieldType);
            on(obj).set(fieldName, fieldValue);
        }
        return obj;
    }

}
