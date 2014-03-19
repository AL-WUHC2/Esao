package org.n3r.es.schema;

public class EsSchema {

    private String index;

    private String type;

    private String source;

    private String idFieldPath;

    public EsSchema() {}

    public EsSchema(String index, String type, String source, String idFieldPath) {
        this.index = index.toLowerCase();
        this.type = type;
        this.source = source;
        this.idFieldPath = idFieldPath;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index.toLowerCase();
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

    public String getIdFieldPath() {
        return idFieldPath;
    }

    public void setIdFieldPath(String idFieldPath) {
        this.idFieldPath = idFieldPath;
    }

}
