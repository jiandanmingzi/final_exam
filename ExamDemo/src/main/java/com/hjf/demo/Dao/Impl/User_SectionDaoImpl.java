package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Dao.User_SectionDao;
import com.hjf.demo.utils.CRUD_Utils;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class User_SectionDaoImpl implements User_SectionDao {
    private final String table = "user_section";
    @Override
    public List<Map<String, Object>> getUserSchedule(int studentId, int courseId) throws SQLException, InterruptedException {
        String sql = "select finished, schedule from user_section where userId=? and courseId=?";
        return CRUD_Utils.query(sql, rs -> {
            List<Map<String, Object>> dataList = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> rowData  = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    rowData.put(columnName, columnValue);
                }
                dataList.add(rowData);
            }
            return dataList;
        }, studentId, courseId);
    }

    @Override
    public Map<String, Object> getUser_section(int studentId, int sectionId) throws SQLException, InterruptedException {
        String identifier = studentId + "_" + sectionId;
        HashSet<String> type = new HashSet<>();
        type.add("finished");
        type.add("schedule");
        List<Map<String, Object>> list = DAO_Utils.selection(table, "identifier", identifier, type);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean addUser_section(int studentId, int sectionId, int courseId) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("studentId", studentId);
        map.put("sectionId", sectionId);
        map.put("courseId", courseId);
        map.put("finished", false);
        map.put("schedule", 0);
        return DAO_Utils.add(table, map);
    }

    @Override
    public boolean changeUser_section(String dataType, Object data, String identifier) throws SQLException, InterruptedException {
        String sql = "update user_section set " + dataType + " = ? where identifier=?";
        return (CRUD_Utils.update(sql, data,identifier) == 1);
    }
}
