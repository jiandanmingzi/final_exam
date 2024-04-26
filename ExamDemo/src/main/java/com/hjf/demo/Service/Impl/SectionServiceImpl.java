package com.hjf.demo.Service.Impl;

import com.hjf.demo.Dao.Impl.SectionDaoImpl;
import com.hjf.demo.Dao.SectionDao;
import com.hjf.demo.Service.SectionService;
import com.hjf.demo.entity.Course;
import com.hjf.demo.entity.Section;

import java.sql.SQLException;
import java.util.*;

public class SectionServiceImpl implements SectionService {
    private final SectionDao sectionDao = new SectionDaoImpl();
    @Override
    public List<Section> getSectionBelowPart(int partId) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("partId");
        set.add("id");
        set.add("sectionName");
        set.add("sort");
        set.add("type");
        return sectionDao.select("partId", partId, set);
    }

    @Override
    public boolean addSection(String sectionName, int teacherId, int partId, int sort, String type, int courseId, String path) throws SQLException, InterruptedException {
        Map<String, Object> map = new HashMap<>();
        map.put("sectionName", sectionName);
        map.put("teacherId", teacherId);
        map.put("courseId", courseId);
        map.put("partId", partId);
        map.put("sort", sort);
        map.put("type", type);
        map.put("path", path);
        return sectionDao.add(map);
    }

    @Override
    public boolean deleteSection(int id) throws SQLException, InterruptedException {
        Section section = getSection(id);
        if (section == null) {
            return false;
        }else {
            if (!Objects.equals(section.getPath(), "null")) {
                sectionDao.deleteThroughPath(id, section.getPath());
            }
            sectionDao.delete(id);
            return true;
        }
    }

    @Override
    public boolean updatePath(String path, int id) throws SQLException, InterruptedException {
        Section section = getSection(id);
        if (section == null) {
            return false;
        }else {
            if (!Objects.equals(section.getPath(), "null")) {
                sectionDao.deleteThroughPath(id, section.getPath());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("path", path);
            sectionDao.update(id, map);
            return true;
        }
    }

    @Override
    public boolean changeData(Map<String, Object> map, int id) throws SQLException, InterruptedException {
        Section section = getSection(id);
        if (section == null) {
            return false;
        }else {
            sectionDao.update(id, map);
            return true;
        }
    }


    @Override
    public Section getSection(int id) throws SQLException, InterruptedException {
        HashSet<String> set = new HashSet<>();
        set.add("id");
        set.add("sectionName");
        set.add("sort");
        set.add("type");
        set.add("introduction");
        set.add("path");
        set.add("teacherId");
        set.add("partId");
        set.add("courseId");
        List<Section> list = sectionDao.select("id", id, set);
        if (list!= null && (!list.isEmpty())){
            return list.get(0);
        }
        return null;
    }
}
