package com.hjf.demo.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class JSON_Utils {
    public static JsonNode ReadJsonInRequest(ServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine())!= null) {
            stringBuilder.append(line).append("\n");
        }
        String requestBody = stringBuilder.toString();
        return new ObjectMapper().readTree(requestBody);
    }

    public static boolean checkNode(List<JsonNode> list){
        int num = 0;
        for (JsonNode node : list) {
            System.out.println(num++);
            if (node == null || node.isNull()) {
                return false;
            }
        }
        return true;
    }
}
