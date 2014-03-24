package org.n3r.es.source.from;

import static org.n3r.core.lang.RStr.isEmpty;
import static org.n3r.core.lang.RStr.toStr;

import java.util.Date;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.ISODateTimeFormat;
import org.n3r.es.source.FromSourceConverter;
import org.n3r.es.source.anno.EsBindTo;
import org.n3r.es.source.base.DateSourceConverter;

@EsBindTo(Date.class)
public class DateFromSource extends DateSourceConverter implements FromSourceConverter {

    @Override
    public <T> T fromSource(Object source, Class<T> clazz) {
        return (T) dateFormatParser().parseDateTime(toStr(source)).toDate();
    }

    protected DateTimeFormatter dateFormatParser() {
        String pattern = toStr(getOption("dateFormat"));
        return isEmpty(pattern) ? ISODateTimeFormat.dateOptionalTimeParser()
                : new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter();
    }

}
