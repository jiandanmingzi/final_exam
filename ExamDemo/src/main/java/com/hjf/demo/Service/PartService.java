package com.hjf.demo.Service;

import com.hjf.demo.entity.Part;

import java.sql.SQLException;
import java.util.List;

public interface PartService {
    List<Part> getPartByCourseId(int courseId) throws SQLException, InterruptedException;
    Part getPart(int id) throws SQLException, InterruptedException;
    Part getPartByIdentifier(String identifier) throws SQLException, InterruptedException;
    boolean addPart(int courseId, String partName, int sort) throws SQLException, InterruptedException;
    boolean deletePart(int id) throws SQLException, InterruptedException;
    boolean changePartName(int id, String partName) throws SQLException, InterruptedException;
}
