package io.hybridtheory.mutalisk.webservice.server;


import io.netty.channel.Channel;
import org.glassfish.jersey.netty.httpserver.NettyHttpContainerProvider;
import org.glassfish.jersey.server.ResourceConfig;

public class WebServer {
    private WebServerConf conf;
    private ResourceConfig resourceConfig;
    private Channel server;

    public WebServer(WebServerConf conf, ResourceConfig resourceConfig) {
        this.conf = conf;
        this.resourceConfig = resourceConfig;
    }

    public synchronized Channel doStart() {
        if (server != null) {
            server = NettyHttpContainerProvider.createServer(this.conf.uri, resourceConfig, false);
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
