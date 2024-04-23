package com.hjf.demo.Service.Impl;

import com.hjf.demo.Dao.ExerciseDao;
import com.hjf.demo.Dao.Impl.ExerciseDaoImpl;
import com.hjf.demo.Service.ExerciseService;
import com.hjf.demo.entity.Exercise;

import java.sql.SQLException;
import java.util.*;

public class ExerciseServiceImpl implements ExerciseService {
    private final ExerciseDao exerciseDao = new ExerciseDaoImpl();

    @Override
    public List<Exercise> getExercisesBelowPart(int partId) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("partId");
        set.add("id");
        set.add("sort");
        set.add("courseId");
        set.add("type");
        set.add("content");
        set.add("answer");
        return exerciseDao.select("partId", partId, set);
    }

    @Override
    public boolean addExercises(int partId, int sort, int courseId, String type, String content, String answer) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("partId", partId);
        map.put("sort", sort);
        map.put("courseId", courseId);
        map.put("type", type);
        map.put("content", content);
        map.put("answer", answer);
        return exerciseDao.add(map);
    }

    @Override
    public boolean deleteExercises(int id) throws SQLException, InterruptedException {
        return exerciseDao.delete(id);
    }

    @Override
    public Exercise getExercises(int id) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("partId");
        set.add("id");
        set.add("sort");
        set.add("courseId");
        set.add("type");
        set.add("content");
        set.add("answer");
        List<Exercise> list = exerciseDao.select("id", id, set);
        if (list != null && !list.isEmpty()) {
            return list.getFirst();
        }
        return null;
    }

    @Override
    public boolean updateExercises(int id, Map<String, Object> map) throws SQLException, InterruptedException {
        Set<String> set = map.keySet();
        if (set.contains("type")) {
            map.put("content", null);
            map.put("answer", null);
        }
        return exerciseDao.update(id, map);
    }
}
