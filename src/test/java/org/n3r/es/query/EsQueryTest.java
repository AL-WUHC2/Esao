package org.n3r.es.query;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n3r.core.collection.RMap;
import org.n3r.core.lang.RStr;
import org.n3r.es.helper.DocumentBean;
import org.n3r.es.helper.EsBulkHelper;
import org.n3r.es.helper.EsIndicesHelper;
import org.n3r.es.schema.SimpleAnnoBean;
import org.n3r.es.schema.SimpleBean;

public class EsQueryTest {

    private static TransportClient client;

    private static EsBulkHelper bulkHelper;

    private static EsIndicesHelper indicesHelper;

//    private EsQueryHelper queryHelper;

    @BeforeClass
    public static void setup() {
        Settings settings = settingsBuilder().put("cluster.name", "elasticsearch").build();
        client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

        indicesHelper = new EsIndicesHelper(client);
        bulkHelper = new EsBulkHelper(client);
    }

    @AfterClass
    public static void cleanup() {
        client.close();
    }

    @Test
    public void testSearch() {
        indicesHelper.putMapping(DocumentBean.class);
        DocumentBean[] srcs = new DocumentBean[100];
        for (int i = 0; i < srcs.length; i++) {
            srcs[i] = new DocumentBean();
            srcs[i].setDocId(RStr.toStr(i));
            srcs[i].setDocContent(RStr.toStr(i * 5));
            bulkHelper.index(srcs[i]);
        }
        bulkHelper.execute();
        indicesHelper.refreshIndex(DocumentBean.class);

        EsQueryPage page = new EsQueryPage();
        page.setCurrent(3);
        EsQueryHelper query = new EsQueryHelper().onIndexes("document")
                .onTypes("docType").descBy("docId").ascBy("docContent");
        query.where("docContent").mustMoreThan("90");
        while (query.countTotalRows(client) != 100) {}

        List<Map> result = query.execute(client);
        Map<String, String> exp01 = RMap.of("docId", "19", "docContent", "95");
        Map<String, String> exp02 = RMap.of("docId", "18", "docContent", "90");
        assertEquals(2, result.size());
        assertEquals(exp01, result.get(0));
        assertEquals(exp02, result.get(1));

        indicesHelper.deleteIndex(DocumentBean.class);
    }

    @Test
    public void testSimpleBeanResult() {
        indicesHelper.putMapping(SimpleBean.class);
        SimpleBean bean = new SimpleBean();
        bean.setSimpleDate(new Date(1395237181924L));
        bean.setSimpleInteger(12);
        bean.setSimpleString("345");
        bulkHelper.index("simplebean", "org.n3r.es.schema.SimpleBean",
                "{\"simpleDate\":\"2014-03-18\",\"simpleInteger\":12,\"simpleString\":\"345\"}");
        bulkHelper.index(bean).execute();
        indicesHelper.refreshIndex(SimpleBean.class);

        EsQueryHelper query = new EsQueryHelper().onIndexes("simplebean")
                .onTypes("org.n3r.es.schema.SimpleBean").orderBy("simpleDate");
        while (query.countTotalRows(client) == 0) {}

        List<SimpleBean> result = query.returnType(SimpleBean.class).execute(client);
        assertEquals(2, result.size());
        assertEquals(new DateTime("2014-03-18").toDate(), result.get(0).getSimpleDate());
        assertEquals(bean, result.get(1));

        indicesHelper.deleteIndex(SimpleBean.class);
    }

    @Test
    public void testAnnoBeanResult() {
        SimpleAnnoBean bean = new SimpleAnnoBean();
        bean.setSimpleInteger(12);
        bean.setSimpleString("345");
        bean.setOriginalDate(new Date());
        bean.setStringDate("2014-03-14");
        bulkHelper.index(bean).execute();
        indicesHelper.refreshIndex(SimpleAnnoBean.class);

        EsQueryHelper query = new EsQueryHelper().onIndexes("simple")
                .onTypes("simpleType");
        while (query.countTotalRows(client) != 1) {}

        SimpleAnnoBean result = query.returnType(SimpleAnnoBean.class).execute(client);
        assertEquals(bean, result);

        new EsIndicesHelper(client).deleteIndex(SimpleAnnoBean.class);
    }

}
