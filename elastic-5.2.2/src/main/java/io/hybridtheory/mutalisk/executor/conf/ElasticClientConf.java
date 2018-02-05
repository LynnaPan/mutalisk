package io.hybridtheory.mutalisk.executor.conf;


import io.hybridtheory.mutalisk.common.conf.ElasticSearchClusterConf;
import org.apache.http.HttpHost;

import java.util.List;

public class ElasticClientConf extends ElasticSearchClusterConf {
    public ElasticClientConf() {
    }

    public ElasticClientConf(List<String> hosts) {
        super(hosts);
    }

    public ElasticClientConf(HttpHost[] hostPorts) {
        super(hostPorts);
    }
}
