package com.pcz.permission.util;

import com.sun.org.apache.bcel.internal.generic.FADD;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

/**
 * @author picongzhi
 */
@Slf4j
public class JsonMapper {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
    }

    public static <T> String objectToString(T src) {
        if (src == null) {
            return null;
        }

        try {
            return src instanceof String ? (String) src : objectMapper.writeValueAsString(src);
        } catch (Exception e) {
            log.warn("parse Object to String exception, error: {}", e);
            return null;
        }
    }

    public static <T> T stringToObject(String src, TypeReference<T> typeReference) {
        if (src == null || typeReference == null) {
            return null;
        }

        try {
            return typeReference.getType().equals(String.class) ?
                    (T) src : objectMapper.readValue(src, typeReference);
        } catch (Exception e) {
            log.warn("parse String to Object exception, string: {}, TypeReference: {}, error: {}",
                    src, typeReference.getType(), e);
            return null;
        }
    }
}
