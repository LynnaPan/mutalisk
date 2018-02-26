package io.hybridtheory.mutalisk.transport.executor;

import io.hybridtheory.mutalisk.common.conf.ElasticClientConf;
import org.apache.http.HttpHost;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;

public class ElasticTransportExecutorBuilder {
    private ElasticClientConf conf;

    public ElasticTransportExecutorBuilder setConf(ElasticClientConf conf) {
        this.conf = conf;
        return this;
    }

    public ElasticTransportExecutor build() {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY);

        for (HttpHost httpHost : conf.hostPorts) {
            try {
                client.addTransportAddress(
                        new TransportAddress(InetAddress.getByName(httpHost.getHostName()), httpHost.getPort()));
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return null;
    }
}
