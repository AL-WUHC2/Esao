package org.n3r.es.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.n3r.es.enums.EsFieldType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface EsField {

    EsFieldType value();

}
