package org.n3r.es.source.to;

import org.n3r.es.source.ToSourceConverter;
import org.n3r.es.source.base.BaseSourceConverter;

public class BaseToSource extends BaseSourceConverter implements ToSourceConverter {

    @Override
    public <T> T toSource(Object obj) {
        return (T) obj;
    }

}
