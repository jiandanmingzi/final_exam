package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Dao.UserDao;
import com.hjf.demo.entity.User;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.SQLException;
import java.util.*;

public class UserDaoImpl implements UserDao {
    private final String table ="user";
    public UserDaoImpl(){}

    @Override
    public List<User> select(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException {
        List<Map<String, Object>> Data = DAO_Utils.selection(table,dataType, data, type);
        List<User> list = null;
        if (Data != null && (!Data.isEmpty())){
            list = new ArrayList<>();
            for (Map<String, Object> map : Data) {
                User user = new User();
                if (type.contains("introduction")) {
                    user.setIntroduction((String) map.get("introduction"));
                }
                if (type.contains(("authenticated"))){
                    user.setAuthenticated((boolean) map.get("authenticated"));
                }
                if (type.contains("password")) {
                    user.setPassword((String) map.get("password"));
                }
                if (type.contains("account")) {
                    user.setAccount((String) map.get("account"));
                }
                if (type.contains("username")) {
                    user.setUsername((String) map.get("username"));
                }
                if (type.contains("admin")) {
                    user.setAdmin((boolean) map.get("admin"));
                }
                if (type.contains("email")) {
                    user.setEmail((String) map.get("email"));
                }
                if (type.contains("id")) {
                    user.setId((int) map.get("id"));
                }
                if (type.contains("qq")) {
                    user.setQq((String) map.get("qq"));
                }
                list.add(user);
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
        return DAO_Utils.update(table, id , map);
    }
}
