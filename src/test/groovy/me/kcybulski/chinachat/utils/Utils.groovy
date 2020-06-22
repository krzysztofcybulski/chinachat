package me.kcybulski.chinachat.utils

import com.fasterxml.jackson.databind.ObjectMapper

class Utils {

    static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()

    static Map<String, Object> asMap(String string) {
        return OBJECT_MAPPER.readValue(string, Map<String, Object>)
    }

    static String asString(Map<String, Object> map) {
        return OBJECT_MAPPER.writeValueAsString(map)
    }

}
