package org.n3r.es.schema.builder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.core.lang.RType;
import org.n3r.es.annotation.EsDateFormat;
import org.n3r.es.annotation.EsField;
import org.n3r.es.annotation.EsIndex;
import org.n3r.es.annotation.EsIndexAlias;
import org.n3r.es.annotation.EsStore;
import org.n3r.es.enums.EsFieldType;
import org.n3r.es.exception.EsaoRuntimeException;

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

    public EsFieldSettingBuilder buildFieldIndexAlias() {
        if (field.isAnnotationPresent(EsIndexAlias.class))
            fieldSetting.put("index_name", field.getAnnotation(EsIndexAlias.class).value());
        return this;
    }

    public EsFieldSettingBuilder buildFieldIndex() {
        if (field.isAnnotationPresent(EsIndex.class))
            fieldSetting.put("index", field.getAnnotation(EsIndex.class).value().describe());
        return this;
    }

    public EsFieldSettingBuilder buildFieldStore() {
        if (field.isAnnotationPresent(EsStore.class))
            fieldSetting.put("store", field.getAnnotation(EsStore.class).value());
        return this;
    }

    public EsFieldSettingBuilder buildFieldType() {
        // EsDateFormat Annotation -> type: date
        if (field.isAnnotationPresent(EsDateFormat.class)) {
            fieldSetting.put("format", field.getAnnotation(EsDateFormat.class).value());
            fieldSetting.put("type", EsFieldType.DATE.describe());
            return this;
        }

        if (field.isAnnotationPresent(EsField.class)) {
            fieldSetting.put("type", field.getAnnotation(EsField.class).value().describe());
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
