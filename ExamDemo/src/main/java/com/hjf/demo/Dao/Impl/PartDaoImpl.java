package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Dao.PartDao;
import com.hjf.demo.entity.Part;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PartDaoImpl implements PartDao {
    private final String table = "part";
    @Override
    public List<Part> select(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException {
        List<Map<String, Object>> Data = DAO_Utils.selection(table,dataType, data, type);
        List<Part> list = null;
        if (Data != null && (!Data.isEmpty())) {
            list = new ArrayList<>();
            for (Map<String, Object> map : Data){
                Part part = new Part();
                if (type.contains("identifier")){
                    part.setIdentifier((String) map.get("identifier"));
                }
                if (type.contains("partName")){
                    part.setPartName((String) map.get("partName"));
                }
                if (type.contains("courseId")){
                    part.setCourseId((Integer) map.get("courseId"));
                }
                if (type.contains("sort")){
                    part.setSort((Integer) map.get("sort"));
                }
                if (type.contains("id")){
                    part.setId((Integer) map.get("id"));
                }
                list.add(part);
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
