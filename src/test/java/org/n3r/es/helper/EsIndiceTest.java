package org.n3r.es.helper;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class EsIndiceTest {

    private static TransportClient client;

    private static EsIndicesHelper indicesHelper;

    @BeforeClass
    public static void setup() {
        Settings settings = settingsBuilder().put("cluster.name", "elasticsearch").build();
        client = new TransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress("127.0.0.1", 9300));
        indicesHelper = new EsIndicesHelper(client);
        indicesHelper.deleteIndex("noneIndex", "test01", "test02");
    }

    @AfterClass
    public static void cleanup() {
        client.close();
    }

    @Test
    public void testIndexApi() {
        assertFalse(indicesHelper.indicesExists("noneIndex"));

        assertTrue(indicesHelper.createIndex("test01", "test02"));
        assertTrue(indicesHelper.indicesExists("test01", "test02"));
        assertFalse(indicesHelper.indicesExists("test01", "test02", "noneIndex"));

        assertTrue(indicesHelper.deleteIndex("test01", "test02"));
        assertFalse(indicesHelper.indicesExists("test01", "test02"));
    }

    @Test
    public void testMappingApi() throws Exception {
        assertFalse(indicesHelper.typeExists("noneIndex", "noneType"));
        assertTrue(indicesHelper.deleteMapping("noneIndex", "noneType"));

        XContentBuilder noneType = XContentFactory.jsonBuilder()
                .startObject()
                .startObject("noneType")
                .startObject("properties")
                .startObject("noneField").field("type", "string").endObject()
                .endObject()
                .endObject()
                .endObject();
        assertTrue(indicesHelper.putMapping("noneIndex", "noneType", noneType.string()));
        assertTrue(indicesHelper.typeExists("noneIndex", "noneType"));
        assertTrue(indicesHelper.putMapping("noneIndex", "noneType", noneType.string()));

        assertTrue(indicesHelper.deleteMapping("noneIndex", "noneType"));
        assertFalse(indicesHelper.typeExists("noneIndex", "noneType"));
        assertTrue(indicesHelper.deleteMapping("noneIndex", "noneType"));
    }

}