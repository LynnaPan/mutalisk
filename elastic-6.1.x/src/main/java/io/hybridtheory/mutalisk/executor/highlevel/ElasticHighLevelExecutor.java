package io.hybridtheory.mutalisk.executor.highlevel;


import io.hybridtheory.mutalisk.executor.ElasticExecutor;
import io.hybridtheory.mutalisk.executor.conf.ElasticClientConf;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
public class ElasticHighLevelExecutor implements ElasticExecutor {
    private ElasticClientConf conf;
    private RestHighLevelClient client;
    private ElasticHighLevelExecutorImpl impl;

    public ElasticHighLevelExecutor(ElasticClientConf conf) {
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

        client = new RestHighLevelClient(
            RestClient.builder(conf.hostPorts));


        System.out.println("Transport Client init done");

        impl = new ElasticHighLevelExecutorImpl(this);
    }

    public synchronized void close() {
        if (client != null) {

            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                client = null;
            }
        }
    }

    public ElasticClientConf conf() {
        return conf;
    }

    public RestHighLevelClient client() {
        return client;
    }

    public ElasticHighLevelExecutorImpl impl() {
        return impl;
    }
}
