package org.n3r.es.enums;

public enum EsFieldType {

    STRING("string"),
    FLOAT("float"),
    DOUBLE("double"),
    INTEGER("integer"),
    INT("integer"),
    LONG("long"),
    SHORT("short"),
    BYTE("byte"),
    BOOLEAN("boolean"),
    DATE("date");

    private String describe;

    EsFieldType(String describe) {
        this.describe = describe;
    }

    public String describe() {
        return describe;
    }

    public static String typeDescOfClass(Class<?> clz) {
        EsFieldType[] types = values();
        for (EsFieldType type : types) {
            if (clz.getSimpleName().equalsIgnoreCase(type.name())) return type.describe();
        }
        return "object";
    }

}
