package com.yinkai.dao;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据库的常用操作,数据库连接直接绑定到当前线程。
 * <p>
 * 1、获取数据库连接 {@link #getConnection()}
 * <p>
 * 2、开启事务 {@link #startTransaction()}
 * <p>
 * 3、回滚事务 {@link #rollback()}
 * <p>
 * 4、提交事务 {@link #commit()}
 * <p>
 * 5、关闭数据库连接 {@link #close()}
 *
 *
 */
public class JdbcUtils {

    private static DruidDataSource ds = null;
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<Connection>();

    /**
     * 获取数据源
     */
    static {
       ds =  DataBase.getDruidDataSource();
    }

    /**
     * 从数据源中获取数据库连接
     *
     * @return Connection
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn == null) {
            conn = getDataSource().getConnection();
            threadLocal.set(conn);
        }
        return conn;
    }

    /**
     * 开启事务
     *
     */
    public static void startTransaction() {
        try {
            Connection conn = threadLocal.get();
            if (conn == null) {
                conn = getConnection();
                // 把 conn绑定到当前线程上
                threadLocal.set(conn);
            }
            // 开启事务
            conn.setAutoCommit(false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 回滚事务
     *
     */
    public static void rollback() {
        try {
            // 从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if (conn != null) {
                // 回滚事务
                conn.rollback();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 提交事务
     *
     */
    public static void commit() {
        try {
            // 从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if (conn != null) {
                // 提交事务
                conn.commit();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭数据库连接(注意，这里只是把连接还给数据库连接池)
     *
     */
    public static void close() {
        try {
            // 从当前线程中获取Connection
            Connection conn = threadLocal.get();
            if (conn != null) {
                conn.close();
                // 解除当前线程上绑定conn
                threadLocal.remove();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据源
     */
    public static DataSource getDataSource() {
        // 从数据源中获取数据库连接
        return ds;
    }
}