package com.reliaquest.api.util;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BuildResponseEntityUtil {
    public static ResponseEntity<Map<String, Object>> buildResponseEntity(Object data) {
        Map<String, Object> response = new HashMap<>();
        if (data instanceof Map<?, ?>) {
            Map<String, Object> dataMap = (Map<String, Object>) data;
            if ("ERROR".equals(dataMap.get("ERROR"))) {
                response.put("error", data);
            } else {
                response.put("data", data);
            }
        } else {
            response.put("data", data);
        }
        response.put("traceId", MDC.get("traceId"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
