package org.n3r.es.schema;

public class EsSchema {

    private String[] indexs;

    private String type;

    private String source;

    public EsSchema() {}

    public EsSchema(String index, String type, String source) {
        this(new String[] { index }, type, source);
    }

    public EsSchema(String[] indexs, String type, String source) {
        this.indexs = indexs;
        this.type = type;
        this.source = source;
    }

    public String[] getIndexs() {
        return indexs;
    }

    public void setIndexs(String[] indexs) {
        this.indexs = indexs;
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
