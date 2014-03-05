package org.n3r.es.schema.builder;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.n3r.core.collection.RMap;
import org.n3r.core.lang.RField;
import org.n3r.es.annotation.EsTransient;

public class EsClassPropsBuilder {

    private Map<String, Object> classProps = new HashMap<String, Object>();

    public EsClassPropsBuilder(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(EsTransient.class)) continue;
            if (RField.isNotNormal(fields[i])) continue;

            classProps.put(fields[i].getName(), new EsFieldSettingBuilder(fields[i]).setting());
        }
    }

    public Map<String, Object> props() {
        return RMap.isEmpty(classProps) ? null : classProps;
    }

}
