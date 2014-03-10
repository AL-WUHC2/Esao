package org.n3r.es.schema;

public class EsSchema {

    private String index;

    private String type;

    private String source;

    public EsSchema() {}

    public EsSchema(String index, String type, String source) {
        this.index = index;
        this.type = type;
        this.source = source;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

}
