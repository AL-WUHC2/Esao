package org.n3r.es.source.from;

import static org.n3r.core.joor.Reflect.on;
import static org.n3r.es.source.utils.ConverterScanner.getBindFromSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.n3r.es.source.FromSourceConverter;
import org.n3r.es.source.anno.EsBindTo;
import org.n3r.es.source.base.ListSourceConverter;

@EsBindTo(List.class)
public class ListFromSource extends ListSourceConverter implements FromSourceConverter {

    @Override
    public <T> T fromSource(Object source, Class<T> clazz) {
        List<Object> result = new ArrayList<Object>();

        Class<?> itemType = (Class<?>) getOption("itemType");
        Class<? extends FromSourceConverter> converterClz = itemFromSourceConverter();
        Iterator iterator = ((List) source).iterator();
        while (iterator.hasNext()) {
            FromSourceConverter itemConverter = on(converterClz).create().get();
            result.add(itemConverter.fromSource(iterator.next(), itemType));
        }
        return (T) result;
    }

    protected Class<? extends FromSourceConverter> itemFromSourceConverter() {
        FromSourceConverter fromSource = getBindFromSource((Class<?>) getOption("itemType"));
        return fromSource == null ? BeanFromSource.class : fromSource.getClass();
    }

}
