package com.hjf.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class JSON_Utils {
    public static JsonNode ReadJsonInRequest(ServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine())!= null) {
            stringBuilder.append(line).append("\n");
        }
        String requestBody = stringBuilder.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(requestBody);
    }

}
