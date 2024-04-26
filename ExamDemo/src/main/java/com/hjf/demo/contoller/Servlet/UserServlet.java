package com.hjf.demo.contoller.Servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.hjf.demo.Service.AuthenticateService;
import com.hjf.demo.Service.Impl.AuthenticateServiceImpl;
import com.hjf.demo.Service.Impl.UserServiceImpl;
import com.hjf.demo.Service.UserService;
import com.hjf.demo.entity.User;
import com.hjf.demo.utils.Encrypt_Utils;
import com.hjf.demo.utils.JSON_Utils;
import com.hjf.demo.utils.JWT_Utils;
import com.hjf.demo.utils.SetResponse_Utils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@WebServlet("/userServlet")
public class UserServlet extends BaseServlet{
    private static final ReentrantLock lock = new ReentrantLock();
    private final UserService userService = new UserServiceImpl();
    private final AuthenticateService authenticateService = new AuthenticateServiceImpl();
    public void changePassword(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, IOException, SQLException, InterruptedException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String account = rootNode.get("account").asText();
        String newPassword = Encrypt_Utils.encrypt(rootNode.get("newPassword").asText());
        if (userService.changePassword(account, newPassword)){
            SetResponse_Utils.setResponse(response ,300, "success", "修改成功");
            response.sendRedirect("/login.html");
        }else{
            SetResponse_Utils.setResponse(response ,200, "false", "修改失败");
        }
    }
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException, NoSuchAlgorithmException {
        int status = 200;
        String message = "false";
        Object details = "用户名或密码错误";
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String account = rootNode.get("account").asText();
        String password = Encrypt_Utils.encrypt(rootNode.get("password").asText());
        User user = userService.login(account, password);
        System.out.println(22);
        if (user == null) {
            System.out.println(44);
        }else{
            System.out.println(33);
            Map<String , Object> map = new TreeMap<>();
            map.put("id", String.valueOf(user.getId()));
            System.out.println(user.getId());
            map.put("admin", String.valueOf(user.isAdmin()));
            System.out.println(user.isAdmin());
            map.put("authenticated", String.valueOf(user.isAuthenticated()));
            System.out.println(user.isAuthenticated());
            details = JWT_Utils.getToken(map);
            message = "success";
            status = 200;
        }
        SetResponse_Utils.setResponse(response ,status, message, details);
    }

    public void authentication(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException, NoSuchAlgorithmException {
        int status = 200;
        String message = "false";
        Object details = "数据不完整";
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        JsonNode admin = rootNode.get("admin");
        JsonNode realName = rootNode.get("realName");
        JsonNode introduction = rootNode.get("introduction");
        List<JsonNode> nodeList = new ArrayList<>();
        nodeList.add(admin);
        nodeList.add(realName);
        nodeList.add(introduction);
        if(JSON_Utils.checkNode(nodeList)){
            System.out.println(22);
            int userId = Integer.parseInt((String) JWT_Utils.getClaims(request.getHeader("Authorization").substring(7)).get("id"));
            System.out.println(userId);
            User user = userService.showUserInfo(userId);
            if (!user.isAuthenticated()) {
                Map<String , Object> info = new TreeMap<>();
                if (admin.asBoolean()) {
                    JsonNode teach = rootNode.get("teach");
                    JsonNode identifier = rootNode.get("identifier");
                    nodeList.add(teach);
                    nodeList.add(identifier);
                    if (JSON_Utils.checkNode(nodeList)) {
                        if (identifier.asText().equals("I am a teacher")) {
                            info.put("teach", teach.asText());
                            info.put("realName", realName.asText());
                        } else {
                            message = "false";
                            details = "认证不通过";
                        }
                    }
                } else {
                    JsonNode studentId = rootNode.get("studentId");
                    JsonNode college = rootNode.get("college");
                    JsonNode major = rootNode.get("major");
                    JsonNode grade = rootNode.get("grade");
                    JsonNode Class = rootNode.get("clazz");
                    nodeList.add(studentId);
                    nodeList.add(college);
                    nodeList.add(major);
                    nodeList.add(grade);
                    nodeList.add(Class);
                    if (JSON_Utils.checkNode(nodeList)) {
                        info.put("realName", realName.asText());
                        info.put("studentId", studentId.asText());
                        info.put("college", college.asText());
                        info.put("major", major.asText());
                        info.put("grade", grade.asText());
                        info.put("Class", Class.asText());
                    }
                }
                if (!info.isEmpty()){
                    if (authenticateService.addInfo(userId, admin.asBoolean(), info)) {
                        userService.changeData("authenticated", true, userId);
                        userService.changeData("admin", admin.asBoolean(), userId);
                        userService.changeData("introduction", introduction.asText(), userId);
                        Map<String, Object> map = new TreeMap<>();
                        map.put("id", userId);
                        map.put("admin", admin.asBoolean());
                        map.put("authenticated", true);
                        details = JWT_Utils.getToken(map);
                        message = "success";
                    }
                }
            }else {
                details = "不可重复完成实名认证";
            }
        }
        SetResponse_Utils.setResponse(response ,status, message, details);
    }

    public void signup(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException, NoSuchAlgorithmException {
        int status = 200;
        String message = "false";
        Object details = "注册失败";
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String account = rootNode.get("account").asText();
        String password = Encrypt_Utils.encrypt(rootNode.get("password").asText());
        String username = rootNode.get("username").asText();
        String email = rootNode.get("email").asText()+"@qq.com";
        if (userService.checkData("account", account) == 0) {
                if (userService.checkData("account", account) == 0) {
                    if (userService.signup(account, username, password, email, false)) {
                        System.out.println(101);
                        status = 200;
                        message = "success";
                        details = "注册成功";
                    } else {
                        status = 500;
                        message = "false";
                    }
                }else {
                    status = 200;
                    message = "false";
                    details = "账号已存在";
                }
        }else {
            status = 200;
            message = "false";
            details = "账号已存在";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void checkAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String account = rootNode.get("account").asText();
        if (userService.checkData("account", account) != 0){
            SetResponse_Utils.setResponse(response ,200, "false", "账号已存在");
        }else {
            SetResponse_Utils.setResponse(response ,200, "success", "账号不存在");
        }
    }

    public void checkEmail(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String email = rootNode.get("email").asText()+"@qq.com";
        if (userService.checkData("email",email) != 0){
            SetResponse_Utils.setResponse(response ,200, "false", "邮箱已存在");
        }else {
            SetResponse_Utils.setResponse(response ,200, "success", "邮箱不存在");
        }
    }
}
