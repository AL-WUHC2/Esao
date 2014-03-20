package org.n3r.es.source.from;

import org.n3r.es.source.FromSourceConverter;
import org.n3r.es.source.base.BaseSourceConverter;

public class BaseFromSource extends BaseSourceConverter implements FromSourceConverter {

    @Override
    public <T> T fromSource(Object source, Class<T> clazz) {
        return (T) source;
    }

}
