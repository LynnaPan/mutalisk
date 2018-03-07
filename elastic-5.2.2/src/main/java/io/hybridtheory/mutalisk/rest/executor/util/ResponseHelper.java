package io.hybridtheory.mutalisk.rest.executor.util;


import com.google.gson.JsonObject;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import org.elasticsearch.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;

public class ResponseHelper {

    private final static Logger log = LoggerFactory.getLogger(ResponseHelper.class);

    public static JsonObject getJson(Response response) throws IOException {
        return StorageUtil.jsonParser.parse(new InputStreamReader(response.getEntity().getContent())).getAsJsonObject();
    }

    public static boolean responseAckCheck(Response response) {
        int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode >= 300) return false;

        JsonObject res = null;
        try {
            res = getJson(response);
        } catch (IOException e) {
            log.error("Unable to fetch response or format error", e);
            return false;
        }

        return res.get("acknowledged").getAsBoolean();
    }

    public static boolean response200Check(Response response) {
        return response.getStatusLine().getStatusCode() == 200;
    }

    public static boolean responseTimeoutCheck(Response response) {
        JsonObject res = null;
        try {
            res = getJson(response);
        } catch (IOException e) {
            log.error("Unable to parse response or format error", e);
            return false;
        }

        return res.get("timed_out").getAsBoolean();
    }

    public static int searchHitsFetch(Response response) {
        JsonObject res = null;
        try {
            res = getJson(response);
        } catch (IOException e) {
            log.error("Unable to parse response or format error", e);
            return -1;
        }

        boolean timeouted = res.get("timeed_out").getAsBoolean();

        if (timeouted) return -1;

        return res.get("hits").getAsJsonObject().get("total").getAsInt();
    }

    public static boolean documentCreatedCheck(Response response) {
        JsonObject res = null;
        try {
            res = getJson(response);
        } catch (IOException e) {
            log.error("Unable to parse response or format error", e);
            return false;
        }

        return res.get("created").getAsBoolean() && "created".equals(res.get("result").getAsString());
    }

    public static boolean documentDeletedCheck(Response response) {
        JsonObject res = null;
        try {
            res = getJson(response);
        } catch (IOException e) {
            log.error("Unable to parse response or format error", e);
            return false;
        }

        return res.get("found").getAsBoolean() && "deleted".equals(res.get("result").getAsString());
    }

    public static boolean bulkInsertCheck(Response response) {
        try {
            JsonObject res = getJson(response);
            if (res.get("errors").getAsBoolean()) return false;
            JsonObject createObj = res.get("items").getAsJsonObject().get("create").getAsJsonObject();
            return createObj.get("created").getAsBoolean() && "created".equals(createObj.get("result").getAsString());
        }  catch (IOException e) {
            log.error("Unable to parse response or format error", e);
            return false;
        }
    }
}
