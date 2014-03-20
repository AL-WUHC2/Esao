package org.n3r.es.source.from;

import static com.google.common.collect.Maps.newHashMap;
import static org.n3r.core.joor.Reflect.on;
import static org.n3r.es.source.utils.ConverterScanner.getBindFromSource;

import java.util.Map;
import java.util.Set;

import org.n3r.es.source.FromSourceConverter;
import org.n3r.es.source.anno.EsBindTo;
import org.n3r.es.source.base.MapSourceConverter;

@EsBindTo(Map.class)
public class MapFromSource extends MapSourceConverter implements FromSourceConverter {

    @Override
    public <T> T fromSource(Object source, Class<T> clazz) {
        Map<Object, Object> result = newHashMap();

        Class<?> kType = (Class<?>) getOption("kType");
        Class<?> vType = (Class<?>) getOption("vType");
        Class<? extends FromSourceConverter> kConverterClz = fromSourceConverter("kType");
        Class<? extends FromSourceConverter> vConverterClz = fromSourceConverter("vType");
        Set<Map.Entry> entrySet = ((Map) source).entrySet();
        for (Map.Entry entry : entrySet) {
            FromSourceConverter kConverter = on(kConverterClz).create().get();
            FromSourceConverter vConverter = on(vConverterClz).create().get();
            result.put(kConverter.fromSource(entry.getKey(), kType),
                    vConverter.fromSource(entry.getValue(), vType));
        }

        return (T) result;
    }

    protected Class<? extends FromSourceConverter> fromSourceConverter(String optionKey) {
        FromSourceConverter fromSource = getBindFromSource((Class<?>) getOption(optionKey));
        return fromSource == null ? BeanFromSource.class : fromSource.getClass();
    }

}
