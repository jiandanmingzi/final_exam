package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Dao.ExerciseDao;
import com.hjf.demo.entity.Exercise;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ExerciseDaoImpl implements ExerciseDao {
    private final String table = "exercises";
    @Override
    public List<Exercise> select(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException {
        List<Map<String, Object>> Data = DAO_Utils.selection(table,dataType, data, type);
        List<Exercise> list = null;
        if (Data != null && (!Data.isEmpty())) {
            list = new ArrayList<>();
            for (Map<String, Object> map : Data){
                Exercise exercise = new Exercise();
                if (type.contains("content")){
                    exercise.setContent((String) map.get("content"));
                }
                if (type.contains("answer")){
                    exercise.setAnswer((String) map.get("answer"));
                }
                if (type.contains("id")){
                    exercise.setId((int) map.get("id"));
                }
                if (type.contains("partId")){
                    exercise.setPartId((int) map.get("partId"));
                }
                if (type.contains("type")){
                    exercise.setType((String) map.get("type"));
                }
                if (type.contains("sort")){
                    exercise.setSort((int) map.get("sort"));
                }
                if (type.contains("courseId")){
                    exercise.setCourseId((int) map.get("courseId"));
                }
                list.add(exercise);
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
        return DAO_Utils.update(table, id ,map);
    }
}
