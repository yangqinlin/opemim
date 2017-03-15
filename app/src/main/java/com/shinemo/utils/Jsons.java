package com.shinemo.utils;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;


public final class Jsons {
    public static final Gson GSON = new Gson();

    public static synchronized String toJson(Object bean) {
        return GSON.toJson(bean);
    }

    public static synchronized <T> T fromJson(String json, Class<T> clazz) {
        try{
            return GSON.fromJson(json, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized <T> T fromJson(String json, Type type) {
        try{
            return GSON.fromJson(json, type);
        }catch (Exception e){
        }
        return null;

    }

    public static boolean mayJson(String json) {
        if (TextUtils.isEmpty(json)) return false;
        if (json.charAt(0) == '{' && json.charAt(json.length() - 1) == '}') return true;
        if (json.charAt(0) == '[' && json.charAt(json.length() - 1) == ']') return true;
        return false;
    }

    public static String toJson(Map<String, String> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder(64 * map.size());
        sb.append('{');
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        if (it.hasNext()) {
            append(it.next(), sb);
        }
        while (it.hasNext()) {
            sb.append(',');
            append(it.next(), sb);
        }
        sb.append('}');
        return sb.toString();
    }

    private static void append(Map.Entry<String, String> entry, StringBuilder sb) {
        String key = entry.getKey(), value = entry.getValue();
        if (value == null) value = "";
        sb.append('"').append(key).append('"');
        sb.append(':');
        sb.append('"').append(value).append('"');
    }
}
