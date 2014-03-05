package org.n3r.es;

import org.elasticsearch.client.transport.TransportClient;
import org.n3r.es.config.EsClientConfigManager;

public class Esao {

    private TransportClient client;

    public Esao() {
        client = EsClientConfigManager.getDefConfig().client();
    }

    public Esao(String clusterName) {
        client = EsClientConfigManager.getConfig(clusterName).client();
    }

    public void close() {
        client.close();
    }


}
