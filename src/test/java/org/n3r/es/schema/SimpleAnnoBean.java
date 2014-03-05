package org.n3r.es.schema;

import java.util.Date;

import org.n3r.es.annotation.EsDateFormat;
import org.n3r.es.annotation.EsField;
import org.n3r.es.annotation.EsIdField;
import org.n3r.es.annotation.EsIdSetting;
import org.n3r.es.annotation.EsIndex;
import org.n3r.es.annotation.EsIndexAlias;
import org.n3r.es.annotation.EsIndexName;
import org.n3r.es.annotation.EsStore;
import org.n3r.es.annotation.EsTransient;
import org.n3r.es.annotation.EsTypeName;
import org.n3r.es.enums.EsFieldType;
import org.n3r.es.enums.EsIndexType;

@EsIndexName( { "simple", "anno" } )
@EsTypeName("simpleType")
@EsIdSetting(index = EsIndexType.NOT_ANALYZED, store = true)
public class SimpleAnnoBean {

    @EsIdField
    @EsIndexAlias("intField")
    private int simpleInteger;

    @EsIndex(EsIndexType.NO)
    private String simpleString;

    @EsField(EsFieldType.STRING)
    @EsStore(true)
    private Date originalDate;

    @EsDateFormat("yyyy-MM-dd")
    private String stringDate;

    @EsTransient
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
