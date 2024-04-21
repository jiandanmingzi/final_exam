package com.hjf.demo.contoller.Servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjf.demo.Service.CourseService;
import com.hjf.demo.Service.Impl.CourseServiceImpl;
import com.hjf.demo.Service.Impl.UserServiceImpl;
import com.hjf.demo.Service.UserService;
import com.hjf.demo.entity.Course;
import com.hjf.demo.entity.User;
import com.hjf.demo.utils.JSON_Utils;
import com.hjf.demo.utils.JWT_Utils;
import com.hjf.demo.utils.SetResponse_Utils;
import io.jsonwebtoken.Claims;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@WebServlet("user/userAndCourseServlet")
public class UserAndCourseServlet extends BaseServlet{
    private final ReentrantLock lock = new ReentrantLock();
    private UserService userService = new UserServiceImpl();
    public CourseService courseService = new CourseServiceImpl();
    public void showMyInfo(HttpServletRequest request, HttpServletResponse response) throws SQLException, InterruptedException, IOException {
        String authHeader = request.getHeader("Authorization");
        String account = (String) JWT_Utils.getClaims(authHeader.substring(7)).get("account");
        User user = userService.showUserInfo(account);
        if (user != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("qq",user.getQq());
            params.put("introduction",user.getIntroduction());
            params.put("email",user.getEmail());
            params.put("account",user.getAccount());
            params.put("admin",user.isAdmin());
            String jsonString = new ObjectMapper().writeValueAsString(params);
            SetResponse_Utils.setResponse(response,200,"success",jsonString);
        }else {
            SetResponse_Utils.setResponse(response,500,"false","服务器异常");
            response.sendRedirect("/login.html");
        }
    }

    public void addCourse(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status;
        String message;
        String details;
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        int id = (int)claims.get("id");
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        if (admin) {
            String startTime = rootNode.get("startTime").asText();
            String endTime = rootNode.get("endTime").asText();
            String introduction = rootNode.get("introduction").asText();
            String teacherName = rootNode.get("teacherName").asText();
            String courseName = rootNode.get("courseName").asText();
            int maxStudent = rootNode.get("maxStudent").asInt();
            int teacherId = rootNode.get("teacherId").asInt();
            boolean ready = rootNode.get("ready").asBoolean();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            if (courseService.checkName(courseName)) {
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    if (courseService.checkName(courseName)) {
                        if (courseService.creatCourse(ready, courseName, maxStudent, teacherName, introduction, LocalDateTime.parse(startTime, formatter), LocalDateTime.parse(endTime, formatter), teacherId)) {
                            status = 200;
                            message = "true";
                            details = "添加成功";
                        } else {
                            status = 400;
                            message = "false";
                            details = "添加失败";
                        }
                    }else{
                        status = 400;
                        message = "false";
                        details = "课程名已存在";
                    }
                    lock.unlock();
                }else {
                    status = 500;
                    message = "false";
                    details = "服务器繁忙";
                }
            }else{
                status = 400;
                message = "false";
                details = "课程名已存在";
            }
        }else{
            int courseId = rootNode.get("courseId").asInt();
            Course course = courseService.getCourse(courseId);
            if (course != null) {
                if (courseService.addStudentToCourse(id, course)){
                    status = 200;
                    message = "true";
                    details = "添加成功";
                }else{
                    status = 400;
                    message = "false";
                    details = "人数已满";
                }
            }else{
                status = 400;
                message = "false";
                details = "课程不存在";
            }

        }
        SetResponse_Utils.setResponse(response,status,message,details);
    }
}
