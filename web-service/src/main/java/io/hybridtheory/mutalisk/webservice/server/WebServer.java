package io.hybridtheory.mutalisk.webservice.server;


import io.hybridtheory.mutalisk.webservice.app.ElasticExecutorBinder;
import io.hybridtheory.mutalisk.webservice.resource.ElasticApplication;
import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import java.net.URI;

public class WebServer {
    private WebServerConf conf;
    private ResourceConfig resourceConfig;
    private Channel server;

    public WebServer(WebServerConf conf, ResourceConfig resourceConfig) {
        this.conf = conf;
        this.resourceConfig = resourceConfig.register(new ElasticExecutorBinder());
    }

    public synchronized Channel doStart() {
        if (server == null) {
            server = NettyHttpContainerProvider.createServer(this.conf.uri, resourceConfig, true);
        }

        return server;
    }

    public synchronized void close() {
        if (server != null) {
            server.close();

            server = null;
        }
    }

    public static void main(String[] args) {
        WebServerConf conf = new WebServerConf(URI.create("http://localhost:9000"));
        ElasticApplication application = new ElasticApplication();

        new WebServer(conf, application).doStart();

        System.out.println(String.format("Application started. (HTTP/2 enabled!)\nTry out %s%s\nStop the application using "
            + "CTRL+C.", conf.uri, "elastic"));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
