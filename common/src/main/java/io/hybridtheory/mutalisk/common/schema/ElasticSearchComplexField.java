package io.hybridtheory.mutalisk.common.schema;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ElasticSearchComplexField extends ElasticSearchField {
    public ElasticSearchProperties properties;


    public ElasticSearchComplexField(String key,
                                     ElasticSearchFieldType type,
                                     boolean indexEnabled,
                                     ElasticSearchProperties properties) {
        super(key, type, indexEnabled);
        this.properties = properties;
    }

    @Override
    public JsonElement toJson() {
        JsonObject objField = new JsonObject();
        objField.addProperty("type", type.toString().toLowerCase());

        objField.add("properties", properties.toJson());


        return objField;
    }
}
