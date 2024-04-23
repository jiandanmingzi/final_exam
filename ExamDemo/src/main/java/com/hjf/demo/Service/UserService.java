package com.hjf.demo.Service;

import com.hjf.demo.entity.User;

import java.sql.SQLException;

public interface UserService {
    boolean changeInfo(int id, String email, String qq, String introduction, String username) throws SQLException, InterruptedException;
    boolean changePassword(String account,String password) throws SQLException, InterruptedException;
    User login(String account,String password) throws SQLException, InterruptedException;
    boolean signup(String account,String username,String password,String email,boolean admin) throws SQLException, InterruptedException;
    int checkData(String type, Object data) throws SQLException, InterruptedException;
    User showUserInfo(int id) throws SQLException, InterruptedException;
}
