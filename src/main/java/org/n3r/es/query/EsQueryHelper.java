package org.n3r.es.query;

import static org.elasticsearch.search.sort.SortBuilders.fieldSort;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.n3r.es.cache.EsSchemaCache;
import org.n3r.es.exception.EsaoRuntimeException;
import org.n3r.es.schema.EsSchema;
import org.n3r.es.schema.builder.EsSchemaBuilder;

public class EsQueryHelper {

    public static final int DEFAULT_SIZE = 10;

    private String index;

    private String type;

    private String[] includes;

    private String[] excludes;

    private EsQueryPage page = new EsQueryPage();

    private List<SortBuilder> sorts = new LinkedList<SortBuilder>();

    private EsQueryOn queryField;

    private BoolQueryBuilder query = new BoolQueryBuilder();

    private EsSourceConverter rsConverter = new EsSourceConverter();

////Multiple Indexes/Types//////////////////////////////////////////////////////

    public EsQueryHelper on(Class<?> clazz) {
        EsSchema schema = new EsSchemaBuilder(clazz).schema();
        rsConverter.setReturnType(clazz);
        return on(schema.getIndex(), schema.getType());
    }

    public EsQueryHelper on(String index, String type) {
        this.index = index;
        this.type = type;
        rsConverter.setReturnType(EsSchemaCache.
                reflect(index + ":" + type));
        return this;
    }

////Query Fields////////////////////////////////////////////////////////////////

    public EsQueryHelper includeFields(String... fields) {
        this.includes = fields;
        return this;
    }

    public EsQueryHelper excludeFields(String... fields) {
        this.excludes = fields;
        return this;
    }

////Pagination//////////////////////////////////////////////////////////////////

    public EsQueryHelper limit(EsQueryPage page) {
        this.page = page;
        rsConverter.setRsSize(page.getSize());
        return this;
    }

    public EsQueryHelper limit(int size) {
        page.setSize(size);
        rsConverter.setRsSize(size);
        return this;
    }

////Sorting/////////////////////////////////////////////////////////////////////

    public EsQueryHelper orderBy(String... fields) {
        return ascBy(fields);
    }

    public EsQueryHelper ascBy(String... fields) {
        addSortPair(fields, SortOrder.ASC);
        return this;
    }

    public EsQueryHelper descBy(String... fields) {
        addSortPair(fields, SortOrder.DESC);
        return this;
    }

    private void addSortPair(String[] fields, SortOrder order) {
        for (int i = 0; i < fields.length; i++) {
            sorts.add(fieldSort(fields[i]).order(order));
        }
    }

////Query Condition/////////////////////////////////////////////////////////////

    public EsQueryHelper where(String field) {
        queryField = new EsQueryOn(field);
        return this;
    }

    private void validateEsQueryOn() {
        if (queryField == null) throw new EsaoRuntimeException("Query Field unspecify!");
    }

    public EsQueryHelper must(QueryBuilder queryBuilder) {
        query.must(queryBuilder);
        return this;
    }

    public EsQueryHelper should(QueryBuilder queryBuilder) {
        query.should(queryBuilder);
        return this;
    }

    public EsQueryHelper not(QueryBuilder queryBuilder) {
        query.mustNot(queryBuilder);
        return this;
    }

    public EsQueryHelper mustContains(String value) {
        validateEsQueryOn();
        return this.must(queryField.contains(value));
    }

    public EsQueryHelper mustContains(Object... values) {
        validateEsQueryOn();
        return this.must(queryField.contains(values));
    }

    public EsQueryHelper mustContains(Collection<?> values) {
        validateEsQueryOn();
        return this.must(queryField.contains(values));
    }

    public EsQueryHelper mustMatch(String regexp) {
        validateEsQueryOn();
        return this.must(queryField.match(regexp));
    }

    public EsQueryHelper mustStartWith(String prefix) {
        validateEsQueryOn();
        return this.must(queryField.startWith(prefix));
    }

    public EsQueryHelper mustMoreThan(Object value) {
        validateEsQueryOn();
        return this.must(queryField.moreThan(value));
    }

    public EsQueryHelper mustMoreThanExclude(Object value) {
        validateEsQueryOn();
        return this.must(queryField.moreThanExclude(value));
    }

    public EsQueryHelper mustLessThan(Object value) {
        validateEsQueryOn();
        return this.must(queryField.lessThan(value));
    }

    public EsQueryHelper mustLessThanExclude(Object value) {
        validateEsQueryOn();
        return this.must(queryField.lessThanExclude(value));
    }

    public EsQueryHelper mustBetween(Object more, Object less) {
        validateEsQueryOn();
        return this.must(queryField.between(more, less));
    }

    public EsQueryHelper mustBetweenExcludeLeft(Object more, Object less) {
        validateEsQueryOn();
        return this.must(queryField.betweenExcludeLeft(more, less));
    }

    public EsQueryHelper mustBetweenExcludeRight(Object more, Object less) {
        validateEsQueryOn();
        return this.must(queryField.betweenExcludeRight(more, less));
    }

    public EsQueryHelper mustBetweenExcludeBoth(Object more, Object less) {
        validateEsQueryOn();
        return this.must(queryField.betweenExcludeBoth(more, less));
    }

    public EsQueryHelper shouldContains(String value) {
        validateEsQueryOn();
        return this.should(queryField.contains(value));
    }

    public EsQueryHelper shouldContains(Object... values) {
        validateEsQueryOn();
        return this.should(queryField.contains(values));
    }

    public EsQueryHelper shouldContains(Collection<?> values) {
        validateEsQueryOn();
        return this.should(queryField.contains(values));
    }

    public EsQueryHelper shouldMatch(String regexp) {
        validateEsQueryOn();
        return this.should(queryField.match(regexp));
    }

    public EsQueryHelper shouldStartWith(String prefix) {
        validateEsQueryOn();
        return this.should(queryField.startWith(prefix));
    }

    public EsQueryHelper shouldMoreThan(Object value) {
        validateEsQueryOn();
        return this.should(queryField.moreThan(value));
    }

    public EsQueryHelper shouldMoreThanExclude(Object value) {
        validateEsQueryOn();
        return this.should(queryField.moreThanExclude(value));
    }

    public EsQueryHelper shouldLessThan(Object value) {
        validateEsQueryOn();
        return this.should(queryField.lessThan(value));
    }

    public EsQueryHelper shouldLessThanExclude(Object value) {
        validateEsQueryOn();
        return this.should(queryField.lessThanExclude(value));
    }

    public EsQueryHelper shouldBetween(Object more, Object less) {
        validateEsQueryOn();
        return this.should(queryField.between(more, less));
    }

    public EsQueryHelper shouldBetweenExcludeLeft(Object more, Object less) {
        validateEsQueryOn();
        return this.should(queryField.betweenExcludeLeft(more, less));
    }

    public EsQueryHelper shouldBetweenExcludeRight(Object more, Object less) {
        validateEsQueryOn();
        return this.should(queryField.betweenExcludeRight(more, less));
    }

    public EsQueryHelper shouldBetweenExcludeBoth(Object more, Object less) {
        validateEsQueryOn();
        return this.should(queryField.betweenExcludeBoth(more, less));
    }

    public EsQueryHelper notContains(String value) {
        validateEsQueryOn();
        return this.not(queryField.contains(value));
    }

    public EsQueryHelper notContains(Object... values) {
        validateEsQueryOn();
        return this.not(queryField.contains(values));
    }

    public EsQueryHelper notContains(Collection<?> values) {
        validateEsQueryOn();
        return this.not(queryField.contains(values));
    }

    public EsQueryHelper notMatch(String regexp) {
        validateEsQueryOn();
        return this.not(queryField.match(regexp));
    }

    public EsQueryHelper notStartWith(String prefix) {
        validateEsQueryOn();
        return this.not(queryField.startWith(prefix));
    }

    public EsQueryHelper notMoreThan(Object value) {
        validateEsQueryOn();
        return this.not(queryField.moreThan(value));
    }

    public EsQueryHelper notMoreThanExclude(Object value) {
        validateEsQueryOn();
        return this.not(queryField.moreThanExclude(value));
    }

    public EsQueryHelper notLessThan(Object value) {
        validateEsQueryOn();
        return this.not(queryField.lessThan(value));
    }

    public EsQueryHelper notLessThanExclude(Object value) {
        validateEsQueryOn();
        return this.not(queryField.lessThanExclude(value));
    }

    public EsQueryHelper notBetween(Object more, Object less) {
        validateEsQueryOn();
        return this.not(queryField.between(more, less));
    }

    public EsQueryHelper notBetweenExcludeLeft(Object more, Object less) {
        validateEsQueryOn();
        return this.not(queryField.betweenExcludeLeft(more, less));
    }

    public EsQueryHelper notBetweenExcludeRight(Object more, Object less) {
        validateEsQueryOn();
        return this.not(queryField.betweenExcludeRight(more, less));
    }

    public EsQueryHelper notBetweenExcludeBoth(Object more, Object less) {
        validateEsQueryOn();
        return this.not(queryField.betweenExcludeBoth(more, less));
    }

////Result Convert//////////////////////////////////////////////////////////////

    public EsQueryHelper returnType(Class<?> clazz) {
        rsConverter.setReturnType(clazz);
        return this;
    }

    public EsQueryHelper returnSourceMap() {
        rsConverter.setReturnType(null);
        return this;
    }

////Execute/////////////////////////////////////////////////////////////////////

    public <T> T execute(TransportClient client) {
        if (page.getTotal() == 0) page.setTotal(countTotalRows(client));

        SearchHits searchHits = buildRequest(client).execute()
                .actionGet().getHits();
        return (T) rsConverter.convert(searchHits);
    }

    public int countTotalRows(TransportClient client) {
        long count = new CountRequestBuilder(client).setIndices(index)
                .setTypes(type).execute().actionGet().getCount();
        return count > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) count;
    }

    private SearchRequestBuilder buildRequest(TransportClient client) {
        SearchRequestBuilder request = new SearchRequestBuilder(client);
        request.setIndices(index).setTypes(type);
        request.setFetchSource(includes, excludes);
        request.setFrom(page.calcStartIndex());
        request.setSize(page.getSize());
        if (query.hasClauses()) request.setQuery(query);
        for (SortBuilder sort : sorts) {
            request.addSort(sort);
        }
        return request;
    }

}
