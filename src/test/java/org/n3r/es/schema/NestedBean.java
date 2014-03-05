package org.n3r.es.schema;

import org.n3r.es.annotation.EsIndex;
import org.n3r.es.annotation.EsIndexName;
import org.n3r.es.annotation.EsTypeName;
import org.n3r.es.enums.EsIndexType;

@EsIndexName("Nested")
@EsTypeName("NestedBean")
public class NestedBean {

    private SimpleBean simple;

    private SimpleAnnoBean simpleAnno;

    @EsIndex(EsIndexType.NO)
    private String comment;

    public SimpleBean getSimple() {
        return simple;
    }

    public void setSimple(SimpleBean simple) {
        this.simple = simple;
    }

    public SimpleAnnoBean getSimpleAnno() {
        return simpleAnno;
    }

    public void setSimpleAnno(SimpleAnnoBean simpleAnno) {
        this.simpleAnno = simpleAnno;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
