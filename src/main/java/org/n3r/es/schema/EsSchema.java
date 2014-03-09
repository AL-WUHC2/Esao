package org.n3r.es.schema;

public class EsSchema {

    private String[] indexes;

    private String type;

    private String source;

    public EsSchema() {}

    public EsSchema(String index, String type, String source) {
        this(new String[] { index }, type, source);
    }

    public EsSchema(String[] indexes, String type, String source) {
        this.indexes = indexes;
        this.type = type;
        this.source = source;
    }

    public String[] getIndexes() {
        return indexes;
    }

    public void setIndexes(String... indexes) {
        this.indexes = indexes;
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
