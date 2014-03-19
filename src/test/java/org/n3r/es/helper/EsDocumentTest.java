package org.n3r.es.helper;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n3r.core.collection.RMap;

public class EsDocumentTest {

    private static TransportClient client;

    private static EsDocumentHelper documentHelper;

    @BeforeClass
    public static void setup() {
        Settings settings = settingsBuilder().put("cluster.name", "elasticsearch").build();
        client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

        EsIndicesHelper indicesHelper = new EsIndicesHelper(client);
        indicesHelper.createIndex(DocumentBean.class);
        indicesHelper.putMapping(DocumentBean.class);

        documentHelper = new EsDocumentHelper(client);
    }

    @AfterClass
    public static void cleanup() {
        EsIndicesHelper indicesHelper = new EsIndicesHelper(client);
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
    }

}
