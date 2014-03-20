package org.n3r.es.source;

import java.lang.reflect.Field;
import java.util.Map;

public interface SourceConverter {

    void addOptions(Map<String, Object> options);

    void addOption(String name, Object option);

    void parseOptions(Field field);

}
