package io.hybridtheory.mutalisk.common.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.api.GsonJsonable;

import java.util.ArrayList;
import java.util.List;

public class ElasticSearchProperties implements GsonJsonable {
    public List<ElasticSearchField> fields = new ArrayList<>();

    @Override
    public JsonElement toJson() {
        JsonObject proJsonObj = new JsonObject();

        for (ElasticSearchField field : fields) {
            proJsonObj.add(field.key, field.toJson());
        }

        return proJsonObj;
    }
}
