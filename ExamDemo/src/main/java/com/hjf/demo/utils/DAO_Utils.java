package com.hjf.demo.utils;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

public class DAO_Utils {
    public static List<Map<String, Object>> selection(String table, String dataType, Object data,HashSet<String> type) throws SQLException, InterruptedException {
        var stringJoiner = new StringJoiner(", ","SELECT "," FROM " + table + " WHERE " + dataType + " = ?");
        for (String string:type){
            stringJoiner.add(string);
        }
        return CRUD_Utils.query(stringJoiner.toString(),rs -> {
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
        }, data);
    }

    public static boolean add(String table, Map<String, Object> map) throws SQLException, InterruptedException {
        var keyJoiner = new StringJoiner(", ","INSERT INTO " + table + " ( "," ) VALUES ");
        var valueJoiner = new StringJoiner(", ","( "," )");
        Set<String> keys = map.keySet();
        List<Object> keyValueList = new ArrayList<>();
        for (String key : keys) {
            keyJoiner.add("?");
            valueJoiner.add("?");
            keyValueList.add(key);
            keyValueList.add(map.get(key));
        }
        String sql = keyJoiner.toString() + valueJoiner;
        Object[] objects = keyValueList.toArray(new Object[0]);
        return (CRUD_Utils.update(sql, objects) == 1);
    }

    public static boolean update(String table, int id ,Map<String, Object> map) throws SQLException, InterruptedException {
        var stringJoiner = new StringJoiner(" , ","UPDATE " + table + " SET ","WHERE id = ?");
        Set<String> keys = map.keySet();
        List<Object> keyValueList = new ArrayList<>();
        for (String key : keys) {
            String sql = key + " = ?";
            stringJoiner.add(sql);
            keyValueList.add(map.get(key));
        }
        Object[] objects = keyValueList.toArray(new Object[0]);
        return (CRUD_Utils.update(stringJoiner.toString(), objects,id) == 1);
    }

    public static boolean delete(String table, int id) throws SQLException, InterruptedException {
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        return (CRUD_Utils.update(sql, id) == 1);
    }

    public static void deleteAll(String table, String dataType, Object data) throws SQLException, InterruptedException {
        String sql = "DELETE FROM " + table + " WHERE " + dataType + " = ?";
        CRUD_Utils.update(sql, data);
    }
}
