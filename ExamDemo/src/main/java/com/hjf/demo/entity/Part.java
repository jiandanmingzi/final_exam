package com.hjf.demo.entity;

public class Part {
    private int id;
    private String partName;
    private int courseId;
    private int sort;
    private String identifier;

    public int getCourseId() {
        return courseId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Part(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
