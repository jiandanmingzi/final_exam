package com.hjf.demo.contoller.Servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.hjf.demo.Dao.Impl.User_SectionDaoImpl;
import com.hjf.demo.Dao.User_SectionDao;
import com.hjf.demo.Service.*;
import com.hjf.demo.Service.Impl.*;
import com.hjf.demo.entity.*;
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
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@WebServlet("/user/userAndCourseServlet")
public class UserAndCourseServlet extends BaseServlet{
    private final ReentrantLock lock = new ReentrantLock();
    private final UserService userService = new UserServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final ExerciseService exerciseService = new ExerciseServiceImpl();
    private final PartService partService = new PartServiceImpl();
    private final SectionService sectionService = new SectionServiceImpl();
    private final User_ExerService userExerService = new User_ExerServiceImpl();
    private final AuthenticateService authenticateService = new AuthenticateServiceImpl();
    private final User_SectionDao userSectionDao = new User_SectionDaoImpl();
    public void showMyInfo(HttpServletRequest request, HttpServletResponse response) throws SQLException, InterruptedException, IOException {
        String authHeader = request.getHeader("Authorization");
        int id = (int) JWT_Utils.getClaims(authHeader.substring(7)).get("id");
        User user = userService.showUserInfo(id);
        if (user != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("username", user.getUsername());
            params.put("qq",user.getQq());
            params.put("introduction",user.getIntroduction());
            params.put("email",user.getEmail());
            params.put("id",user.getAccount());
            params.put("admin",user.isAdmin());
            params.put("account", user.getAccount());
            params.putAll(authenticateService.getInfo(id, user.isAdmin()));
            SetResponse_Utils.setResponse(response,200,"success",params);
        }else {
            SetResponse_Utils.setResponse(response,500,"false","服务器异常");
            response.sendRedirect("/login.html");
        }
    }

    public void changeAccountInfo(HttpServletRequest request, HttpServletResponse response) throws SQLException, InterruptedException, IOException {
        int status = 200;
        String message = "false";
        Object details = "修改失败";
        int id = (int) JWT_Utils.getClaims(request.getHeader("Authorization").substring(7)).get("id");
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        JsonNode email = rootNode.get("newEmail");
        JsonNode qq = rootNode.get("newQQ");
        JsonNode introduction = rootNode.get("newIntroduction");
        JsonNode username = rootNode.get("newName");
        List<JsonNode> list = new ArrayList<>();
        list.add(email);
        list.add(qq);
        list.add(introduction);
        list.add(username);
        if (JSON_Utils.checkNode(list)){
            if(userService.changeInfo(id, email.asText(), qq.asText(), introduction.asText(), username.asText())){
                message = "success";
                details = "修改成功";
            }
        }else {
            details = "数据不完善";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void changeSelfInfo(HttpServletRequest request, HttpServletResponse response) throws SQLException, InterruptedException, IOException {
        int status = 200;
        String message = "false";
        Object details = "修改失败";
        int id = (int) JWT_Utils.getClaims(request.getHeader("Authorization").substring(7)).get("id");
        boolean admin = (boolean) JWT_Utils.getClaims(request.getHeader("Authorization").substring(7)).get("admin");
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        JsonNode realName = rootNode.get("newRealName");
        List<JsonNode> list = new ArrayList<>();
        list.add(realName);
        Map<String, Object> info = new HashMap<>();
        if (admin){
            JsonNode teach = rootNode.get("newTeach");
            list.add(teach);
            if (JSON_Utils.checkNode(list)){
                info.put("teach", teach.asText());
                info.put("realName", realName.asText());
            }else {
                details = "数据不完整";
            }
        }else {
            JsonNode studentId = rootNode.get("newStudentId");
            JsonNode college = rootNode.get("newCollege");
            JsonNode major = rootNode.get("newMajor");
            JsonNode grade = rootNode.get("newGrade");
            JsonNode clazz = rootNode.get("newClazz");
            list.add(major);
            list.add(grade);
            list.add(clazz);
            list.add(studentId);
            list.add(college);
            if (JSON_Utils.checkNode(list)){
                info.put("realName", realName.asText());
                info.put("major", major.asText());
                info.put("grade", grade.asText());
                info.put("class", clazz.asText());
                info.put("studentId", studentId.asText());
                info.put("college", college.asText());
            }else {
                details = "数据不完整";
            }
        }
        if (!info.isEmpty()){
            if (authenticateService.changeInfo(id, admin, info)){
                message = "success";
                details = "修改成功";
            }
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void showMyCourse(HttpServletRequest request, HttpServletResponse response) throws SQLException, InterruptedException, IOException {
        int status = 200;
        String message = "false";
        Object details = "查询失败";
        int id = (int) JWT_Utils.getClaims(request.getHeader("Authorization").substring(7)).get("id");
        boolean admin = (boolean) JWT_Utils.getClaims(request.getHeader("Authorization").substring(7)).get("admin");
        List<Map<String, Object>> maps = new ArrayList<>();
        if (admin){
            List<Course> list = courseService.showTeacherCourse(id);
            for(Course course : list){
                maps.add(CourseServlet.CourseToMap(course));
            }
        }else {
            List<Course> list = courseService.showStudentCourse(id);
            for(Course course : list){
                maps.add(CourseServlet.CourseToMap(course));
            }
        }
        message = "success";
        details = maps;
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void setCourseUnready(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "修改失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (!admin){
            details = "权限不足";
        }else {
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode id = rootNode.get("id");
            JsonNode ready = rootNode.get("ready");
            List<JsonNode> nodes = new ArrayList<>();
            if (JSON_Utils.checkNode(nodes)) {
                if (courseService.setCourseUnready(id.asInt(), ready.asBoolean())) {
                    status = 200;
                    message = "success";
                    details = "修改成功";
                }
            }
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void deletePart(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status =400 ;
        String message ="false";
        Object details = "删除失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (!admin){
            details = "权限不足";
        }else {
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode id = rootNode.get("id");
            List<JsonNode> nodes = new ArrayList<>();
            nodes.add(id);
            if (JSON_Utils.checkNode(nodes)){
                if (partService.deletePart(id.asInt())) {
                    status = 200;
                    message = "success";
                    details = "删除成功";
                }
            }
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void addPart(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "数据不完整";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin) {
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode courseId = rootNode.get("courseId");
            JsonNode sort = rootNode.get("sort");
            JsonNode partName = rootNode.get("partName");
            List<JsonNode> list = new ArrayList<>();
            list.add(courseId);
            list.add(sort);
            list.add(partName);
            if (JSON_Utils.checkNode(list)) {
                if (partService.addPart(courseId.asInt(), partName.asText(), sort.asInt())) {
                    status = 200;
                    message = "success";
                    details = "添加成功";
                } else details = "添加失败，修改前数据已刷新";
            }
        }else{
            details = "权限不足";
        }
        SetResponse_Utils.setResponse(response, status, message ,details);
    }

    public void changePartName(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status =400 ;
        String message ="false";
        Object details = "修改失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin){
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode id = rootNode.get("id");
            JsonNode partName = rootNode.get("partName");
            List<JsonNode> list = new ArrayList<>();
            list.add(id);
            list.add(partName);
            if (JSON_Utils.checkNode(list)) {
                if (partService.changePartName(id.asInt(), partName.asText())) {
                    status = 200;
                    message = "success";
                    details = "修改成功";
                }
            }
        }else {
            details = "权限不足";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void addSection(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "数据不完整";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        int teacherId = Integer.parseInt(claims.get("id").toString());
        if (admin) {
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode courseId = rootNode.get("courseId");
            JsonNode sort = rootNode.get("sort");
            JsonNode sectionName = rootNode.get("sectionName");
            JsonNode type = rootNode.get("type");
            JsonNode introduction = rootNode.get("introduction");
            JsonNode partId = rootNode.get("partId");
            List<JsonNode> list = new ArrayList<>();
            list.add(courseId);
            list.add(sort);
            list.add(sectionName);
            list.add(type);
            list.add(introduction);
            list.add(partId);
            if (JSON_Utils.checkNode(list)) {
                if (sectionService.addSection(sectionName.asText(), teacherId, partId.asInt(), sort.asInt(), type.asText(), introduction.asText(), courseId.asInt())) {
                    courseService.AddSectionToCourse(courseId.asInt());
                    status = 200;
                    message = "success";
                    details = "添加成功";
                } else details = "添加失败，修改前数据已刷新";
            }
        }else{
            details = "权限不足";
        }
        SetResponse_Utils.setResponse(response, status, message ,details);
    }

    public void deleteSection(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "数据不完整";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin) {
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode sectionId = rootNode.get("sectionId");
            JsonNode courseId = rootNode.get("courseId");
            List<JsonNode> list = new ArrayList<>();
            list.add(sectionId);
            list.add(courseId);
            if (JSON_Utils.checkNode(list)) {
                if (sectionService.deleteSection(sectionId.asInt())) {
                    courseService.DeleteSectionFromCourse(courseId.asInt());
                    status = 200;
                    message = "success";
                    details = "删除成功";
                } else details = "删除成功，修改前数据已刷新";
            }
        }else{
            details = "权限不足";
        }
        SetResponse_Utils.setResponse(response, status, message ,details);
    }

    public void changePartData(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "修改失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin){
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode sectionId = rootNode.get("sectionId");
            JsonNode dataType = rootNode.get("dataType");
            JsonNode data = rootNode.get("data");
            List<JsonNode> list = new ArrayList<>();
            list.add(sectionId);
            list.add(dataType);
            list.add(data);
            if (JSON_Utils.checkNode(list)) {
                Map<String, Object> map = new HashMap<>();
                map.put(dataType.asText(), data.asText());
                if (sectionService.changeData(map, sectionId.asInt())) {
                    status = 200;
                    message = "success";
                    details = "修改成功";
                }
            }
        }else {
            details = "权限不足";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void showMyExerciseHistory(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 200;
        String message = "false";
        Object details = "查询失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if(!admin){
            int userId = Integer.parseInt(claims.get("id").toString());
            details = userExerService.getUser_ExerByUser(userId);
            message = "success";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void addExercise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "添加失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin) {
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode partId = rootNode.get("partId");
            JsonNode type = rootNode.get("type");
            JsonNode content = rootNode.get("content");
            JsonNode sort = rootNode.get("sort");
            JsonNode courseId = rootNode.get("courseId");
            JsonNode answer = rootNode.get("answer");
            List<JsonNode> list = new ArrayList<>();
            list.add(courseId);
            list.add(answer);
            list.add(type);
            list.add(content);
            list.add(partId);
            list.add(type);
            if (JSON_Utils.checkNode(list)) {
                if(exerciseService.addExercises(partId.asInt(), sort.asInt(), courseId.asInt(), type.asText(), content.asText(), answer.asText())){
                    Part part = partService.getPart(partId.asInt());
                    if (!part.isHasExercises()){
                        partService.setPartExercise(partId.asInt(), true);
                        courseService.AddExercisesToCourse(courseId.asInt());
                    }
                    courseService.AddExerciseToCourse(courseId.asInt());
                    status = 200;
                    message = "success";
                    details = "添加成功";
                }
            }else{
                details = "数据不完整";
            }
        }else {
            details = "权限不足";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void deleteExercise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "删除失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin){
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode exerciseId = rootNode.get("exerciseId");
            JsonNode courseId = rootNode.get("courseId");
            List<JsonNode> list = new ArrayList<>();
            list.add(courseId);
            list.add(exerciseId);
            if (JSON_Utils.checkNode(list)) {
                if (exerciseService.deleteExercises(exerciseId.asInt())) {
                    courseService.RemoveExerciseFromCourse(courseId.asInt());
                    status = 200;
                    message = "success";
                    details = "删除成功";
                }
            }
        }else {
            details = "权限不足";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void changeExerciseData(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "修改失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin){
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode exerciseId = rootNode.get("exerciseId");
            JsonNode data = rootNode.get("data");
            JsonNode dataType = rootNode.get("type");
            List<JsonNode> list = new ArrayList<>();
            list.add(data);
            list.add(dataType);
            list.add(exerciseId);
            if (JSON_Utils.checkNode(list)) {
                Map<String, Object> map = new HashMap<>();
                map.put(dataType.asText(), data.asText());
                if (exerciseService.updateExercises(exerciseId.asInt(), map)){
                    status = 200;
                    message = "success";
                    details = "修改成功";
                }
            }else{
                details = "信息不全";
            }
        }else {
            details = "权限不足";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void checkAnswer(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "检查失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        int id = Integer.parseInt(claims.get("id").toString());;
        if (admin){
            details = "老师不能做题";
        }else {
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            Map<Integer, String> map = new HashMap<>();
            for (JsonNode node : rootNode) {
                JsonNode answer = node.get("answer");
                JsonNode exerciseId = node.get("exerciseId");
                List<JsonNode> list = new ArrayList<>();
                list.add(exerciseId);
                list.add(answer);
                if (JSON_Utils.checkNode(list)) {
                    map.put(exerciseId.asInt(), answer.asText());
                }
                userExerService.checkAnswer(id, map);
            }
            status = 200;
            message = "success";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void checkTeacher(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 200;
        String message = "success";
        Object details = "isnot";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin){
            details = "is";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void checkIsAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "不是该课程的老师";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin){
            int id = Integer.parseInt(claims.get("id").toString());;
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode courseId = rootNode.get("courseId");
            List<JsonNode> list = new ArrayList<>();
            list.add(courseId);
            if (JSON_Utils.checkNode(list)) {
                Course course = courseService.getCourse(courseId.asInt());
                if (course.getId() == id){
                    status = 200;
                    message = "success";
                    details = "是该课程的老师";
                }
            }else {
                details = "数据不完整";
            }
        }else {
            status = 200;
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void checkUserAndCourse(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "已添加";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        int id = Integer.parseInt(claims.get("id").toString());
        boolean admin = (boolean)claims.get("admin");
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        JsonNode courseId = rootNode.get("courseId");
        List<JsonNode> list = new ArrayList<>();
        list.add(courseId);
        if (JSON_Utils.checkNode(list)) {
            if (!admin){
                if (!courseService.checkUserAndCourse(id, courseId.asInt())) {
                    status = 200;
                    message = "success";
                    details = "unAdded";
                }else {
                    status = 200;
                    message = "success";
                    details = "add";
                }
            }else {
                status = 200;
                details = "是老师";
            }
        }else{
            details = "数据不完整";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void getMySchedule(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "数据不完善";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        int id = Integer.parseInt(claims.get("id").toString());;
        if (admin){
            details = "老师没有学习记录";
        }else {
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode courseId = rootNode.get("courseId");
            List<JsonNode> list = new ArrayList<>();
            list.add(courseId);
            if (JSON_Utils.checkNode(list)) {
                Map<String, Object> map = new HashMap<>();
                map.put("sectionSchedule", courseService.getSectionSchedule(id, courseId.asInt()));
                map.putAll(userExerService.getExercisesSchedule(id, courseId.asInt()));
                details = map;
                status = 200;
                message = "success";
            }
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void getAllStudentSchedule(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        if (admin){
            message = "success";
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode courseId = rootNode.get("courseId");
            List<JsonNode> list = new ArrayList<>();
            list.add(courseId);
            if (JSON_Utils.checkNode(list)) {
                details = courseService.getAllUserSchedule(courseId.asInt());
                message = "success";
                status = 200;
            }
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void checkIsFinished(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "查询失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        int id = Integer.parseInt(claims.get("id").toString());;
        if(!admin){
            JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
            JsonNode sectionId = rootNode.get("sectionId");
            List<JsonNode> list = new ArrayList<>();
            list.add(sectionId);
            if (JSON_Utils.checkNode(list)) {
                Map<String,Object> map = userSectionDao.getUser_section(id, sectionId.asInt());
                if (map == null){
                    Section section = sectionService.getSection(sectionId.asInt());
                    userSectionDao.addUser_section(id, sectionId.asInt(), section.getCourseId());
                    status = 200;
                    message = "success";
                    details = "unfinished";
                }else {
                    if ((boolean) map.get("finish") ){
                        status = 200;
                        message = "success";
                        details = "finished";
                    }else {
                        status = 200;
                        message = "success";
                        details = "unfinished";
                    }
                }
            }
        }else {
            details = "老师没有学习记录";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void addCourse(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 200;
        String message = "false";
        Object details = "添加失败";
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        boolean admin = (boolean)claims.get("admin");
        int id = Integer.parseInt(claims.get("id").toString());;
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        if (admin) {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            JsonNode startTime = rootNode.get("startTime");
            JsonNode endTime = rootNode.get("endTime");
            JsonNode introduction = rootNode.get("introduction");
            JsonNode courseName = rootNode.get("courseName");
            JsonNode maxStudent = rootNode.get("maxStudent");
            JsonNode ready = rootNode.get("ready");
            List<JsonNode> jsonNodes = new ArrayList<>();
            jsonNodes.add(startTime);
            jsonNodes.add(endTime);
            jsonNodes.add(introduction);
            jsonNodes.add(courseName);
            jsonNodes.add(maxStudent);
            jsonNodes.add(ready);
            if (!JSON_Utils.checkNode(jsonNodes)){
                status = 400;
                message = "false";
                details = "数据不完整";
            }else {
                String teacherName = userService.showUserInfo(id).getUsername();
                if (courseService.checkName(courseName.asText())) {
                    if (lock.tryLock(1, TimeUnit.SECONDS)) {
                        if (courseService.checkName(courseName.asText())) {
                            if (courseService.creatCourse(ready.asBoolean(), courseName.asText(), maxStudent.asInt(), teacherName, introduction.asText(), LocalDateTime.parse(startTime.asText(), formatter), LocalDateTime.parse(endTime.asText(), formatter), id)) {
                                status = 200;
                                message = "success";
                                details = "添加成功";
                            } else {
                                status = 400;
                                message = "false";
                                details = "添加失败";
                            }
                        } else {
                            status = 400;
                            message = "false";
                            details = "课程名已存在";
                        }
                        lock.unlock();
                    } else {
                        status = 500;
                        message = "false";
                        details = "服务器繁忙";
                    }
                } else {
                    status = 400;
                    message = "false";
                    details = "课程名已存在";
                }
            }
        }else{
            int courseId = rootNode.get("courseId").asInt();
            if (!courseService.checkUserAndCourse(id, courseId)) {
                Course course = courseService.getCourse(courseId);
                if (course != null) {
                    if (courseService.addStudentToCourse(id, course)) {
                        message = "success";
                        details = "添加成功";
                    } else {
                        status = 400;
                        message = "false";
                        details = "人数已满";
                    }
                } else {
                    status = 400;
                    message = "false";
                    details = "课程不存在";
                }
            }else{
                details = "不可重复添加";
            }
        }
        SetResponse_Utils.setResponse(response,status,message,details);
    }
}
