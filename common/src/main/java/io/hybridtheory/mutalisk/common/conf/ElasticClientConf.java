package io.hybridtheory.mutalisk.common.conf;

import org.apache.http.HttpHost;

import java.util.List;

/**
 * Created by kevinlynna on 21/01/2018.
 */
public class ElasticClientConf {
    public String cluster;
    public HttpHost[] hostPorts;

    public ElasticClientConf() {
    }

    public ElasticClientConf(List<String> hosts) {
        hostPorts = new HttpHost[hosts.size()];

        for (int i = 0; i < hostPorts.length; i++) {
            String[] splits = hosts.get(i).split(":");
            hostPorts[i] = new HttpHost(splits[0], Integer.parseInt(splits[1]));
        }
    }

    public ElasticClientConf(HttpHost[] hostPorts) {
        this.hostPorts = hostPorts;
    }
}
