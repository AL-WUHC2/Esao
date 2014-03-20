package org.n3r.es.source.to;

import static org.n3r.core.joor.Reflect.on;
import static org.n3r.es.source.utils.ConverterScanner.getBindToSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.n3r.es.source.ToSourceConverter;
import org.n3r.es.source.anno.EsBindTo;
import org.n3r.es.source.base.ListSourceConverter;

@EsBindTo(List.class)
public class ListToSource extends ListSourceConverter implements ToSourceConverter {

    @Override
    public List<Object> toSource(Object obj) {
        List<Object> source = new ArrayList<Object>();

        Class<? extends ToSourceConverter> converterClz = itemToSourceConverter();
        Iterator iterator = ((List) obj).iterator();
        while (iterator.hasNext()) {
            ToSourceConverter itemConverter = on(converterClz).create().get();
            source.add(itemConverter.toSource(iterator.next()));
        }
        return source;
    }

    protected Class<? extends ToSourceConverter> itemToSourceConverter() {
        ToSourceConverter toSource = getBindToSource((Class<?>) getOption("itemType"));
        return toSource == null ? BeanToSource.class : toSource.getClass();
    }

}
