package com.hjf.demo.Dao.Impl;

import com.hjf.demo.Dao.SectionDao;
import com.hjf.demo.entity.Section;
import com.hjf.demo.utils.DAO_Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SectionDaoImpl implements SectionDao {
    private static final String table = "section";
    @Override
    public List<Section> select(String dataType, Object data, HashSet<String> type) throws SQLException, InterruptedException {
        List<Map<String, Object>> Data = DAO_Utils.selection(table,dataType, data, type);
        List<Section> list = null;
        if (Data != null &&(!Data.isEmpty())){
            list = new ArrayList<>();
            for (Map<String, Object> map : Data){
               Section section = new Section();
               if (type.contains("introduction")){
                   section.setIntroduction((String) map.get("introduction"));
               }
               if (type.contains("sectionName")){
                   section.setSectionName((String) map.get("sectionName"));
               }
               if (type.contains("teacherId")){
                   section.setTeacherId((int) map.get("teacherId"));
               }
               if (type.contains("courseId")){
                   section.setCourseId((int) map.get("courseId"));
               }
               if (type.contains("partId")){
                   section.setPartId((int) map.get("partId"));
               }
               if (type.contains("type")){
                   section.setType((String) map.get("type"));
               }
               if (type.contains("path")){
                   section.setPath((String) map.get("path"));
               }
               if (type.contains("sort")){
                   section.setSort((int) map.get("sort"));
               }
               if (type.contains("id")){
                   section.setId((int) map.get("id"));
               }
               list.add(section);
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

    @Override
    public boolean deleteThroughPath(int id, String path) {
        return false;
    }
}
