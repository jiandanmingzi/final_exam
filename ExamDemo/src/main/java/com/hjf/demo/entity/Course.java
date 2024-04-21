package com.hjf.demo.entity;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Course {
    private int id;
    private int teacherId;
    private String courseName;
    private int maxStudent;
    private int student;
    private String teacherName;
    private String introduction;
    private boolean ready;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private final ReentrantLock lock = new ReentrantLock();

    public Course(){}

    public Course(int teacherId, String courseName, int student, int maxStudent, String teacherName, String introduction, LocalDateTime startDate, LocalDateTime endDate, boolean ready) {
        this.teacherId = teacherId;
        this.courseName = courseName;
        this.maxStudent = maxStudent;
        this.teacherName = teacherName;
        this.introduction = introduction;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getMaxStudent() {
        return maxStudent;
    }

    public void setMaxStudent(int maxStudent) {
        this.maxStudent = maxStudent;
    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }

    public boolean tryAddStudent() throws InterruptedException {
        if (this.maxStudent > this.student) {
            if(lock.tryLock(1, TimeUnit.SECONDS)){
                if (this.maxStudent > this.student) {
                    this.student++;
                    return true;
                }
                lock.unlock();
            }
        }
        return false;
    }

    public boolean tryRemoveStudent() throws InterruptedException {
        if (this.student > 0) {
            if(lock.tryLock(1, TimeUnit.SECONDS)){
                if (this.student > 0) {
                    this.student--;
                    return true;
                }
            }
        }
        return false;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

}
