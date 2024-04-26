package com.hjf.demo.Service.Impl;

import com.hjf.demo.Dao.AuthenticateDao;
import com.hjf.demo.Dao.Impl.AuthenticateDaoImpl;
import com.hjf.demo.Service.AuthenticateService;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;

public class AuthenticateServiceImpl implements AuthenticateService {
    private final AuthenticateDao authenticateDao = new AuthenticateDaoImpl();
    @Override
    public boolean changeInfo(int userId, boolean admin, Map<String, Object> info) throws SQLException, InterruptedException {
        return authenticateDao.update(admin, userId, info);
    }

    @Override
    public boolean addInfo(int userId, boolean admin, Map<String, Object> info) throws SQLException, InterruptedException {
        return authenticateDao.add(admin, userId, info);
    }

    @Override
    public Map<String, Object> getInfo(int userId, boolean admin) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("realName");
        if (admin){
            set.add("teach");
        }else{
            set.add("studentId");
            set.add("college");
            set.add("major");
            set.add("grade");
            set.add("clazz");
        }
        return authenticateDao.select(userId, admin, set);
    }

    @Override
    public boolean deleteInfo(int userId, boolean admin) throws SQLException, InterruptedException {
        return authenticateDao.delete(userId, admin);
    }
}
