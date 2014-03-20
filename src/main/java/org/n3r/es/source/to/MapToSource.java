package org.n3r.es.source.to;

import static com.google.common.collect.Maps.newHashMap;
import static org.n3r.core.joor.Reflect.on;
import static org.n3r.es.source.utils.ConverterScanner.getBindToSource;

import java.util.Map;
import java.util.Set;

import org.n3r.es.source.ToSourceConverter;
import org.n3r.es.source.anno.EsBindTo;
import org.n3r.es.source.base.MapSourceConverter;

@EsBindTo(Map.class)
public class MapToSource extends MapSourceConverter implements ToSourceConverter {

    @Override
    public Map<Object, Object> toSource(Object obj) {
        Map<Object, Object> source = newHashMap();

        Class<? extends ToSourceConverter> kConverterClz = toSourceConverter("kType");
        Class<? extends ToSourceConverter> vConverterClz = toSourceConverter("vType");
        Set<Map.Entry> entrySet = ((Map) obj).entrySet();
        for (Map.Entry entry : entrySet) {
            ToSourceConverter kConverter = on(kConverterClz).create().get();
            ToSourceConverter vConverter = on(vConverterClz).create().get();
            source.put(kConverter.toSource(entry.getKey()),
                    vConverter.toSource(entry.getValue()));
        }
        return source;
    }

    protected Class<? extends ToSourceConverter> toSourceConverter(String optionKey) {
        ToSourceConverter toSource = getBindToSource((Class<?>) getOption(optionKey));
        return toSource == null ? BeanToSource.class : toSource.getClass();
    }

}
