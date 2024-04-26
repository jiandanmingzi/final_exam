package com.hjf.demo.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hjf.demo.entity.Course;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface CourseService {
    List<Course> showTeacherCourse(int teacherId) throws SQLException, InterruptedException;
    Course getCourse(int id) throws SQLException, InterruptedException;
    List<Course> showSomeCourse(int start, int num) throws SQLException, InterruptedException;
    boolean creatCourse(boolean ready, String name, int maxStudent, String teacherName, String introduction, LocalDateTime startDate, LocalDateTime endDate, int teacherId) throws SQLException, InterruptedException;
    boolean changeMaxStudent(int maxStudent, int id);
    boolean changeTime(LocalDateTime startDate, LocalDateTime endDate, int id);
    boolean deleteCourse(int id) throws SQLException, InterruptedException;
     boolean setCourseUnready(int id, boolean ready) throws SQLException, InterruptedException;
    boolean checkName(String name) throws SQLException, InterruptedException;
    boolean checkUserAndCourse(int userId, int courseId) throws SQLException, InterruptedException;
    boolean addStudentToCourse(int id, Course course) throws InterruptedException, SQLException;
    List<Course> showStudentCourse(int id) throws SQLException, InterruptedException;
    void AddSectionToCourse(int courseId) throws SQLException, InterruptedException;
    void DeleteSectionFromCourse(int courseId) throws SQLException, InterruptedException;
    void AddExerciseToCourse(int courseId) throws InterruptedException, SQLException;
    void RemoveExerciseFromCourse(int courseId) throws InterruptedException, SQLException;
    void AddExercisesToCourse(int courseId) throws InterruptedException, SQLException;
    void RemoveExercisesFromCourse(int courseId) throws InterruptedException, SQLException;
    boolean removeStudentFromCourse(int id, Course course) throws InterruptedException, SQLException;
    int getSectionSchedule(int userId, int courseId) throws SQLException, InterruptedException;
    List<Map<String, Object>> getAllUserSchedule(int courseId) throws SQLException, InterruptedException, JsonProcessingException;
}
