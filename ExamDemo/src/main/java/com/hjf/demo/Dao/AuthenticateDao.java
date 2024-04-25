package com.hjf.demo.Dao;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;

public interface AuthenticateDao {
    Map<String, Object> select(int userId, boolean admin, HashSet<String> set) throws SQLException, InterruptedException;
    boolean add(boolean admin, int userId, Map<String, Object>map) throws SQLException, InterruptedException;
    boolean update(boolean admin, int userId, Map<String, Object> map) throws SQLException, InterruptedException;
    boolean delete(int userId, boolean admin) throws SQLException, InterruptedException;
}
