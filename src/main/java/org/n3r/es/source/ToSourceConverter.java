package org.n3r.es.source;

public interface ToSourceConverter extends SourceConverter {

    <T> T toSource(Object obj);

}
