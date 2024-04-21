package com.hjf.demo.Dao;

import com.hjf.demo.entity.Course;
import com.hjf.demo.entity.User;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface CourseAndUserDao {
    List<Course> selectCourse(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException;
    List<User> selectUser(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException;
    boolean add(Map<String, Object> map)throws SQLException, InterruptedException;
    boolean delete(int studentId, int courseId) throws SQLException, InterruptedException;
    boolean update(int id, Map<String, Object> map) throws SQLException, InterruptedException;
}
