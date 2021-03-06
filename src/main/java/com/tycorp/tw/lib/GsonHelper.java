package com.tycorp.tw.lib;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

public class GsonHelper {

    public static GsonBuilder builder = new GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeToJsonSerializer());

    public static JsonElement createJsonElement(Object obj) {
        return new JsonParser().parse(builder.create().toJson(obj));
    }

    public static JsonObject getJsonObject() {
        return new JsonObject();
    }

    public static <T> List<T> getList(String jsonArray, Class<T> clazz) {
        Type typeOfT = TypeToken.getParameterized(List.class, clazz).getType();
        return new Gson().fromJson(jsonArray, typeOfT);
    }

}
