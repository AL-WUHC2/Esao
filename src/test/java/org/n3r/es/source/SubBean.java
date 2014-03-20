package org.n3r.es.source;

import java.util.Date;

import org.joda.time.DateTime;
import org.n3r.core.lang.RBaseBean;
import org.n3r.es.schema.anno.EsMapDateFormat;

public class SubBean extends RBaseBean {

    private String sub01;

    @EsMapDateFormat("yyyy-MM-dd HH:mm:ss")
    private Date sub02;

    public String getSub01() {
        return sub01;
    }

    public void setSub01(String sub01) {
        this.sub01 = sub01;
    }

    public Date getSub02() {
        return sub02;
    }

    public void setSub02(Date sub02) {
        this.sub02 = sub02;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != this.getClass()) return false;
        SubBean b = (SubBean) obj;
        if (b.getSub01() == null && sub01 == null
                && b.getSub02() == null
                && sub02 == null) return true;
        if (b.getSub01() == null || sub01 == null
                || b.getSub02() == null
                || sub02 == null) return false;
        else return b.getSub01().equals(sub01)
                && new DateTime(b.getSub02()).
                toString("yyyy-MM-dd HH:mm:ss").
                equals(new DateTime(sub02).
                        toString("yyyy-MM-dd HH:mm:ss"));
    }

}
