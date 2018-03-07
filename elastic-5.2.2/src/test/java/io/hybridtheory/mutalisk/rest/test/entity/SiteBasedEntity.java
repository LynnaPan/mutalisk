package io.hybridtheory.mutalisk.rest.test.entity;


import io.hybridtheory.mutalisk.common.entity.ElasticBaseEntity;
import io.hybridtheory.mutalisk.common.schema.annotation.ElasticSearchIndex;

public abstract class SiteBasedEntity extends ElasticBaseEntity {

    @ElasticSearchIndex
    protected String site = "";

    public String getSite() {
        return site;
    }

    public SiteBasedEntity setSite(String site) {
        this.site = site;

        return this;
    }
}
