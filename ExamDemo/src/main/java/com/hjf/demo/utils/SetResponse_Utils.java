package com.hjf.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetResponse_Utils {
    public static void setResponse(HttpServletResponse response , int status, String message, String details) throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("details", details);
        String jsonString = new ObjectMapper().writeValueAsString(map);
        response.getWriter().write(jsonString);
        response.setStatus(status);
    }
}
