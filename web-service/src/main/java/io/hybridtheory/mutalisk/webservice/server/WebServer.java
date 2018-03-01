package io.hybridtheory.mutalisk.webservice.server;


import io.hybridtheory.mutalisk.webservice.app.ElasticExecutorBinder;
import io.hybridtheory.mutalisk.webservice.resource.ElasticApplication;
import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class WebServer {
    private WebServerConf conf;
    private ResourceConfig resourceConfig;
    private Channel server;

    public WebServer(WebServerConf conf, ResourceConfig resourceConfig) {
        this.conf = conf;
        this.resourceConfig = resourceConfig.register(new ElasticExecutorBinder());
    }

    public static void main(String[] args) {
        URI baseUri = UriBuilder.fromUri("http://localhost/").port(6543).build();
        ResourceConfig resourceConfig = new ElasticApplication();
        NettyHttpContainerProvider.createServer(baseUri, resourceConfig, true);
    }

    public synchronized Channel doStart() {
        if (server == null) {
            server = NettyHttpContainerProvider.createServer(this.conf.uri, resourceConfig, true);
            System.out.println(server.localAddress());
        }

        return server;
    }

    public synchronized void close() {
        if (server != null) {
            server.close();

            server = null;
        }
    }
}
