package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Dao.AuthenticateDao;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AuthenticateDaoImpl implements AuthenticateDao {
    private final String student = "student";
    private final String teacher = "teacher";
    @Override
    public Map<String, Object> select(int userId, boolean admin, HashSet<String> set) throws SQLException, InterruptedException {
        String table;
        if (admin){
            table = teacher;
        }else {
            table = student;
        }
        List<Map<String, Object>> list = DAO_Utils.selection(table, "userId", userId, set);
        if (list != null && !list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean add(boolean admin, int userId, Map<String, Object> map) throws SQLException, InterruptedException {
        map.put("userId", userId);
        if (admin){
           return DAO_Utils.add(teacher, map);
        }else {
           return DAO_Utils.add(student, map);
        }
    }

    @Override
    public boolean update(boolean admin, int userId, Map<String, Object> map) throws SQLException, InterruptedException {
        if (admin){
            return DAO_Utils.update(teacher, userId, map);
        }else {
            return DAO_Utils.update(student, userId, map);
        }
    }

    @Override
    public boolean delete(int userId, boolean admin) throws SQLException, InterruptedException {
        if (admin){
            return DAO_Utils.delete(teacher, userId);
        }else {
            return DAO_Utils.delete(student, userId);
        }
    }
}
