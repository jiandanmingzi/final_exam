package com.hjf.demo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SetResponse_Utils {
    public static void setResponse(HttpServletResponse response , int status, String message, Object details) throws IOException {
        response.setContentType("application/json");
        Map<String,Object> map = new HashMap<>();
        map.put("status", status);
        map.put("message", message);
        map.put("details", details);
        System.out.println(status);
        System.out.println(message);
        System.out.println(details);
        String jsonString = new ObjectMapper().writeValueAsString(map);
        response.getWriter().write(jsonString);
        response.setStatus(status);
    }
}
