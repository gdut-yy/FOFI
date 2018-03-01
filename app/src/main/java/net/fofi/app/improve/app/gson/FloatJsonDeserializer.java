package net.fofi.app.improve.app.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.fofi.app.util.TLog;

import java.lang.reflect.Type;

/**
 * Created by ZYY on 2018/2/26.
 */

public class FloatJsonDeserializer implements JsonDeserializer {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return json.getAsFloat();
        } catch (Exception e) {
            TLog.log("FloatJsonDeserializer-deserialize-error:" + (json != null ? json.toString() : ""));
            return 0F;
        }
    }
}
