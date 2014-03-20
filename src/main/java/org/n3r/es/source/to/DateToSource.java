package org.n3r.es.source.to;

import java.util.Date;

import org.joda.time.DateTime;
import org.n3r.es.source.ToSourceConverter;
import org.n3r.es.source.anno.EsBindTo;
import org.n3r.es.source.base.DateSourceConverter;

@EsBindTo(Date.class)
public class DateToSource extends DateSourceConverter implements ToSourceConverter {

    @Override
    public String toSource(Object obj) {
        return new DateTime(obj).toString(getDateFormat());
    }

}
