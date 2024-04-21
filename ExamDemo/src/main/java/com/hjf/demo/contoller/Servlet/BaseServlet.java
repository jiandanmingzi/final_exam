package com.hjf.demo.contoller.Servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.hjf.demo.utils.JSON_Utils;
import com.hjf.demo.utils.SetResponse_Utils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class BaseServlet extends HttpServlet {
    private final Logger LOGGER = Logger.getLogger(BaseServlet.class.toString());

    private void invokeMethod(HttpServletRequest req, HttpServletResponse resp, String methodName) throws IOException {
        Class<? extends BaseServlet> actionClass = this.getClass();
        Method method;
        try {
            method = actionClass.getMethod(methodName,HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this,req, resp);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            if (e.getClass() == NoSuchMethodException.class)
                LOGGER.severe("Method not found:" + e.getMessage());
            else
                LOGGER.severe("Error invoking method:" + e.getMessage());
            SetResponse_Utils.setResponse(resp ,500, "Error", e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String methodName = req.getParameter("method");
        invokeMethod(req, resp, methodName);
    }



    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(req);
        String methodName = rootNode.get("method").asText();
        invokeMethod(req, resp, methodName);
    }
}
