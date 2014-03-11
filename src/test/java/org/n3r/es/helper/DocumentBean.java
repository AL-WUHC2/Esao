package org.n3r.es.helper;

import org.n3r.es.annotation.EsIdField;
import org.n3r.es.annotation.EsIndexName;
import org.n3r.es.annotation.EsTypeName;

@EsIndexName("document")
@EsTypeName("docType")
public class DocumentBean {

    @EsIdField
    private String docId;

    private String docContent;

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getDocContent() {
        return docContent;
    }

    public void setDocContent(String docContent) {
        this.docContent = docContent;
    }

    @Override
    public String toString() {
        return "{docId=" + docId + ", docContent=" + docContent + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        DocumentBean b = (DocumentBean) obj;
        if (b.getDocId() == null && docId == null
                && b.getDocContent() == null
                && docContent == null) return true;
        if (b.getDocId() == null || docId == null
                || b.getDocContent() == null
                || docContent == null) return false;
        else return b.getDocId().equals(docId)
                && b.getDocContent().equals(docContent);
    }

}
