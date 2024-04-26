package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Bean.AllCourseFactory;
import com.hjf.demo.Dao.*;
import com.hjf.demo.Dao.UserDao;
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

public class User_CourseDaoImpl implements User_CourseDao {
    private final String table = "user_course";
    private final UserDao userDao = new UserDaoImpl();
    @Override
    public List<Course> selectCourse(String dataType, Object data) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("courseId");
        List<Map<String, Object>> maps = DAO_Utils.selection(table, dataType, data, set);
        List<Course> courses = null;
        if (maps != null && !maps.isEmpty()) {
            courses = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                Course course = AllCourseFactory.getInstance().getCourse((int)map.get("courseId"));
                courses.add(course);
            }
        }
        return courses;
    }

    @Override
    public List<User> selectUser(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("userId");
        List<Map<String, Object>> maps = DAO_Utils.selection(table, dataType, data, set);
        List<User> users = null;
        if (maps != null && !maps.isEmpty()) {
            users = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                User user = userDao.select("id", map.get("userId"), type).get(0);
                users.add(user);
            }
        }
        return users;
    }

    public List<Map<String, Object>> showAllSchedule(int courseId) throws SQLException, InterruptedException {
        List<Map<String, Object>> maps = new ArrayList<>();

        return maps;
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

    @Override
    public boolean checkUserCourse(int userId, int courseId) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("id");
        String identifier = userId + "_" + courseId;
        List<Map<String, Object>> list = DAO_Utils.selection(table, "identifier", identifier, set);
        return (list != null && !list.isEmpty());
    }
}
