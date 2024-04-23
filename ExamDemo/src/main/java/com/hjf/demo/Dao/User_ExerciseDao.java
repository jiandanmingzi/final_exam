package com.hjf.demo.Dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hjf.demo.entity.User_Exer;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

public interface User_ExerciseDao {
    List<User_Exer> getUser_Exer(String dataType, Object data, HashSet<String> set) throws SQLException, InterruptedException, JsonProcessingException;
    boolean addUser_Exer(User_Exer user_exer) throws SQLException, InterruptedException, JsonProcessingException;
    boolean updateUser_Exer(User_Exer user_exer) throws JsonProcessingException, SQLException, InterruptedException;
    boolean deleteUser_Exer(int id) throws SQLException, InterruptedException;
    void deleteAllUser_Exer(int partId) throws SQLException, InterruptedException;
}
