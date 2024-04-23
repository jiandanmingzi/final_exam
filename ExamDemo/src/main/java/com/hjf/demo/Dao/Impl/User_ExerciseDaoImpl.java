package com.hjf.demo.Dao.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hjf.demo.Dao.User_ExerciseDao;
import com.hjf.demo.entity.User_Exer;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.SQLException;
import java.util.*;

public class User_ExerciseDaoImpl implements User_ExerciseDao {
    private final String table = "user_exercise";
    @Override
    public List<User_Exer> getUser_Exer(String dataType, Object data, HashSet<String> set) throws SQLException, InterruptedException, JsonProcessingException {
        List<Map<String, Object>> list = DAO_Utils.selection(table, dataType, data, set);
        if (list != null && !list.isEmpty()) {
            List<User_Exer> user_exers = new ArrayList<>();
            for (Map<String, Object> map : list) {
                User_Exer user_exer = new User_Exer();
                if (set.contains("id")) {
                    user_exer.setId((int) map.get("id"));
                }
                if (set.contains("userId")) {
                    user_exer.setUserId((int) map.get("userId"));
                }
                if (set.contains("partId")) {
                    user_exer.setPartId((int) map.get("partId"));
                }
                if (set.contains("accuracy")) {
                    user_exer.setAccuracy((float) map.get("accuracy"));
                }
                if (set.contains("identifier")) {
                    user_exer.setIdentifier((String) map.get("identifier"));
                }
                if (set.contains("rightExercise")) {
                    user_exer.setRightExercise((String) map.get("rightExercise"));
                }
                if (set.contains("wrongExercise")) {
                    user_exer.setWrongExercise((String) map.get("wrongExercise"));
                }
                user_exers.add(user_exer);
            }
            return user_exers;
        }
        return null;
    }

    @Override
    public boolean addUser_Exer(User_Exer user_exer) throws SQLException, InterruptedException, JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user_exer.getUserId());
        map.put("partId", user_exer.getPartId());
        map.put("accuracy", user_exer.getAccuracy());
        map.put("identifier", user_exer.getIdentifier());
        map.put("rightExercise", user_exer.getRightExercise());
        map.put("wrongExercise", user_exer.getWrongExercise());
        return DAO_Utils.add(table, map);
    }

    @Override
    public boolean updateUser_Exer(User_Exer user_exer) throws JsonProcessingException, SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user_exer.getUserId());
        map.put("partId", user_exer.getPartId());
        map.put("accuracy", user_exer.getAccuracy());
        map.put("identifier", user_exer.getIdentifier());
        map.put("rightExercise", user_exer.getRightExercise());
        map.put("wrongExercise", user_exer.getWrongExercise());
        return DAO_Utils.update(table, user_exer.getId(), map);
    }

    @Override
    public boolean deleteUser_Exer(int id) throws SQLException, InterruptedException {
        return DAO_Utils.delete(table, id);
    }

    @Override
    public void deleteAllUser_Exer(int partId) throws SQLException, InterruptedException {
        DAO_Utils.deleteAll(table, "partId", partId);
    }
}
