package com.hjf.demo.Service.Impl;

import com.hjf.demo.Dao.Impl.UserDaoImpl;
import com.hjf.demo.Dao.UserDao;
import com.hjf.demo.Service.UserService;
import com.hjf.demo.entity.User;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    public UserServiceImpl(){}

    @Override
    public boolean changeInfo(int id, String email, String qq, String introduction, String username) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("qq", qq);
        map.put("introduction", introduction);
        map.put("username", username);
        return userDao.update(id,map);
    }

    @Override
    public boolean changePassword(String account,String password) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("passWord", password);
        HashSet<String> set = new HashSet<>();
        set.add("id");
        List<User> list = userDao.select("account", account, set);
        if (list != null && (!list.isEmpty())) {
            int id = list.getFirst().getId();
            return userDao.update(id,map);
        }
        return false;
    }

    @Override
    public User login(String account, String password) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("password");
        set.add("id");
        set.add("authenticated");
        set.add("admin");
        List<User> list = userDao.select("account",account,set);
        if (list != null && (!list.isEmpty())) {
            User user = list.getFirst();
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean signup(String account, String username, String password, String email, boolean admin) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        map.put("email", email);
        map.put("admin", admin);
        map.put("account", account);
        map.put("authenticated", false);
        return userDao.add(map);
    }

    @Override
    public int checkData(String type, Object data) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("id");
        List<User> list = userDao.select(type,data,set);
        if (list != null && (!list.isEmpty())){
            return list.getFirst().getId();
        }
        return 0;
    }

    @Override
    public User showUserInfo(int id) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("account");
        set.add("username");
        set.add("email");
        set.add("admin");
        set.add("introduction");
        set.add("qq");
        set.add("authenticated");
        return userDao.select("id",id,set).getFirst();
    }
}
