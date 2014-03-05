package org.n3r.es.enums;

public enum EsIndexType {

    NO("no"),
    ANALYZED("analyzed"),
    NOT_ANALYZED("not_analyzed");

    private String describe;

    EsIndexType(String describe) {
        this.describe = describe;
    }

    public String describe() {
        return describe;
    }

}
