package org.n3r.es.config;

import static com.google.common.collect.Maps.newHashMap;
import static org.apache.commons.lang3.StringUtils.split;
import static org.n3r.core.lang.RStr.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.n3r.config.Config;
import org.n3r.core.lang.Pair;
import org.n3r.core.lang.RStr;
import org.n3r.es.exception.EsaoConfigException;

public class EsClientConfigManager {

    private static Map<String, EsClientConfig> configMap = newHashMap();

    private static String defaultClusterName = Config.getStr("esDefCluster", "elasticsearch");

    static {
        initialize();
    }

    private static void initialize() {
        String clusterName = Config.getStr("esClusterName", "elasticsearch");

        String[] addresses = split(Config.getStr("esNodeAddress", ""), ",");
        List<Pair<String, Integer>> addressLs = new ArrayList<Pair<String, Integer>>(addresses.length);
        for (String address : addresses) {
            String[] addrConf = split(address, ":");
            if (addrConf.length <= 0) continue;

            String host = addrConf[0].trim();
            if (isEmpty(host)) continue;
            int port = addrConf.length == 1 ? 9300 : RStr.toInteger(addrConf[1].trim(), 9300);

            addressLs.add(new Pair<String, Integer>(host, port));
        }
        configMap.put(clusterName, new EsClientConfig(clusterName, addressLs));
    }

    public static EsClientConfig getConfig(String clusterName) {
        EsClientConfig esClientConfig = configMap.get(clusterName);
        if (esClientConfig != null) return esClientConfig;

        throw new EsaoConfigException(clusterName + " cluster is not properly configed.");
    }

    public static EsClientConfig getDefConfig() {
        return getConfig(defaultClusterName);
    }

}
