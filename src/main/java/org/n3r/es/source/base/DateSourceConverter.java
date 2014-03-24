package org.n3r.es.source.base;

import java.lang.reflect.Field;

import org.n3r.es.schema.anno.EsMapDateFormat;

public class DateSourceConverter extends BaseSourceConverter {

    @Override
    public void parseOptions(Field field) {
        super.parseOptions(field);
        if (field.isAnnotationPresent(EsMapDateFormat.class)) {
            String dateFormat = field.getAnnotation(EsMapDateFormat.class).value();
            addOption("dateFormat", dateFormat);
        }
    }

}
