package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Dao.CourseDao;
import com.hjf.demo.entity.Course;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CourseDaoImpl implements CourseDao {
    private final String table = "course";
    @Override
    public List<Course> select(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException {
        List<Map<String, Object>> Data = DAO_Utils.selection(table,dataType, data, type);
        List<Course> list = null;
        if (Data != null && (!Data.isEmpty())){
            list = new ArrayList<>();
            for (Map<String, Object> map : Data){
                Course course = new Course();
                if (type.contains("exerciseNum")){
                    course.setExerciseNum((int)map.get("exerciseNum"));
                }
                if (type.contains("exercisesNum")){
                    course.setExercisesNum((int)map.get("exercisesNum"));
                }
                if (type.contains("ready")){
                    course.setReady((boolean) map.get("ready"));
                }
                if(type.contains("sectionNum")){
                    course.setSectionNum((Integer) map.get("sectionNum"));
                }
                if (type.contains("teacherId"))
                {
                    course.setTeacherId((int) map.get("teacherId"));
                }
                if (type.contains("introduction")){
                    course.setIntroduction((String) map.get("introduction"));
                }
                if (type.contains("teacherName")){
                    course.setTeacherName((String) map.get("teacherName"));
                }
                if (type.contains("courseName")){
                    course.setCourseName((String) map.get("courseName"));
                }
                if (type.contains("maxStudent")){
                    course.setMaxStudent((int) map.get("maxStudent"));
                }
                if (type.contains("student")){
                    course.setStudent((int) map.get("student"));
                }
                if (type.contains("startDate")){
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    course.setStartDate(LocalDateTime.parse((String) map.get("startDate"),formatter));
                }
                if (type.contains("endDate")){
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    course.setEndDate(LocalDateTime.parse((String) map.get("endDate"),formatter));
                }
                if (type.contains("id")){
                    course.setId((int) map.get("id"));
                }
                list.add(course);
            }
        }
        return list;
    }

    @Override
    public boolean add(Map<String, Object> map) throws SQLException, InterruptedException {
        return DAO_Utils.add(table,map);
    }

    @Override
    public boolean delete(int id) throws SQLException, InterruptedException {
        return DAO_Utils.delete(table, id);
    }

    @Override
    public boolean update(int id, Map<String, Object> map) throws SQLException, InterruptedException {
        return DAO_Utils.update(table, id ,map);
    }
}