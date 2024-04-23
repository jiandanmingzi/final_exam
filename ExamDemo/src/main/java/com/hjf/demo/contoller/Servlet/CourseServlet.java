package com.hjf.demo.contoller.Servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.hjf.demo.Bean.AllCourseBrief;
import com.hjf.demo.Bean.AllCourseFactory;
import com.hjf.demo.Service.CourseService;
import com.hjf.demo.Service.ExerciseService;
import com.hjf.demo.Service.Impl.CourseServiceImpl;
import com.hjf.demo.Service.Impl.ExerciseServiceImpl;
import com.hjf.demo.Service.Impl.PartServiceImpl;
import com.hjf.demo.Service.Impl.SectionServiceImpl;
import com.hjf.demo.Service.PartService;
import com.hjf.demo.Service.SectionService;
import com.hjf.demo.entity.Course;
import com.hjf.demo.entity.Exercise;
import com.hjf.demo.entity.Part;
import com.hjf.demo.entity.Section;
import com.hjf.demo.utils.JSON_Utils;
import com.hjf.demo.utils.JWT_Utils;
import com.hjf.demo.utils.SetResponse_Utils;
import io.jsonwebtoken.Claims;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("user/courseServlet")
public class CourseServlet extends BaseServlet{
    private final SectionService sectionService = new SectionServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final PartService partService = new PartServiceImpl();
    private final ExerciseService exerciseService = new ExerciseServiceImpl();

    public void getCourseCount (HttpServletRequest request, HttpServletResponse response) throws SQLException, InterruptedException, IOException {
        AllCourseBrief allCourseBrief = AllCourseFactory.getInstance();
        int count = allCourseBrief.CourseCount();
        SetResponse_Utils.setResponse(response, 200, "success", count);
    }

    public void getSomeCourses(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        int start = rootNode.get("start").asInt();
        int num = rootNode.get("num").asInt();
        List<Course> courses = courseService.showSomeCourse(start, num);
        if (courses != null && !courses.isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Course course : courses) {
                list.add(CourseToMap(course));
            }
            SetResponse_Utils.setResponse(response, 200, "success", list);
        }else{
            SetResponse_Utils.setResponse(response, 400, "false","No more courses");
        }
    }

    public void showCourseDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "无该课程";
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        int id = rootNode.get("id").asInt();
        Course course = courseService.getCourse(id);
        if(course != null){
            Map<String, Object> courseMap = CourseToMap(course);
            status = 200;
            message = "success";
            details = courseMap;
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void showCoursePart(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "无章节";
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        int courseId = rootNode.get("courseId").asInt();
        List<Part> parts = partService.getPartByCourseId(courseId);
        if(parts!= null &&!parts.isEmpty()){
            List<Map<String, Object>> list = new ArrayList<>();
            for (Part part : parts) {
                Map<String, Object> partMap = new HashMap<>();
                partMap.put("id", part.getId());
                partMap.put("partName", part.getPartName());
                partMap.put("sort", part.getSort());
                list.add(partMap);
            }
            status = 200;
            message = "success";
            details = list;
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void showPartSection(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "无小节";
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        JsonNode partId = rootNode.get("partId");
        List<JsonNode> list = new ArrayList<>();
        list.add(partId);
        if (JSON_Utils.checkNode(list)){
            List<Section> sections = sectionService.getSectionBelowPart(partId.asInt());
            List<Map<String, Object>> sectionList = new ArrayList<>();
            if(sections != null && !sections.isEmpty()) {
                for (Section section : sections) {
                    Map<String, Object> map = SectionToMap(section);
                    sectionList.add(map);
                }
                details = sectionList;
                status = 200;
                message = "success";
            }
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void getExerciseNum(HttpServletRequest request, HttpServletResponse response){

    }

    public void showPartExercise(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "无题目";
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        JsonNode partId = rootNode.get("partId");
        List<JsonNode> list = new ArrayList<>();
        list.add(partId);
        if (JSON_Utils.checkNode(list)){
            String authHeader = request.getHeader("Authorization");
            Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
            int userId = (int)claims.get("id");
            boolean admin = (boolean)claims.get("admin");
            List<Exercise> exercises = exerciseService.getExercisesBelowPart(partId.asInt());
            if (exercises != null && !exercises.isEmpty()) {
                int courseId = exercises.getFirst().getCourseId();
                Course course = courseService.getCourse(courseId);
                if ( course != null && !course.isReady() && !admin) {
                    details = "课程尚未开放";
                }else if (course != null ){
                    if ( admin && userId != course.getTeacherId()){
                        details = "不可窃取其他老师的知识成果";
                    }else {
                        List<Map<String, Object>> maps = new ArrayList<>();
                        for (Exercise exercise : exercises) {
                            Map<String, Object> map = ExerciseToMap(exercise);
                            if (admin)
                                map.put("answer", exercise.getAnswer());
                            maps.add(map);
                        }
                        status = 200;
                        message = "success";
                        details = maps;
                    }
                }else{
                    details = "课程不存在";
                }
            }
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public void checkAnswer(HttpServletRequest request, HttpServletResponse response){

    }

    public void study(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        int status = 400;
        String message = "false";
        Object details = "课程不存在";
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        String authHeader = request.getHeader("Authorization");
        Claims claims = JWT_Utils.getClaims(authHeader.substring(7));
        int userId = (int)claims.get("id");
        boolean admin = (boolean)claims.get("admin");
        JsonNode sectionId = rootNode.get("sectionId");
        List<JsonNode> nodes = new ArrayList<>();
        nodes.add(sectionId);
        if (JSON_Utils.checkNode(nodes)){
            Section section = sectionService.getSection(sectionId.asInt());
            if (section != null) {
                if (admin && section.getTeacherId() != userId) {
                    details = "不可窃取其他老师的知识成果";
                }else{
                    Course course = courseService.getCourse(section.getCourseId());
                    if (course != null && !course.isReady() && !admin) {
                        details = "课程尚未开放";
                    }else if (course != null){
                        Map<String, Object> map = SectionToMap(section);
                        map.put("teacherId", section.getTeacherId());
                        map.put("path", section.getPath());
                        map.put("introduction", section.getIntroduction());
                        map.put("courseId", section.getCourseId());
                        status = 200;
                        message = "success";
                        details = map;
                    }
                }
            }
        }else{
            details = "请重新请求";
        }
        SetResponse_Utils.setResponse(response, status, message, details);
    }

    public static Map<String, Object> SectionToMap(Section section){
        Map<String, Object> map = new HashMap<>();
        map.put("id", section.getId());
        map.put("type", section.getType());
        map.put("partId", section.getPartId());
        map.put("sort", section.getSort());
        map.put("sectionName", section.getSectionName());
        return map;
    }

    private static Map<String, Object> CourseToMap(Course course) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", course.getId());
            map.put("teacherId", course.getTeacherId());
            map.put("courseName", course.getCourseName());
            map.put("teacherName", course.getTeacherName());
            map.put("startDate", course.getStartDate().toString());
            map.put("endDate", course.getEndDate().toString());
            map.put("introduction", course.getIntroduction());
            return map;
    }

    private static Map<String, Object> ExerciseToMap(Exercise exercise) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", exercise.getId());
        map.put("sort", exercise.getSort());
        map.put("partId", exercise.getPartId());
        map.put("type", exercise.getType());
        map.put("content", exercise.getContent());
        return map;
    }
}
