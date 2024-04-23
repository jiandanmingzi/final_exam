package com.hjf.demo.Service;

import com.hjf.demo.entity.Exercise;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ExerciseService {
    List<Exercise> getExercisesBelowPart(int partId) throws SQLException, InterruptedException;
    boolean addExercises(int partId, int sort, int courseId, String type, String content, String answer) throws SQLException, InterruptedException;
    boolean deleteExercises(int id) throws SQLException, InterruptedException;
    Exercise getExercises(int id) throws SQLException, InterruptedException;
    boolean updateExercises(int id, Map<String, Object> map) throws SQLException, InterruptedException;
}
