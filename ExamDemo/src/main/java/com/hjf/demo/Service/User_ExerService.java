package com.hjf.demo.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hjf.demo.entity.User_Exer;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface User_ExerService {
    boolean checkAnswer(int userId, Map<Integer, String> map) throws SQLException, InterruptedException, JsonProcessingException;
    List<User_Exer> getUser_ExerByUser(int userId) throws SQLException, InterruptedException, JsonProcessingException;
    User_Exer getUser_Exer(String identifier) throws SQLException, InterruptedException, JsonProcessingException;
    User_Exer getUser_Exer(int id);
    boolean deleteUser_Exer(int partId) throws SQLException, InterruptedException;
    Map<String, Object> getExercisesSchedule(int userId, int courseId) throws SQLException, InterruptedException, JsonProcessingException;
}
