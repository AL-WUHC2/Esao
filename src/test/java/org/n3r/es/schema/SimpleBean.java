package org.n3r.es.schema;

import java.util.Date;

import org.n3r.core.lang.RBaseBean;

public class SimpleBean extends RBaseBean {

    private int simpleInteger;

    private String simpleString;

    private Date simpleDate;

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

    public Date getSimpleDate() {
        return simpleDate;
    }

    public void setSimpleDate(Date simpleDate) {
        this.simpleDate = simpleDate;
    }

}
