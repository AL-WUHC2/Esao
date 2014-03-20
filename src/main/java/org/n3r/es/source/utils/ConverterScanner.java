package org.n3r.es.source.utils;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.ClassUtils.isAssignable;
import static org.apache.commons.lang3.ClassUtils.isPrimitiveOrWrapper;
import static org.apache.commons.lang3.ClassUtils.primitiveToWrapper;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.n3r.core.joor.Reflect;
import org.n3r.core.lang.RClassPath;
import org.n3r.es.source.FromSourceConverter;
import org.n3r.es.source.ToSourceConverter;
import org.n3r.es.source.anno.EsBindTo;
import org.n3r.es.source.from.BaseFromSource;
import org.n3r.es.source.to.BaseToSource;

public class ConverterScanner {

    private static Map<Class<?>, Class<?>> toSourceMap = newHashMap();

    private static Map<Class<?>, Class<?>> fromSourceMap = newHashMap();

    static {
        for (Class<?> clz : RClassPath.getAnnotatedClasses("org.n3r.es.source", EsBindTo.class)) {
            Class<?> bindClasses = clz.getAnnotation(EsBindTo.class).value();

            if (isAssignable(clz, ToSourceConverter.class)) toSourceMap.put(bindClasses, clz);
            if (isAssignable(clz, FromSourceConverter.class)) fromSourceMap.put(bindClasses, clz);
        }
    }

    public static ToSourceConverter getBindToSource(Class<?> clazz) {
        if (isSimpleType(clazz)) return new BaseToSource();
        return getBindClass(toSourceMap, clazz);
    }

    public static FromSourceConverter getBindFromSource(Class<?> clazz) {
        if (isSimpleType(clazz)) return new BaseFromSource();
        return getBindClass(fromSourceMap, clazz);
    }

    public static <T> T getBindClass(Map<Class<?>, Class<?>> map, Class<?> target) {
        Class<?> clz = map.get(target);
        if (clz == null && target.isPrimitive())
            clz = map.get(primitiveToWrapper(target));
        if (clz == null) {
            Set<Entry<Class<?>,Class<?>>> entrySet = map.entrySet();
            for (Entry<Class<?>, Class<?>> entry : entrySet) {
                if (!isAssignable(target, entry.getKey())) continue;
                clz = entry.getValue();
                break;
            }
        }
        return clz == null ? null : Reflect.on(clz).create().<T>get();
    }

    private static boolean isSimpleType(Class<?> clazz) {
        return isPrimitiveOrWrapper(clazz) || clazz.equals(String.class);
    }

}
