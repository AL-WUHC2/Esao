package org.n3r.es.schema.builder;

import static org.n3r.core.collection.RMap.getStr;
import static org.n3r.core.collection.RMap.isEmpty;
import static org.n3r.core.lang.RClass.isBasicType;
import static org.n3r.core.lang.RClass.isCollectionType;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.n3r.core.lang.RField;
import org.n3r.es.schema.anno.EsMapId;
import org.n3r.es.schema.anno.EsMapIdSetting;
import org.n3r.es.schema.anno.EsMapTransient;

public class EsMapIdSettingBuilder {

    private Map<String, Object> idSetting = new HashMap<String, Object>();

    public EsMapIdSettingBuilder(Class<?> clazz) {
        if (clazz.isAnnotationPresent(EsMapIdSetting.class)) {
            EsMapIdSetting setting = clazz.getAnnotation(EsMapIdSetting.class);
            idSetting.put("index", setting.index().describe());
            idSetting.put("store", setting.store());
        }
        String idFieldPath = buildIdFieldPath(clazz);
        if (idFieldPath != null) idSetting.put("path", idFieldPath);
    }

    public Map<String, Object> setting() {
        return isEmpty(idSetting) ? null : idSetting;
    }

    public String idFieldPath() {
        return getStr(idSetting, "path");
    }

    private String buildIdFieldPath(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].isAnnotationPresent(EsMapTransient.class)) continue;
            if (RField.isNotNormal(fields[i])) continue;

            String fieldName = fields[i].getName();
            if (fields[i].isAnnotationPresent(EsMapId.class) ||
                    fieldName.equalsIgnoreCase("id")) return fieldName;

            Class<?> fieldType = fields[i].getType();
            if (!isBasicType(fieldType) && !isCollectionType(fieldType)) {
                String idFieldPath = buildIdFieldPath(fieldType);
                if (idFieldPath != null) return fieldName + "." + idFieldPath;
            }
        }
        return null;
    }

}
