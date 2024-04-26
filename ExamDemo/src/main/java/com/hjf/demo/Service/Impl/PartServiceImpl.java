package com.hjf.demo.Service.Impl;

import com.hjf.demo.Dao.Impl.PartDaoImpl;
import com.hjf.demo.Dao.PartDao;
import com.hjf.demo.Service.PartService;
import com.hjf.demo.entity.Part;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class PartServiceImpl implements PartService {
    private final PartDao partDao = new PartDaoImpl();
    @Override
    public List<Part> getPartByCourseId(int courseId) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("courseId");
        set.add("id");
        set.add("partName");
        set.add("sort");
        set.add("hasExercises");
        return partDao.select("courseId", courseId, set);
    }

    @Override
    public Part getPart(int id) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("courseId");
        set.add("id");
        set.add("partName");
        set.add("sort");
        set.add("hasExercises");
        List<Part> parts = partDao.select("courseId", id, set);
        if (parts != null && !parts.isEmpty()){
            return parts.get(0);
        }
        return null;
    }

    @Override
    public Part getPartByIdentifier(String identifier) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("id");
        List<Part> parts = partDao.select("identifier", identifier,set);
        if (parts != null && !parts.isEmpty()){
            return parts.get(0);
        }
        return null;
    }

    @Override
    public boolean addPart(int courseId, String partName, int sort) throws SQLException, InterruptedException {
        String identifier = courseId + "-" + sort;
        if (getPartByIdentifier(identifier) == null){
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", courseId);
            map.put("sort", sort);
            map.put("partName", partName);
            return partDao.add(map);
        }
        return false;
    }

    @Override
    public boolean deletePart(int id) throws SQLException, InterruptedException {
        return (partDao.delete(id));
    }

    @Override
    public boolean changePartName(int id, String partName) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("partName", partName);
        return partDao.update(id, map);
    }

    @Override
    public void setPartExercise(int id, boolean hasExercises) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("hasExercises", hasExercises);
        partDao.update(id, map);
    }
}
