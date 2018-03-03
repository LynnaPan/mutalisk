package io.hybridtheory.mutalisk.common;

import io.hybridtheory.mutalisk.common.schema.annotation.ElasticSearchMeta;

import java.util.Date;
import java.util.List;

/**
 * Created by lynna on 2018/2/22.
 */
@ElasticSearchMeta(index = "Production")
public class Production {
    public int uuid;
    public String brand;
    public String name;
    public String country;
    public String function;
    public String type;
    public String manufacturer;
    public List<String> comments;
    public double price;
    public Date produceDate;
    public boolean homemade;
    public int stock;
    public long bought;
}

