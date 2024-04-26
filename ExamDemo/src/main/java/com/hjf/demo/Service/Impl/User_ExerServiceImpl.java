package com.hjf.demo.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hjf.demo.Dao.Impl.User_ExerciseDaoImpl;
import com.hjf.demo.Dao.User_ExerciseDao;
import com.hjf.demo.Service.ExerciseService;
import com.hjf.demo.Service.User_ExerService;
import com.hjf.demo.entity.Exercise;
import com.hjf.demo.entity.User_Exer;

import java.sql.SQLException;
import java.util.*;

public class User_ExerServiceImpl implements User_ExerService {
    private final User_ExerciseDao user_exerciseDao = new User_ExerciseDaoImpl();
    private final ExerciseService exerciseService = new ExerciseServiceImpl();
    @Override
    public boolean checkAnswer(int userId, Map<Integer, String> map) throws SQLException, InterruptedException, JsonProcessingException {
        Set<Integer> set = map.keySet();
        List<Exercise> exercises =  new ArrayList<>();
        for (Integer i : set) {
            exercises.add(exerciseService.getExercises(i));
        }
        if(!exercises.isEmpty()){
            User_Exer userExer = new User_Exer();
            userExer.setPartId(exercises.get(0).getPartId());
            userExer.setUserId(userId);
            userExer.setIdentifier(userId + "_" + userExer.getPartId());
            for (Exercise exercise : exercises) {
                if (exercise.getAnswer().equals(map.get(exercise.getId()))){
                    userExer.updateRightExercise(exercise.getId(), map.get(exercise.getId()));
                }else{
                    userExer.updateWrongExercise(exercise.getId(), map.get(exercise.getId()));
                }
            }
            return user_exerciseDao.addUser_Exer(userExer);
        }
        return false;
    }

    @Override
    public List<User_Exer> getUser_ExerByUser(int userId) throws SQLException, InterruptedException, JsonProcessingException {
        HashSet<String> set = new HashSet<>();
        set.add("id");
        set.add("partId");
        set.add("courseId");
        set.add("accuracy");
        set.add("exercisesNum");
        set.add("rightExercise");
        set.add("wrongExercise");
        return user_exerciseDao.getUser_Exer("userId", userId, set);
    }

    @Override
    public User_Exer getUser_Exer(String identifier) throws SQLException, InterruptedException, JsonProcessingException {
        HashSet<String> set = new HashSet<>();
        set.add("accuracy");
        set.add("rightExercise");
        set.add("wrongExercise");
        List<User_Exer> list = user_exerciseDao.getUser_Exer("identifier", identifier, set);
        if(list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public User_Exer getUser_Exer(int id) {
        return null;
    }

    @Override
    public boolean deleteUser_Exer(int partId) throws SQLException, InterruptedException {
        user_exerciseDao.deleteAllUser_Exer(partId);
        return true;
    }

    public Map<String, Object> getExercisesSchedule(int userId, int courseId) throws SQLException, InterruptedException, JsonProcessingException {
        Map<String, Object> map = null;
        int exerciseNum = 0;
        int exercisesNum = 0;
        float accuracy = 0;
        String user_course = userId + "_" + courseId;
        HashSet<String> set = new HashSet<>();
        set.add("accuracy");
        set.add("exercisesNum");
        List<User_Exer> list = user_exerciseDao.getUser_Exer("user_course", user_course, set);
        if(list != null && !list.isEmpty()) {
            map = new HashMap<>();
            for (User_Exer user_exer : list) {
                float temp = accuracy * exerciseNum;
                exercisesNum++;
                exerciseNum += user_exer.getExercisesNum();
                accuracy = (temp + user_exer.getAccuracy() * user_exer.getExercisesNum()) / exerciseNum;
            }
            map.put("exerciseNum", exerciseNum);
            map.put("accuracy", accuracy);
            map.put("exercisesNum", exercisesNum);
        }
        return map;
    }
}
