package com.typeface.notification.util;

import com.typeface.notification.model.ProjectPageDetail;
import org.json.JSONObject;

import java.io.*;
import java.util.Base64;

public class ObjectSerializationUtil {

    public static String serializeObjectToString(JSONObject object) {
        byte[] serializedData = object.toString().getBytes();
        return Base64.getEncoder().encodeToString(serializedData);
    }

    public static JSONObject deserializeStringToObject(String serializedString) throws IOException, ClassNotFoundException {
        byte[] serializedData = Base64.getDecoder().decode(serializedString);
        String jsonStr = new String(serializedData);
        return new JSONObject(jsonStr);
    }
}
