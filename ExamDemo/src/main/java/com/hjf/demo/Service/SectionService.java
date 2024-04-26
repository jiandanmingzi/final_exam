package com.hjf.demo.Service;

import com.hjf.demo.entity.Section;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface SectionService {
    List<Section> getSectionBelowPart(int partId) throws SQLException, InterruptedException;
    boolean addSection(String sectionName, int teacherId, int partId, int sort, String type, int courseId, String path) throws SQLException, InterruptedException;
    boolean deleteSection(int id) throws SQLException, InterruptedException;
    boolean updatePath(String path, int id) throws SQLException, InterruptedException;
    boolean changeData(Map<String, Object> map, int id) throws SQLException, InterruptedException;
    Section getSection(int id) throws SQLException, InterruptedException;
}
