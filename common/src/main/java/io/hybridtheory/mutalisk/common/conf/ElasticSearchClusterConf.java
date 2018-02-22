package io.hybridtheory.mutalisk.common.conf;

import org.apache.http.HttpHost;

import java.util.List;

/**
 * Created by kevinlynna on 21/01/2018.
 */
public class ElasticSearchClusterConf {
    public String cluster;
    public HttpHost[] hostPorts;

    public ElasticSearchClusterConf() {
    }

    public ElasticSearchClusterConf(List<String> hosts) {
        hostPorts = new HttpHost[hosts.size()];

        for (int i = 0; i < hostPorts.length; i++) {
            String[] splits = hosts.get(i).split(":");
            hostPorts[i] = new HttpHost(splits[0], Integer.parseInt(splits[1]));
        }
    }

    public ElasticSearchClusterConf(HttpHost[] hostPorts) {
        this.hostPorts = hostPorts;
    }
}
