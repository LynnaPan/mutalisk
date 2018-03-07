package io.hybridtheory.mutalisk.rest.test;


import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;
import pl.allegro.tech.embeddedelasticsearch.PopularProperties;

import java.io.File;

public class ElasticEmbeddedServer {

    public static EmbeddedElastic buildLocalserver() {
        // build up one embedded elastic server
        EmbeddedElastic esServer = EmbeddedElastic.builder()
            .withElasticVersion("5.2.2")
            .withSetting(PopularProperties.TRANSPORT_TCP_PORT, 9300)
            .withSetting(PopularProperties.CLUSTER_NAME, "test")
            .withSetting(PopularProperties.HTTP_PORT, 9200)
            .withDownloadDirectory(new File("/tmp/job-monitoring/external"))
            .withCleanInstallationDirectoryOnStop(true)
            .build();

        return esServer;
    }
}
