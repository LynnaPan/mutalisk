package io.hybridtheory.mutalisk.common.conf;

import org.apache.http.HttpHost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kevinlynna on 21/01/2018.
 */
public class ElasticClientConf {
    public String cluster;
    public HttpHost[] hostPorts;

    public Map<String, String> settings;

    public ElasticClientConf() {
        this.settings = new HashMap<>();
    }

    public ElasticClientConf setCluster(String cluster) {
        this.cluster = cluster;

        return this;
    }

    public ElasticClientConf setHostPorts(HttpHost[] hostPorts) {
        this.hostPorts = hostPorts;

        return this;
    }

    public ElasticClientConf putSetting(String key, String value) {
        this.settings.put(key, value);

        return this;
    }
}
