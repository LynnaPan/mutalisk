package io.hybridtheory.mutalisk.common.util;


import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class StorageUtil {

    public final static Gson gson = new Gson();
    public final static JsonParser jsonParser = new JsonParser();

    public static Type[] getFieldGenericClz(Field field) {
        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
        return parameterizedType.getActualTypeArguments();
    }

    public static String toObjectFieldString(Object object) {
        Field[] fields = object.getClass().getFields();
        StringBuilder builder = new StringBuilder();

        for (Field field : fields) {
            try {
                if (field.get(object) != null) {
                    builder.append(field.getName() + " = " + field.get(object).toString() + "\n");
                } else {
                    builder.append(field.getName() + " = null\n");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }
}
