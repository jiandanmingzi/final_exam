package com.hjf.demo.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface User_SectionDao {
    List<Map<String, Object>> getUserSchedule(int studentId, int courseId) throws SQLException, InterruptedException;
    Map<String, Object> getUser_section(int studentId, int sectionId) throws SQLException, InterruptedException;
    boolean addUser_section(int studentId, int sectionId, int courseId) throws SQLException, InterruptedException;
    boolean changeUser_section(String dataType, Object data, String identifier) throws SQLException, InterruptedException;
}
