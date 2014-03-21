package org.n3r.es.source.from;

import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.apache.commons.lang3.ClassUtils.isPrimitiveWrapper;
import static org.apache.commons.lang3.ClassUtils.primitiveToWrapper;
import static org.n3r.core.lang.RStr.toStr;

import org.n3r.core.joor.Reflect;
import org.n3r.es.source.FromSourceConverter;
import org.n3r.es.source.base.BaseSourceConverter;

public class BaseFromSource extends BaseSourceConverter implements FromSourceConverter {

    @Override
    public <T> T fromSource(Object source, Class<T> clazz) {
        Object obj = source;
        if (String.class.equals(clazz)) {
            obj = toStr(source, "");
        } else if (isPrimitiveOrWrapper(clazz)) {
            Class<?> wClz = isPrimitiveWrapper(clazz) ?
                    clazz : primitiveToWrapper(clazz);
            return Reflect.on(wClz).call("valueOf", toStr(source)).get();
        }
        return (T) obj;
    }

}
