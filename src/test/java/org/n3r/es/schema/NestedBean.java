package org.n3r.es.schema;

import org.n3r.es.schema.anno.EsMapIndex;
import org.n3r.es.schema.anno.EsMapIndexName;
import org.n3r.es.schema.anno.EsMapTypeName;
import org.n3r.es.schema.enums.EsIndexType;

@EsMapIndexName("Nested")
@EsMapTypeName("NestedBean")
public class NestedBean {

    private SimpleBean simple;

    private SimpleAnnoBean simpleAnno;

    @EsMapIndex(EsIndexType.NO)
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
