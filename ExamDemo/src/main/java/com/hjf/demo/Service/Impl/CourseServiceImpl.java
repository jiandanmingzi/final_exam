package com.hjf.demo.Service.Impl;

import com.hjf.demo.Bean.AllCourseFactory;
import com.hjf.demo.Dao.CourseAndUserDao;
import com.hjf.demo.Dao.CourseDao;
import com.hjf.demo.Dao.Impl.CourseAndUserDaoImpl;
import com.hjf.demo.Dao.Impl.CourseDaoImpl;
import com.hjf.demo.Service.CourseService;
import com.hjf.demo.entity.Course;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao = new CourseDaoImpl();
    private final CourseAndUserDao courseAndUserDao = new CourseAndUserDaoImpl();
    @Override
    public List<Course> showTeacherCourse(int teacherId) throws SQLException, InterruptedException {
        return AllCourseFactory.getInstance().getCourses(teacherId);
    }

    @Override
    public Course getCourse(int id) throws SQLException, InterruptedException {
        return AllCourseFactory.getInstance().getCourse(id);
    }

    @Override
    public List<Course> showSomeCourse(int start, int num) throws SQLException, InterruptedException {
        return AllCourseFactory.getInstance().getSomeCourses(start, num);
    }

    @Override
    public boolean creatCourse(boolean ready, String name, int maxStudent, String teacherName, String introduction, LocalDateTime startDate, LocalDateTime endDate, int teacherId) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("ready", ready);
        map.put("courseName", name);
        map.put("maxStudent", maxStudent);
        map.put("teacherName", teacherName);
        map.put("introduction", introduction);
        map.put("startDate", startDate.toString());
        map.put("endDate", endDate.toString());
        map.put("teacherId", teacherId);
        map.put("student", 0);
        if (courseDao.add(map)){
            HashSet<String> set = new HashSet<>();
            set.add("id");
            List<Course> courses = courseDao.select("courseName",name,set);
            if (courses!= null && (!courses.isEmpty())){
                Course course = new Course(teacherId ,name , 0, maxStudent, teacherName, introduction, startDate, endDate,ready);
                course.setId(courses.getFirst().getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean changeMaxStudent(int maxStudent, int id) {
        return false;
    }

    @Override
    public boolean changeTime(LocalDateTime startDate, LocalDateTime endDate, int id) {
        return false;
    }

    @Override
    public boolean deleteCourse(int id) {
        return false;
    }

    @Override
    public boolean checkName(String name) throws SQLException, InterruptedException {
        return (AllCourseFactory.getInstance().getCourseIdByName(name) != 0);
    }

    @Override
    public boolean addStudentToCourse(int id, Course course) throws InterruptedException, SQLException {
        if (course.tryAddStudent()){
            int courseId = course.getId();
            Map<String, Object> map = new HashMap<>();
            map.put("studentId", id);
            map.put("courseId", courseId);
            try {
                if (courseAndUserDao.add(map)){
                    Map<String, Object> stringObjectMap = new HashMap<>();
                    map.put("student", course.getStudent());
                    if (courseDao.update(courseId,stringObjectMap)){
                        return true;
                    }else{
                        course.tryRemoveStudent();
                        courseAndUserDao.delete(id, courseId);
                    }
                }else{
                    course.tryRemoveStudent();
                }
            } catch (SQLException e) {
                course.tryRemoveStudent();
                throw new SQLException("Server error:" + e.getMessage());
            }
        }
        return false;
    }
}
