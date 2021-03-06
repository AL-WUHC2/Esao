package org.n3r.es.schema.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.n3r.es.schema.enums.EsIndexType;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EsMapIdSetting {

    EsIndexType index() default EsIndexType.NO;

    boolean store() default false;

}
