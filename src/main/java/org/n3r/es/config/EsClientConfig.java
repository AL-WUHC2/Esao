package org.n3r.es.config;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.n3r.core.lang.Pair;

public class EsClientConfig {

    private String clusterName = "elasticsearch";

    private List<Pair<String, Integer>> addressList = new ArrayList<Pair<String, Integer>>();

    public EsClientConfig() {}

    public EsClientConfig(String clusterName, List<Pair<String, Integer>> addressList) {
        this.clusterName = clusterName;
        this.addressList = addressList;
    }

    public TransportClient client() {
        Settings settings = settingsBuilder().put("cluster.name", clusterName).build();
        TransportClient client = new TransportClient(settings);
        for (Pair<String, Integer> address : addressList) {
            client.addTransportAddress(new InetSocketTransportAddress(address.getFirst(), address.getSecond()));
        }
        return client;
    }

    public String clusterName() {
        return clusterName;
    }

    public void clusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public List<Pair<String, Integer>> addressList() {
        return addressList;
    }

    public void addressList(List<Pair<String, Integer>> addressList) {
        this.addressList = addressList;
    }

}
