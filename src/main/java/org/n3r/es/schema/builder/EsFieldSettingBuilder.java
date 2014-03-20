package org.n3r.es.schema.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.core.lang.RType;
import org.n3r.es.exception.EsaoRuntimeException;
import org.n3r.es.schema.anno.EsMapDateFormat;
import org.n3r.es.schema.anno.EsMapFieldType;
import org.n3r.es.schema.anno.EsMapIndex;
import org.n3r.es.schema.anno.EsMapStore;
import org.n3r.es.schema.enums.EsFieldType;

public class EsFieldSettingBuilder {

    private Field field;

    private Map<String, Object> fieldSetting = new HashMap<String, Object>();

    public EsFieldSettingBuilder(Field field) {
        this.field = field;
        Method[] methods = this.getClass().getMethods();
        try {
            for (Method m : methods) {
                if (isBuildMethod(m)) m.invoke(this);
            }
        } catch (Exception e) {
            throw new EsaoRuntimeException(field.getName() + " field build failed!", e);
        }
    }

    public Map<String, Object> setting() {
        return fieldSetting;
    }

    public EsFieldSettingBuilder buildFieldIndex() {
        if (field.isAnnotationPresent(EsMapIndex.class))
            fieldSetting.put("index", field.getAnnotation(EsMapIndex.class).value().describe());
        return this;
    }

    public EsFieldSettingBuilder buildFieldStore() {
        if (field.isAnnotationPresent(EsMapStore.class))
            fieldSetting.put("store", field.getAnnotation(EsMapStore.class).value());
        return this;
    }

    public EsFieldSettingBuilder buildFieldType() {
        // EsDateFormat Annotation -> type: date
        if (field.isAnnotationPresent(EsMapDateFormat.class)) {
            fieldSetting.put("format", field.getAnnotation(EsMapDateFormat.class).value());
            fieldSetting.put("type", EsFieldType.DATE.describe());
            return this;
        }

        if (field.isAnnotationPresent(EsMapFieldType.class)) {
            fieldSetting.put("type", field.getAnnotation(EsMapFieldType.class).value().describe());
        } else {
            Class<?> fieldType = fieldActualType(field);
            String typeDesc = EsFieldType.typeDescOfClass(fieldType);
            fieldSetting.put("type", typeDesc);
            if ("object".equals(typeDesc))
                fieldSetting.put("properties", new EsClassPropsBuilder(fieldType).props());
        }
        return this;
    }

    private boolean isBuildMethod(Method m) {
        return m.getName().startsWith("buildField")
                && Modifier.isPublic(m.getModifiers())
                && !Modifier.isAbstract(m.getModifiers())
                && m.getReturnType().equals(this.getClass())
                && !m.isVarArgs();
    }

    private Class<?> fieldActualType(Field field) {
        Class<?> fieldType = field.getType();
        if (fieldType.isArray()) fieldType = fieldType.getComponentType();
        if (fieldType.isAssignableFrom(List.class)) {
            fieldType = RType.getActualTypeArgument(field.getGenericType());
            if (Void.class.equals(fieldType))
                throw new EsaoRuntimeException(field.getName() + " field generic type undefined!");
        }
        return fieldType;
    }

}
