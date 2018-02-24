package io.hybridtheory.mutalisk.executor.conf;


import org.apache.http.HttpHost;

import java.util.List;

public class ElasticClientConf extends io.hybridtheory.mutalisk.common.conf.ElasticClientConf {
    public ElasticClientConf() {
    }

    public ElasticClientConf(List<String> hosts) {
        super(hosts);
    }

    public ElasticClientConf(HttpHost[] hostPorts) {
        super(hostPorts);
    }
}
