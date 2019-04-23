package com.yingzi;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private final static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JsonUtil() {
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    public static String ObjectToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("[JsonUtil.ObjectToJson] error:{}", e);
        }

        return null;
    }

    public static <T> T jsonToObject(String jsonStr, Class<T> clazz) {
        try {
            if (StringUtils.isEmpty(jsonStr)) {
                logger.info("[JsonUtil.jsonToObject]jsonStr is empty");
                return null;
            } else {
                return objectMapper.readValue(jsonStr, clazz);
            }
        } catch (Exception e) {
            logger.error("[JsonUtil.jsonToObject]{}, error:{}", jsonStr, e);
        }

        return null;
    }

    public static <T> Map<String, Object> jsonToMap(String jsonStr) {
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (Exception e) {
            logger.error("[JsonUtil.jsonToMap] error:{}", e);
        }

        return null;
    }

    public static <T> Map<String, T> jsonToMap(String jsonStr, Class<T> clazz) {
        try {
            Map<String, Map<String, Object>> map = objectMapper.readValue(jsonStr,
                    new TypeReference<Map<String, T>>() {
                    });
            Map<String, T> result = new HashMap<String, T>();
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), map2pojo(entry.getValue(), clazz));
            }
            return result;
        } catch (Exception e) {
            logger.error("[JsonUtil.jsonToMap] error:{}", e);
        }

        return null;
    }

    public static <T> List<T> jsonToList(String jsonArrayStr, Class<T> clazz) {
        try {
            List<Map<String, Object>> list = objectMapper.readValue(jsonArrayStr,
                    new TypeReference<List<T>>() {
                    });
            List<T> result = new ArrayList<T>();
            for (Map<String, Object> map : list) {
                result.add(map2pojo(map, clazz));
            }
            return result;
        } catch (Exception e) {
            logger.error("[JsonUtil.jsonToList] error:{}", e);
        }

        return null;
    }

    public static <T> T map2pojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

}
