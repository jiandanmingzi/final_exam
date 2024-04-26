package com.hjf.demo.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hjf.demo.Bean.AllCourseFactory;

import com.hjf.demo.Dao.CourseDao;
import com.hjf.demo.Dao.*;
import com.hjf.demo.Dao.Impl.*;
import com.hjf.demo.Dao.Impl.CourseDaoImpl;
import com.hjf.demo.Dao.Impl.User_SectionDaoImpl;
import com.hjf.demo.Dao.User_SectionDao;
import com.hjf.demo.Service.CourseService;
import com.hjf.demo.entity.Course;
import com.hjf.demo.entity.User;

import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao = new CourseDaoImpl();
    private final User_CourseDao courseAndUserDao = new User_CourseDaoImpl();
    private final User_SectionDao userSectionDao = new User_SectionDaoImpl();
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
        map.put("sectionNum", 0);
        if (courseDao.add(map)){
            HashSet<String> set = new HashSet<>();
            set.add("id");
            List<Course> courses = courseDao.select("courseName",name,set);
            if (courses!= null && (!courses.isEmpty())){
                Course course = new Course(0, teacherId, 0, 0, name, maxStudent, 0, teacherName, introduction, ready, startDate, endDate);
                course.setId(courses.get(0).getId());
                AllCourseFactory.getInstance().addCourse(course);
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
    public boolean deleteCourse(int id) throws SQLException, InterruptedException {
        return courseDao.delete(id);
    }

    @Override
    public boolean checkName(String name) throws SQLException, InterruptedException {
        return (AllCourseFactory.getInstance().getCourseIdByName(name) != 0);
    }

    @Override
    public boolean setCourseUnready(int id, boolean ready) throws SQLException, InterruptedException {
        Course course = AllCourseFactory.getInstance().getCourse(id);
        if (course!= null){
            course.setReady(ready);
            Map<String, Object> map = new HashMap<>();
            map.put("ready", ready);
            if (courseDao.update(id, map)){
                return true;
            }else{
                course.setReady(!ready);
            }
        }
        return false;
    }

    @Override
    public boolean checkUserAndCourse(int userId, int courseId) throws SQLException, InterruptedException {
        return courseAndUserDao.checkUserCourse(userId, courseId);
    }

    @Override
    public boolean addStudentToCourse(int id, Course course) throws InterruptedException, SQLException {
        if (course.tryAddStudent()){
            int courseId = course.getId();
            String identifier = id + "_" + courseId;
            Map<String, Object> map = new HashMap<>();
            map.put("studentId", id);
            map.put("courseId", courseId);
            map.put("identifier", identifier);
            try {
                if (courseAndUserDao.add(map)){
                    Map<String, Object> stringObjectMap = new HashMap<>();
                    stringObjectMap.put("student", course.getStudent());
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

    @Override
    public boolean removeStudentFromCourse(int id, Course course) throws InterruptedException, SQLException {
        if (course.tryRemoveStudent()){
            int courseId = course.getId();
            if (courseAndUserDao.delete(id, courseId)){
                Map<String, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("student", course.getStudent());
                courseDao.update(courseId,stringObjectMap);
                return true;
            }else {
                course.tryAddStudent();
            }
        }
        return false;
    }

    @Override
    public void AddExerciseToCourse(int courseId) throws InterruptedException, SQLException {
        Course course = AllCourseFactory.getInstance().getCourse(courseId);
        course.exerciseNumUP();
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("exerciseNum", course.getExerciseNum());
        courseDao.update(courseId,stringObjectMap);
    }

    @Override
    public void RemoveExerciseFromCourse(int courseId) throws InterruptedException, SQLException {
        Course course = AllCourseFactory.getInstance().getCourse(courseId);
        course.exerciseNumDOWN();
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("exerciseNum", course.getExerciseNum());
        courseDao.update(courseId,stringObjectMap);
    }

    @Override
    public void AddExercisesToCourse(int courseId) throws InterruptedException, SQLException {
        Course course = AllCourseFactory.getInstance().getCourse(courseId);
        course.exercisesNumUP();
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("exercisesNum", course.getExercisesNum());
        courseDao.update(courseId,stringObjectMap);
    }

    @Override
    public void RemoveExercisesFromCourse(int courseId) throws InterruptedException, SQLException {
        Course course = AllCourseFactory.getInstance().getCourse(courseId);
        course.exercisesNumDOWN();
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("exercisesNum", course.getExercisesNum());
        courseDao.update(courseId,stringObjectMap);
    }

    @Override
    public List<Course> showStudentCourse(int id) throws SQLException, InterruptedException {
        return courseAndUserDao.selectCourse("userId", id);
    }

    @Override
    public void AddSectionToCourse(int courseId) throws SQLException, InterruptedException {
        Course course = AllCourseFactory.getInstance().getCourse(courseId);
        course.sectionNumUP();
        Map<String, Object> map = new HashMap<>();
        map.put("sectionNum", course.getSectionNum());
        courseDao.update(courseId, map);
    }

    @Override
    public void DeleteSectionFromCourse(int courseId) throws SQLException, InterruptedException {
        Course course = AllCourseFactory.getInstance().getCourse(courseId);
        course.sectionNumDOWN();
        Map<String, Object> map = new HashMap<>();
        map.put("sectionNum", course.getSectionNum());
        courseDao.update(courseId, map);
    }

    @Override
    public int getSectionSchedule(int userId, int courseId) throws SQLException, InterruptedException {
        List<Map<String, Object>> list = userSectionDao.getUserSchedule(userId, courseId);
        int cnt = 0;
        for (Map<String, Object> map : list) {
            if ((boolean) map.get("finished")) {
                cnt ++;
            }
        }
        return cnt;
    }

    @Override
    public List<Map<String, Object>> getAllUserSchedule(int courseId) throws SQLException, InterruptedException, JsonProcessingException {
        HashSet<String> set = new HashSet<>();
        set.add("username");
        set.add("id");
        List<User> users = courseAndUserDao.selectUser("courseId", courseId, set);
        List<Map<String, Object>> list = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", user.getId());
            map.put("username", user.getUsername());
            map.putAll(new User_ExerServiceImpl().getExercisesSchedule(user.getId(), courseId));
            map.put("sectionSchedule", getSectionSchedule(user.getId(), courseId));
            list.add(map);
        }
        return list;
    }
}
