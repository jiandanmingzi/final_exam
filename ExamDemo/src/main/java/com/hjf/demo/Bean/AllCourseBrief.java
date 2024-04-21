package com.hjf.demo.Bean;

import com.hjf.demo.Dao.CourseDao;
import com.hjf.demo.Dao.Impl.CourseDaoImpl;
import com.hjf.demo.entity.Course;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AllCourseBrief {
    private final CourseDao courseDao = new CourseDaoImpl();
    private List<Course> courses;
    protected AllCourseBrief() throws SQLException, InterruptedException {
        this.initialized();
    }

    private void initialized() throws SQLException, InterruptedException {
        HashSet<String> types = new HashSet<>();
        types.add("id");
        types.add("teacherId");
        types.add("teacherName");
        types.add("maxStudent");
        types.add("student");
        types.add("courseName");
        types.add("startDate");
        types.add("endDate");
        types.add("ready");
        types.add("introduction");
        this.courses = courseDao.select("ready", true, types);
    }

    public List<Course> getSomeCourses(int start, int num) {
        List<Course> courseList;
        if (courses.size() > start) {
            int end = Math.min(courses.size(), start + num);
            courseList = this.courses.subList(start, end);
        }else {
            courseList = new ArrayList<>();
        }
        return courseList;
    }

    public int CourseCount() {
        return this.courses.size();
    }

    public Course getCourse(int id){
        for (Course course : courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null;
    }

    public List<Course> getCourses(int teacherId) {
        List<Course> courses = new ArrayList<>();
        for (Course course : this.courses) {
            if (course.getTeacherId() == teacherId) {
                courses.add(course);
            }
        }
        return courses;
    }

    public int getCourseIdByName(String name) {
        for (Course course : courses) {
            if (course.getCourseName().equals(name)) {
                return course.getId();
            }
        }
        return 0;
    }
}
