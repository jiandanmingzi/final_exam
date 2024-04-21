package com.hjf.demo.contoller.Servlet;

import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@WebServlet("/userServlet")
public class UserServlet extends BaseServlet{
    private static final ReentrantLock lock = new ReentrantLock();
    UserService userService = new UserServiceImpl();
    public void changePassword(HttpServletRequest request, HttpServletResponse response) throws NoSuchAlgorithmException, IOException, SQLException, InterruptedException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String account = rootNode.get("account").asText();
        String newPassword = Encrypt_Utils.encrypt(rootNode.get("newPassword").asText());
        if (userService.changePassword(account, newPassword)){
            SetResponse_Utils.setResponse(response ,300, "success", "修改成功");
            response.sendRedirect("/login.html");
        }else{
            SetResponse_Utils.setResponse(response ,400, "false", "修改失败");
        }
    }
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException, NoSuchAlgorithmException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String account = rootNode.get("account").asText();
        String password = Encrypt_Utils.encrypt(rootNode.get("password").asText());
        User user = userService.login(account, password);
        if (user == null) {
            SetResponse_Utils.setResponse(response ,400, "false", "账号或密码错误");
        }else{
            Map<String , Object> map = new TreeMap<>();
            map.put("id", user.getId());
            map.put("account", user.getAccount());
            map.put("admin", user.isAdmin());
            String token = JWT_Utils.getToken(map);
            SetResponse_Utils.setResponse(response ,300, "success", token);
            response.sendRedirect("/home.html");
        }
    }

    public void signup(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException, NoSuchAlgorithmException {
        int status;
        String message;
        String details;
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String account = rootNode.get("account").asText();
        String password = Encrypt_Utils.encrypt(rootNode.get("password").asText());
        String username = rootNode.get("username").asText();
        String email = rootNode.get("email").asText();
        boolean admin = rootNode.get("admin").asBoolean();
        if (userService.checkData("account", account) != 0) {
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                if (userService.checkData("account", account) != 0) {
                    if (userService.signup(account, username, password, email, admin)) {
                        status = 300;
                        message = "success";
                        details = "注册成功";
                        response.sendRedirect("/login.html");
                    } else {
                        status = 500;
                        message = "false";
                        details = "注册失败";
                    }
                }else {
                    status = 400;
                    message = "false";
                    details = "账号已存在";
                }
                lock.unlock();
            }else {
                status = 500;
                message = "false";
                details = "服务器繁忙";
            }
        }else {
            status = 400;
            message = "false";
            details = "账号已存在";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void checkAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String account = rootNode.get("account").asText();
        if (userService.checkData("account", account) != 0){
            SetResponse_Utils.setResponse(response ,400, "false", "账号已存在");
        }else {
            SetResponse_Utils.setResponse(response ,200, "success", "账号不存在");
        }
    }

    public void checkEmail(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String email = rootNode.get("email").asText();
        if (userService.checkData("email",email) != 0){
            SetResponse_Utils.setResponse(response ,400, "false", "邮箱已存在");
        }else {
            SetResponse_Utils.setResponse(response ,200, "success", "邮箱已存在");
        }
    }
}
