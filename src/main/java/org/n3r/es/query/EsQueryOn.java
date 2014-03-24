package org.n3r.es.query;

import static org.elasticsearch.index.query.QueryBuilders.prefixQuery;
import static org.elasticsearch.index.query.QueryBuilders.rangeQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.termsQuery;

import java.util.Collection;

import org.elasticsearch.index.query.QueryBuilder;

public class EsQueryOn {

    private String fieldName;

    public EsQueryOn(String fieldName) {
        this.fieldName = fieldName;
    }

    public QueryBuilder contains(String value) {
        return termQuery(fieldName, value);
    }

    public QueryBuilder contains(Object... values) {
        return termsQuery(fieldName, values);
    }

    public QueryBuilder contains(Collection<?> values) {
        return termsQuery(fieldName, values);
    }

    public QueryBuilder match(String regexp) {
        return regexpQuery(fieldName, regexp);
    }

    public QueryBuilder startWith(String prefix) {
        return prefixQuery(fieldName, prefix);
    }

    public QueryBuilder moreThan(Object value) {
        return rangeQuery(fieldName).from(value);
    }

    public QueryBuilder moreThanExclude(Object value) {
        return rangeQuery(fieldName).from(value).includeLower(false);
    }

    public QueryBuilder lessThan(Object value) {
        return rangeQuery(fieldName).to(value);
    }

    public QueryBuilder lessThanExclude(Object value) {
        return rangeQuery(fieldName).to(value).includeUpper(false);
    }

    public QueryBuilder between(Object more, Object less) {
        return rangeQuery(fieldName).from(more).to(less);
    }

    public QueryBuilder betweenExcludeLeft(Object more, Object less) {
        return rangeQuery(fieldName).from(more).to(less).includeLower(false);
    }

    public QueryBuilder betweenExcludeRight(Object more, Object less) {
        return rangeQuery(fieldName).from(more).to(less).includeUpper(false);
    }

    public QueryBuilder betweenExcludeBoth(Object more, Object less) {
        return rangeQuery(fieldName).from(more).to(less).includeLower(false).includeUpper(false);
    }

}
