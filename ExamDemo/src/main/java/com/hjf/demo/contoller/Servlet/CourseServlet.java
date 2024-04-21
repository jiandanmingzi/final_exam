package com.hjf.demo.contoller.Servlet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hjf.demo.Bean.AllCourseBrief;
import com.hjf.demo.Bean.AllCourseFactory;
import com.hjf.demo.Service.CourseService;
import com.hjf.demo.Service.Impl.CourseServiceImpl;
import com.hjf.demo.entity.Course;
import com.hjf.demo.utils.JSON_Utils;
import com.hjf.demo.utils.SetResponse_Utils;

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
    private final CourseService courseService = new CourseServiceImpl();
    public void showCourseDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException, InterruptedException {
        JsonNode rootNode = JSON_Utils.ReadJsonInRequest(request);
        int id = rootNode.get("id").asInt();
        Course course = courseService.getCourse(id);
        if(course != null){
            Map<String, Object> courseMap = CourseToMap(course);
        }
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
            String jsonString = new ObjectMapper().writeValueAsString(list);
            SetResponse_Utils.setResponse(response, 200, "success", jsonString);
        }else{
            SetResponse_Utils.setResponse(response, 400, "false","No more courses");
        }
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

    public void getCourseCount (HttpServletRequest request, HttpServletResponse response) throws SQLException, InterruptedException, IOException {
        AllCourseBrief allCourseBrief = AllCourseFactory.getInstance();
        int count = allCourseBrief.CourseCount();
        SetResponse_Utils.setResponse(response, 200, "success", count + "");
    }
}
