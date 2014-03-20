package org.n3r.es.source.to;

import static com.google.common.collect.Maps.newHashMap;
import static org.n3r.core.joor.Reflect.on;
import static org.n3r.es.source.utils.ConverterScanner.getBindToSource;

import java.lang.reflect.Field;
import java.util.Map;

import org.n3r.core.lang.RField;
import org.n3r.es.schema.anno.EsMapTransient;
import org.n3r.es.source.ToSourceConverter;
import org.n3r.es.source.base.BaseSourceConverter;

public class BeanToSource extends BaseSourceConverter implements ToSourceConverter {

    @Override
    public <T> T toSource(Object obj) {
        if (obj == null) return null;
        Map<String, Object> source = newHashMap();

        Class<?> clazz = obj.getClass();
        ToSourceConverter toSource = getBindToSource(clazz);
        if (toSource != null) return toSource.toSource(obj);

        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(EsMapTransient.class)) continue;
            if (RField.isNotNormal(fields[i])) continue;

            Object fieldValue = on(obj).get(fields[i]);
            if (fieldValue == null) continue;

            toSource = getBindToSource(fields[i].getType());
            if (toSource == null) toSource = new BeanToSource();
            toSource.parseOptions(fields[i]);

            Object fieldSource = toSource.toSource(fieldValue);
            if (fieldSource == null) continue;
            source.put(fields[i].getName(), fieldSource);
        }
        return (T) source;
    }

}
