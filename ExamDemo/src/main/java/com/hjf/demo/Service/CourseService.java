package com.hjf.demo.Service;

import com.hjf.demo.entity.Course;
import com.hjf.demo.entity.Part;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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
    boolean addStudentToCourse(int id, Course course) throws InterruptedException, SQLException;
}
