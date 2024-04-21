package com.hjf.demo.Service;

import com.hjf.demo.entity.Part;

import java.util.List;

public interface PartService {
    List<Part> getPartByCourseId(int courseId);
    Part getPart(int id);
    boolean addPart(Part part);
}
