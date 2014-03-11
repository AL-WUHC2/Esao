package org.n3r.es.helper;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.junit.Assert.assertTrue;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
        Object getRaw = documentHelper.get("document", "docType", "1234");
        DocumentBean getResult = documentHelper.get(DocumentBean.class, "1234");
        assertTrue(getResult.equals(getRaw));
    }

}
