package com.hjf.demo.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Logger;

public class CRUD_Utils {
    private final static Logger LOGGER = Logger.getLogger(CRUD_Utils.class.toString());
    private CRUD_Utils() {
        throw new AssertionError("This class should not be instantiated.");
    }

    public static boolean checkPlaceholder(String sql,Object... params){
        //获取sql语句中的占位符数量
        long cnt=  sql.chars()
                .filter(c->c=='?')
                .count();

        //比较占位符数量是否与可变参数数量相同
        LOGGER.severe("The number of placeholder should be the same as the number of params");
        return cnt == params.length;
    }

    public static void setParameters(PreparedStatement ps,Object[] params) throws SQLException {
        int num = 1;
        //执行语句
        for (Object param : params) {
            if (param instanceof Object[]) {
                Object[] objects = (Object[]) param;
                for (Object object : objects) {
                    ps.setObject(num, object);
                    num++;
                }
            } else {
                ps.setObject(num, param);
                num++;
            }
        }
    }

    public static int update(String sql,Object... params) throws InterruptedException, SQLException {
        if (checkPlaceholder(sql, params)) {
            //获取链接
            Connection connection = Connection_Pool.getConnection();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                setParameters(ps,params);
                //返回影响的数据的条数
                return ps.executeUpdate();
            } finally {
                //释放资源
                Connection_Pool.returnConnection(connection);
            }
        }else
            return 0;
    }

    public static <T> T query(String sql,MyHandler<T> handler, Object... params) throws InterruptedException, SQLException {
        if (checkPlaceholder(sql, params)) {
            //获取链接
            Connection connection = Connection_Pool.getConnection();
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                //执行语句
                setParameters(ps,params);
                try (ResultSet rs = ps.executeQuery()) {
                    return handler.handle(rs);
                }
            } finally {
                //释放资源
                Connection_Pool.returnConnection(connection);
            }
        }else
            return null;
    }
}
