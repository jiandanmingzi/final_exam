package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Dao.CourseAndUserDao;
import com.hjf.demo.Service.CourseService;
import com.hjf.demo.Service.Impl.CourseServiceImpl;
import com.hjf.demo.entity.Course;
import com.hjf.demo.entity.User;
import com.hjf.demo.utils.CRUD_Utils;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CourseAndUserDaoImpl implements CourseAndUserDao {
    private final String table = "course-student";
    private final CourseService courseService = new CourseServiceImpl();
    @Override
    public List<Course> selectCourse(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException {
        List<Map<String, Object>> maps = DAO_Utils.selection(table, dataType, data, type);
        List<Course> courses = null;
        if (maps != null && !maps.isEmpty()) {
            courses = new ArrayList<Course>();
            for (Map<String, Object> map : maps) {
                Course course = courseService.getCourse((int)map.get("courseId"));
                courses.add(course);
            }
        }
        return courses;
    }

    @Override
    public List<User> selectUser(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException {
        return List.of();
    }

    @Override
    public boolean add(Map<String, Object> map) throws SQLException, InterruptedException {
        return DAO_Utils.add(table, map);
    }

    @Override
    public boolean delete(int studentId, int courseId) throws SQLException, InterruptedException {
        String sql = "DELETE FROM" + table + " WHERE studentId = ? , courseId = ?";
        return (CRUD_Utils.update(sql, studentId, courseId) == 1);
    }

    @Override
    public boolean update(int id, Map<String, Object> map) throws SQLException, InterruptedException {
        return false;
    }
}
