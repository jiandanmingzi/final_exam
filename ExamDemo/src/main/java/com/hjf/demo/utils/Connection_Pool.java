package com.hjf.demo.utils;

import com.hjf.demo.config.ConfigReader;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class Connection_Pool {
    static volatile Queue<Connection> connections = new LinkedList<Connection>();
    private static final ReentrantLock lock = new ReentrantLock();
    private static final int maxConnections;

    static {
        maxConnections = ConfigReader.getMaxConnections();
        for (int i = 0; i < maxConnections; i++)
        {
            try {
                connections.add(JDBC_Utils.getConnection());
            } catch (SQLException e) {

            }
        }
    }

    public static Connection getConnection() throws SQLException, InterruptedException {
        Connection connection = null;
        if (connections.isEmpty())
            connection = JDBC_Utils.getConnection();
        else {
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    if (connections.isEmpty())
                        connection = JDBC_Utils.getConnection();
                    else
                        connection = connections.poll();
                } finally {
                    lock.unlock();
                }
            } else
                connection = JDBC_Utils.getConnection();
        }
        return connection;
    }

    public static void returnConnection(Connection connection) throws SQLException, InterruptedException {
        if (connection!= null) {
            if (connections.size()<maxConnections)
                if (lock.tryLock(1, TimeUnit.SECONDS))
                {
                    if (connections.size()<maxConnections)
                        try {
                            connections.add(connection);
                        } finally {
                            lock.unlock();
                        }
                    else connection.close();
                }
            else connection.close();
        }
    }

    private Connection_Pool(){
        throw new AssertionError();
    }
}
