package com.hjf.demo.Bean;

import java.sql.SQLException;

public class AllCourseFactory {
    private static AllCourseBrief instance = null;
    public static AllCourseBrief getInstance() throws SQLException, InterruptedException {
        if(instance == null){
            instance = new AllCourseBrief();
        }
        return instance;
    }
}
