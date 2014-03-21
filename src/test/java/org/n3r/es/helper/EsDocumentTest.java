package org.n3r.es.helper;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.joda.time.DateTime;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n3r.core.collection.RList;
import org.n3r.core.collection.RMap;
import org.n3r.es.source.SubBean;

public class EsDocumentTest {

    private static TransportClient client;

    private static EsDocumentHelper documentHelper;

    private static EsIndicesHelper indicesHelper;

    @BeforeClass
    public static void setup() {
        Settings settings = settingsBuilder().put("cluster.name", "elasticsearch").build();
        client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

        indicesHelper = new EsIndicesHelper(client);
        indicesHelper.createIndex(DocumentBean.class);
        indicesHelper.putMapping(DocumentBean.class);

        documentHelper = new EsDocumentHelper(client);
    }

    @AfterClass
    public static void cleanup() {
        indicesHelper.deleteMapping(DocumentBean.class);
        indicesHelper.deleteIndex(DocumentBean.class);
        client.close();
    }

    @Test
    public void testSingleDocument() {
        DocumentBean obj1 = new DocumentBean();
        obj1.setDocId("1234");
        obj1.setDocContent("djashddassidhf");
        documentHelper.index(obj1);
        assertTrue(documentHelper.exists(DocumentBean.class, "1234"));

        Object getRaw = documentHelper.get("document", "docType", "1234");
        DocumentBean getResult = documentHelper.get(DocumentBean.class, "1234");
        assertTrue(getResult.equals(getRaw));

        documentHelper.delete("document", "docType", "1234");
        getRaw = documentHelper.get("document", "docType", "1234");
        assertNull(getRaw);

        documentHelper.index(obj1, "1235");
        DocumentBean getResult1 = documentHelper.get(DocumentBean.class, "1234");
        DocumentBean getResult2 = documentHelper.get(DocumentBean.class, "1235");
        assertNull(getResult1);
        assertTrue(obj1.equals(getResult2));

        DocumentBean obj2 = new DocumentBean();
        obj2.setDocId("1236");
        obj2.setDocContent("ouyqegotuqbzdfa");
        documentHelper.update(obj2, "1235");
        getResult2 = documentHelper.get(DocumentBean.class, "1235");
        assertEquals("1236", getResult2.getDocId());
        assertEquals("ouyqegotuqbzdfa", getResult2.getDocContent());

        documentHelper.update(DocumentBean.class, "1235",
                RMap.of("docId", "1237", "docContent", "djashddassidhf"));
        getResult2 = documentHelper.get(DocumentBean.class, "1235");
        assertEquals("1237", getResult2.getDocId());
        assertEquals("djashddassidhf", getResult2.getDocContent());

        documentHelper.delete(DocumentBean.class, "1235");
        assertFalse(documentHelper.exists(DocumentBean.class, "1235"));
    }

    @Test
    public void testNoMapping() {
        NoMapBean bean = new NoMapBean();
        bean.setId("1234");
        bean.setContent("there id no mapping before index.");
        documentHelper.index(bean);
        assertTrue(documentHelper.exists(NoMapBean.class, "1234"));
        NoMapBean getResult = documentHelper.get(NoMapBean.class, "1234");
        assertEquals("there id no mapping before index.", getResult.getContent());

        documentHelper.delete(NoMapBean.class, "1234");
        assertFalse(documentHelper.exists(NoMapBean.class, "1234"));

        indicesHelper.deleteIndex(NoMapBean.class);
    }

    @Test
    public void testFieldTypes() {
        indicesHelper.putMapping(ConvertBean.class);

        ConvertBean bean = new ConvertBean();
        bean.setField01(1);
        bean.setField02(2.3f);
        bean.setField03(true);
        bean.setField04("456");

        Date date = new DateTime("2014-03-19T21:53:01.924").toDate();
        bean.setField05(date);

        List<SubBean> list = new ArrayList<SubBean>();
        SubBean sub01 = new SubBean();
        sub01.setSub01("aaa");
        sub01.setSub02(date);
        list.add(sub01);
        SubBean sub02 = new SubBean();
        sub02.setSub01("bbb");
        sub02.setSub02(date);
        list.add(sub02);
        bean.setField06(list);

        Map<String, SubBean> map = new HashMap<String, SubBean>();
        map.put("ccc", sub01);
        map.put("ddd", sub02);
        bean.setField07(map);

        SubBean sub = new SubBean();
        sub.setSub01("qwert");
        sub.setSub02(date);
        bean.setField08(sub);

        documentHelper.index(bean);
        Map<String, Object> raw = documentHelper.getRaw(ConvertBean.class, "1");
        Map<Object, Object> expRaw = RMap.of(
                "field01", 1,
                "field02", 2.3,
                "field03", true,
                "field04", "456",
                "field05", "2014-03-19T21:53:01.924",
                "field06", RList.asList(
                        RMap.of("sub01", "aaa", "sub02", "2014-03-19 21:53:01"),
                        RMap.of("sub01", "bbb", "sub02", "2014-03-19 21:53:01")),
                "field07", RMap.of(
                        "ccc", RMap.of("sub01", "aaa", "sub02", "2014-03-19 21:53:01"),
                        "ddd", RMap.of("sub01", "bbb", "sub02", "2014-03-19 21:53:01")
                        ),
                "field08", RMap.of("sub01", "qwert", "sub02", "2014-03-19 21:53:01"));
        assertEquals(expRaw, raw);

        ConvertBean bean2 = documentHelper.get(ConvertBean.class, "1");
        assertEquals(1, bean2.getField01());
        assertTrue(2.3f == bean2.getField02());
        assertTrue(bean2.getField03());
        assertEquals("456", bean2.getField04());
        assertEquals(date, bean2.getField05());
        assertEquals(list, bean2.getField06());
        assertEquals(map, bean2.getField07());
        assertTrue(sub.equals(bean2.getField08()));

        indicesHelper.deleteIndex(ConvertBean.class);
    }

}
