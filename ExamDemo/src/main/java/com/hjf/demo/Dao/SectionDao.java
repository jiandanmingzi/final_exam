package com.hjf.demo.Dao;

import com.hjf.demo.entity.Section;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface SectionDao {
    List<Section> select(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException;
    boolean add(Map<String, Object> map)throws SQLException, InterruptedException;
    boolean delete(int id) throws SQLException, InterruptedException;
    boolean update(int id, Map<String, Object> map) throws SQLException, InterruptedException;
    boolean deleteThroughPath(int id, String path);
}
