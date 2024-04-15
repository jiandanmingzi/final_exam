package com.hjf.demo.config;

import com.hjf.demo.exception.ConfigLoadingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final String driver;
    private static final String url;
    private static final String userName;
    private static final String passWord;
    private static final int maxConnections;
    static {
        Properties properties = new Properties();
        try (InputStream in = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                properties.load(in);
            }
        }catch (IOException e) {
            throw new ConfigLoadingException("Error loading config(Now using default config):" + e);
        } finally {
            driver = properties.getProperty("driver", "com.mysql.cj.jdbc.Driver");
            url = properties.getProperty("url", "jdbc:mysql://localhost:3306/demo");
            userName = properties.getProperty("userName", "root");
            passWord = properties.getProperty("passWord", "1234");
            maxConnections = Integer.parseInt(properties.getProperty("maxConnections", "50"));
        }
    }

    public static String getDriver() {
        return driver;
    }

    public static String getUrl() {
        return url;
    }

    public static String getUserName() {
        return userName;
    }

    public static String getPassWord() {
        return passWord;
    }

    public static int getMaxConnections() {
        return maxConnections;
    }

    private ConfigReader(){
        throw new IllegalStateException();
    }
}
