package org.n3r.es.helper;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.junit.Assert.assertEquals;
import static org.n3r.core.lang.RStr.toStr;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EsBulkTest {

    private static TransportClient client;

    private static EsBulkHelper bulkHelper;

    private static EsDocumentHelper documentHelper;

    @BeforeClass
    public static void setup() {
        Settings settings = settingsBuilder().put("cluster.name", "elasticsearch").build();
        client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));

        EsIndicesHelper indicesHelper = new EsIndicesHelper(client);
        indicesHelper.createIndex(DocumentBean.class);
        indicesHelper.putMapping(DocumentBean.class);

        bulkHelper = new EsBulkHelper(client);
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
    public void testBulk() {
        DocumentBean[] srcs = new DocumentBean[7];
        for (int i = 0; i < srcs.length; i++) {
            srcs[i] = new DocumentBean();
            srcs[i].setDocId(toStr(i));
            srcs[i].setDocContent(toStr(i * 5));
            bulkHelper.index(srcs[i]);
        }
        bulkHelper.execute();

        for (int i = 0; i < srcs.length; i++) {
            DocumentBean bean = documentHelper.get(DocumentBean.class, toStr(i));
            assertEquals(srcs[i], bean);

            srcs[i].setDocId(toStr(i * 2));
            bulkHelper.update(srcs[i], toStr(i));
        }
        bulkHelper.execute();

        for (int i = 0; i < srcs.length; i++) {
            DocumentBean bean = documentHelper.get(DocumentBean.class, toStr(i));
            assertEquals(srcs[i], bean);

            bulkHelper.delete(DocumentBean.class, toStr(i));
        }
        bulkHelper.execute();
    }

}
