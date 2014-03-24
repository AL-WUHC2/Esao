package org.n3r.es.schema;

import java.util.Date;

import org.n3r.core.lang.RBaseBean;
import org.n3r.es.schema.anno.EsMapDateFormat;
import org.n3r.es.schema.anno.EsMapFieldType;
import org.n3r.es.schema.anno.EsMapId;
import org.n3r.es.schema.anno.EsMapIdSetting;
import org.n3r.es.schema.anno.EsMapIndex;
import org.n3r.es.schema.anno.EsMapIndexName;
import org.n3r.es.schema.anno.EsMapStore;
import org.n3r.es.schema.anno.EsMapTransient;
import org.n3r.es.schema.anno.EsMapTypeName;
import org.n3r.es.schema.enums.EsFieldType;
import org.n3r.es.schema.enums.EsIndexType;

@EsMapIndexName("simple")
@EsMapTypeName("simpleType")
@EsMapIdSetting(index = EsIndexType.NOT_ANALYZED, store = true)
public class SimpleAnnoBean extends RBaseBean {

    @EsMapId
    private int simpleInteger;

    @EsMapIndex(EsIndexType.NO)
    private String simpleString;

    @EsMapFieldType(EsFieldType.STRING)
    @EsMapStore(true)
    private Date originalDate;

    @EsMapDateFormat("yyyy:MM:dd")
    private String stringDate;

    @EsMapTransient
    private String transientField;

    public int getSimpleInteger() {
        return simpleInteger;
    }

    public void setSimpleInteger(int simpleInteger) {
        this.simpleInteger = simpleInteger;
    }

    public String getSimpleString() {
        return simpleString;
    }

    public void setSimpleString(String simpleString) {
        this.simpleString = simpleString;
    }

    public Date getOriginalDate() {
        return originalDate;
    }

    public void setOriginalDate(Date originalDate) {
        this.originalDate = originalDate;
    }

    public String getStringDate() {
        return stringDate;
    }

    public void setStringDate(String stringDate) {
        this.stringDate = stringDate;
    }

    public String getTransientField() {
        return transientField;
    }

    public void setTransientField(String transientField) {
        this.transientField = transientField;
    }

}
