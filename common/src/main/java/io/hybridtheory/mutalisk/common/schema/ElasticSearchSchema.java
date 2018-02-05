package io.hybridtheory.mutalisk.common.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import io.hybridtheory.mutalisk.common.schema.annotation.ElasticSearchIndex;
import io.hybridtheory.mutalisk.common.schema.annotation.ElasticSearchMeta;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticSearchSchema {

    protected static final Map<Class, ElasticSearchSchema> maps = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(ElasticSearchSchema.class);
    public String index;
    public String type;
    public String[] id;
    public Map<String, Object> settings = new HashMap<>();
    public ElasticSearchProperties properties;
    public Class clz;

    public ElasticSearchSchema(Class clz) {
        this.clz = clz;
    }

    public static ElasticSearchSchema fromClass(Class clz) {
        return fromClass(clz, false);
    }

    public static ElasticSearchSchema fromClass(Class clz, boolean parseField) {
        ElasticSearchMeta ei = (ElasticSearchMeta) clz.getAnnotation(ElasticSearchMeta.class);

        ElasticSearchSchema schema = new ElasticSearchSchema(clz);

        if (ei == null) {
            log.warn("{} contains no @ElasticSearchMeta, use class name as index name, 'data' as type", clz.getName());

            schema.index = clz.getCanonicalName();
            schema.type = "data";
        } else {
            schema.index = ei.index();
            schema.type = ei.type();
            schema.id = ei.id();
        }

        if (parseField) {
            ElasticSearchProperties properties = new ElasticSearchProperties();
            schema.properties = buildProperties(clz, properties);
        }

        return schema;
    }

    protected static ElasticSearchProperties buildProperties(Class clz, ElasticSearchProperties properties) {
        // scan fields
        Field[] fields = clz.getDeclaredFields();

        for (Field f : fields) {
            f.setAccessible(true);

            // if transient is appended, ignore the field
            if (Modifier.isTransient(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
                continue;
            }

            // fetch target name
            // - if SerializedName is defined with name provided, use provided name
            // - else use field name
            SerializedName serializedName = f.getAnnotation(SerializedName.class);
            String target;
            if (serializedName != null && !serializedName.value().equals("")) {
                target = serializedName.value();
            } else {
                target = f.getName();
            }

            ElasticSearchIndex index = f.getAnnotation(ElasticSearchIndex.class);
            boolean needIndex = false;
            ElasticSearchFieldType type = null;
            Class fclzType = f.getType();

            if (index == null || index.type() == ElasticSearchFieldType.NONE) {
                needIndex = false;
                type = getFieldType(f);
            } else {
                needIndex = true;
                type = index.type();

                if (type == ElasticSearchFieldType.AUTO) {
                    type = getFieldType(f);
                }
            }

            if (type == ElasticSearchFieldType.NONE) {
                //@TODO should report warning to show unrecognized field mapping
                log.warn("Unreconized Field : %s -> %s", f.getName(), f.getType());
            } else if (type == ElasticSearchFieldType.OBJECT) {
                ElasticSearchProperties subProperties = buildProperties(fclzType, new ElasticSearchProperties());
                properties.fields.add(
                    new ElasticSearchComplexField(target, ElasticSearchFieldType.OBJECT, needIndex, subProperties));
            } else if (type == ElasticSearchFieldType.NESTED) {
                ElasticSearchProperties subProperties = buildProperties(fclzType, new ElasticSearchProperties());
                properties.fields.add(
                    new ElasticSearchComplexField(target, ElasticSearchFieldType.NESTED, needIndex, subProperties));
            } else {
                properties.fields.add(new ElasticSearchField(target, type, true));
            }
        }

        Class superClz = clz.getSuperclass();
        if (superClz == null
            || superClz.equals(Object.class)
            || superClz.isInterface()
            || List.class.isAssignableFrom(superClz)
            || Map.class.isAssignableFrom(superClz)) {
            return properties;
        }
        // check up parent
        return buildProperties(clz.getSuperclass(), properties);
    }

    private static ElasticSearchFieldType getFieldType(Field f) {
        Class clz = f.getType();

        if (clz.isArray()) {
            clz = clz.getComponentType();
        }

        if (List.class.isAssignableFrom(clz)) {
            clz = (Class) StorageUtil.getFieldGenericClz(f)[0];
        }

        if (clz.equals(Byte.TYPE)) {
            return ElasticSearchFieldType.BYTE;
        } else if (clz.equals(Short.TYPE)) {
            return ElasticSearchFieldType.SHORT;
        } else if (clz.equals(Integer.TYPE)) {
            return ElasticSearchFieldType.INTEGER;
        } else if (clz.equals(Long.TYPE)) {
            return ElasticSearchFieldType.LONG;
        } else if (clz.equals(Float.TYPE)) {
            return ElasticSearchFieldType.FLOAT;
        } else if (clz.equals(Double.TYPE)) {
            return ElasticSearchFieldType.DOUBLE;
        } else if (clz.equals(String.class) || clz.isEnum()) {
            // @NOTE @TODO if you want to txt, you need explicitly define it
            return ElasticSearchFieldType.KEYWORD;
        } else if (clz.equals(Boolean.TYPE)) {
            return ElasticSearchFieldType.BOOLEAN;
        } else if (clz.equals(Date.class)) {
            return ElasticSearchFieldType.DATE;
        } else if (Map.class.isAssignableFrom(clz)) {
            return ElasticSearchFieldType.NESTED;
        } else if (!clz.isPrimitive()) {
            return ElasticSearchFieldType.OBJECT;
        }

        return ElasticSearchFieldType.NONE;
    }

    public static ElasticSearchSchema getOrBuild(Class clz) {
        if (maps.containsKey(clz)) {
            return maps.get(clz);
        } else {
            ElasticSearchSchema schema = ElasticSearchSchema.fromClass(clz, true);
            maps.put(clz, schema);

            return schema;
        }
    }

    public String toTypeMapping() {
        JsonObject mappingContent = new JsonObject();
        JsonObject mappingPro = new JsonObject();

        mappingPro.add("properties", this.properties.toJson());
        mappingContent.add(this.type, mappingPro);

        return mappingContent.toString();
    }

    public String getId(JsonObject jobj) {
        // quick reference for length = 1 and length = 2
        if (id.length == 1) {
            JsonElement element = jobj.get(id[0]);
            return element.getAsString();
        } else if (id.length == 2) {
            JsonElement e1 = jobj.get(id[0]);
            JsonElement e2 = jobj.get(id[1]);

            return e1.getAsString() + "_" + e2.getAsString();
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append(jobj.get(id[0]).getAsString());

            for (int i = 1; i < id.length; i++) {
                builder.append("_");
                builder.append(jobj.get(id[i]).getAsString());
            }

            return builder.toString();
        }
    }
}
