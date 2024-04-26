package com.hjf.demo.entity;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Course {
    private int id;
    private int sectionNum;
    private int teacherId;
    private int exerciseNum;
    private int exercisesNum;
    private String courseName;
    private int maxStudent;
    private int student;
    private String teacherName;
    private String introduction;
    private boolean ready;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private final ReentrantLock lock = new ReentrantLock();
    private final ReentrantLock sectionLock = new ReentrantLock();
    private final ReentrantLock exerciseLock = new ReentrantLock();
    private final ReentrantLock exercisesLock = new ReentrantLock();

    public Course(){}

    public Course(int sectionNum, int teacherId, int exerciseNum, int exercisesNum, String courseName, int maxStudent, int student, String teacherName, String introduction, boolean ready, LocalDateTime startDate, LocalDateTime endDate) {
        this.sectionNum = sectionNum;
        this.teacherId = teacherId;
        this.exerciseNum = exerciseNum;
        this.exercisesNum = exercisesNum;
        this.courseName = courseName;
        this.maxStudent = maxStudent;
        this.student = student;
        this.teacherName = teacherName;
        this.introduction = introduction;
        this.ready = ready;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getExercisesNum() {
        return exercisesNum;
    }

    public void setExercisesNum(int exercisesNum) {
        this.exercisesNum = exercisesNum;
    }

    public void exercisesNumUP() throws InterruptedException {
        if (exercisesLock.tryLock(1, TimeUnit.SECONDS)){
            this.exercisesNum++;
            exercisesLock.unlock();
        }
    }

    public void exercisesNumDOWN() throws InterruptedException {
        if (exercisesLock.tryLock(1, TimeUnit.SECONDS)){
            this.exercisesNum--;
            exercisesLock.unlock();
        }
    }

    public int getExerciseNum() {
        return exerciseNum;
    }

    public void setExerciseNum(int exerciseNum) {
        this.exerciseNum = exerciseNum;
    }

    public void exerciseNumUP() throws InterruptedException {
        if (exerciseLock.tryLock(1, TimeUnit.SECONDS)){
            this.exerciseNum++;
            exerciseLock.unlock();
        }
    }

    public void exerciseNumDOWN() throws InterruptedException {
        if (exerciseLock.tryLock(1, TimeUnit.SECONDS)){
            this.exerciseNum--;
            exerciseLock.unlock();
        }
    }

    public int getSectionNum() {
        return sectionNum;
    }

    public void setSectionNum(int sectionNum) {
        this.sectionNum = sectionNum;
    }

    public void sectionNumUP() throws InterruptedException {
        if (sectionLock.tryLock(1, TimeUnit.SECONDS)) {
            this.sectionNum++;
            sectionLock.unlock();
        }
    }

    public void sectionNumDOWN() throws InterruptedException {
        if(sectionLock.tryLock(1, TimeUnit.SECONDS)) {
            this.sectionNum--;
            sectionLock.unlock();
        }
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
                lock.unlock();
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
