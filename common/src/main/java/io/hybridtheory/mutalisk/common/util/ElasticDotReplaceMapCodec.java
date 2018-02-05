package io.hybridtheory.mutalisk.common.util;


import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class ElasticDotReplaceMapCodec implements JsonSerializer<Map<String, String>>, JsonDeserializer<Map<String, String>> {

    @Override
    public Map<String, String> deserialize(JsonElement json, Type typeOfT,
                                           JsonDeserializationContext context) throws JsonParseException {
        Map<String, String> values = new HashMap<>();
        JsonObject jsonObject = (JsonObject) json;

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            values.put(entry.getKey().replace("$", "."), entry.getValue().getAsString());
        }

        return values;
    }

    @Override
    public JsonElement serialize(Map<String, String> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        for (Map.Entry<String, String> entry : src.entrySet()) {
            jsonObject.addProperty(entry.getKey().replace(".", "$"), entry.getValue());
        }

        return jsonObject;
    }
}
