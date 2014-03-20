package org.n3r.es.source;

public interface FromSourceConverter extends SourceConverter {

    <T> T fromSource(Object source, Class<T> clazz);

}
