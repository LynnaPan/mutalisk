package io.hybridtheory.mutalisk.executor;


import io.hybridtheory.mutalisk.executor.conf.ElasticClientConf;
import org.apache.http.HttpHost;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ElasticTransportExecutor {
    ElasticTransportExecutorImpl impl;
    private ElasticClientConf conf;
    private TransportClient client;

    public ElasticTransportExecutor(ElasticClientConf conf) {
        this.conf = conf;
    }

    public void connect() {

        if (client != null) {
            return;
        }
        /*
            Settings settings = Settings.builder()
                .put("cluster.name", "myClusterName").build();
                TransportClient client = new PreBuiltTransportClient(settings);
                //Add transport addresses and do something with the client...
         */

        client = new PreBuiltTransportClient(Settings.EMPTY);

        for (HttpHost httpHost : conf.hostPorts) {
            try {
                client.addTransportAddress(
                        new InetSocketTransportAddress(InetAddress.getByName(httpHost.getHostName()), httpHost.getPort()));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Transport Client init done");

        impl = new ElasticTransportExecutorImpl(this);
    }

    public synchronized void close() {
        if (client != null) {

            client.close();
        }

        client = null;
    }

    public ElasticClientConf conf() {
        return conf;
    }

    public TransportClient client() {
        return client;
    }
}
