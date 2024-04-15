package com.hjf.demo.utils;

import com.hjf.demo.config.ConfigReader;
import com.hjf.demo.exception.DriverLoadingException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class JDBC_Utils {
    private static final Logger LOGGER = Logger.getLogger(JDBC_Utils.class.getName());
    private static final String userName;
    private static final String passWord;
    private static final String url;

    static {
        //获取配置
        url= ConfigReader.getUrl();
        userName= ConfigReader.getUserName();
        passWord= ConfigReader.getPassWord();
        String driver= ConfigReader.getDriver();

        //加载驱动
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new DriverLoadingException("Error loading driver", e);
        }

    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, userName, passWord);
    }

    private JDBC_Utils(){
        throw new IllegalStateException("Don't call");
    }
}
