package org.n3r.es;

import static org.junit.Assert.assertEquals;

import org.elasticsearch.client.transport.TransportClient;
import org.junit.Test;
import org.n3r.es.config.EsClientConfig;
import org.n3r.es.config.EsClientConfigManager;
import org.n3r.es.exception.EsaoConfigException;

public class EsaoTest {

    @Test
    public void testConfig() {
        EsClientConfig config = EsClientConfigManager.getConfig("elasticsearch");
        assertEquals(config.clusterName(), "elasticsearch");
        assertEquals(config.addressList().size(), 3);
        assertEquals(config.addressList().get(1).getFirst(), "192.168.0.101");
        assertEquals(config.addressList().get(2).getSecond(), new Integer(9300));

        TransportClient client = config.client();
        assertEquals(client.settings().get("cluster.name"), "elasticsearch");;
        assertEquals(config.client().transportAddresses().size(), 3);
    }

    @Test(expected = EsaoConfigException.class)
    public void testConfigNone() {
        EsClientConfigManager.getConfig("noneconfig");

    }

}
