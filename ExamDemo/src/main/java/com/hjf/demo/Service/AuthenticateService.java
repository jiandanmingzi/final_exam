package com.hjf.demo.Service;

import java.sql.SQLException;
import java.util.Map;

public interface AuthenticateService {
    boolean changeInfo(int userId, boolean admin, Map<String, Object> info) throws SQLException, InterruptedException;
    boolean addInfo(int userId, boolean admin, Map<String, Object> info) throws SQLException, InterruptedException;
    Map<String, Object> getInfo(int userId, boolean admin) throws SQLException, InterruptedException;
    boolean deleteInfo(int userId, boolean admin) throws SQLException, InterruptedException;
}
