package org.n3r.es.source.base;

import static com.google.common.collect.Maps.newHashMap;

import java.lang.reflect.Field;
import java.util.Map;

import org.n3r.es.source.SourceConverter;

public class BaseSourceConverter implements SourceConverter {

    protected Map<String, Object> options = newHashMap();

    @Override
    public void addOptions(Map<String, Object> options) {
        this.options.putAll(options);
    }

    @Override
    public void addOption(String name, Object option) {
        this.options.put(name, option);
    }

    @Override
    public void parseOptions(Field field) {
    }

    public Object getOption(String optionName) {
        return options.get(optionName);
    }

    public Object getOption(String optionName, Object defaultValue) {
        Object optionValue = getOption(optionName);
        return optionValue != null ? optionValue : defaultValue;
    }

}
