package com.yinkai.dao;

import com.alibaba.druid.pool.DruidDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


public class DataBase {


    private static String dataType = null;

    private static DruidDataSource druidDataSource;

    static {
        //Class.getResourceAsStream() 会指定要加载的资源路径与当前类所在包的路径一致。
        //ClassLoader.getResourceAsStream()  无论要查找的资源前面是否带'/' 都会从classpath的根路径下查找。
        InputStream in = DataBase.class.getResourceAsStream("/database.properties");

        try {
            Properties properties = new Properties();
            properties.load(in);

            dataType = properties.getProperty("jdbc.database.type");

            druidDataSource = new DruidDataSource();
            druidDataSource.setDriverClassName(properties.getProperty("jdbc.driver"));
            druidDataSource.setUsername(properties.getProperty("jdbc.user"));
            druidDataSource.setPassword(properties.getProperty("jdbc.password"));
            druidDataSource.setUrl(properties.getProperty("jdbc.url"));
            druidDataSource.setInitialSize(Integer.parseInt(properties.getProperty("jdbc.initialSize")));
            druidDataSource.setMaxActive(Integer.parseInt(properties.getProperty("jdbc.maxActive")));
            druidDataSource.setMinIdle(Integer.parseInt(properties.getProperty("jdbc.minIdle")));
            druidDataSource.setMaxWait(Integer.parseInt(properties.getProperty("jdbc.maxWait")));
            //设置sql监控，防火墙，日志记录
            druidDataSource.setFilters("stat,wall,log4j");
            //设置每个连接上PSCache的大小
            druidDataSource.setPoolPreparedStatements(true);
            druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(20);

        } catch (Exception e) {
            System.out.println("找不到database.properties配置文件");
        }
    }


    /**
     * 获取数据库类型 MYSQL DB2 ORACLE SQLSERVER SYSBASE KINGBASE
     *
     * @return String
     */
    public static String getDataBaseType() {
        return dataType;
    }

    public static  DruidDataSource getDruidDataSource() {
        return druidDataSource;
    }
    /**
     * 开启事务
     */
    public void startTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                System.out.println("事务开启失败");

            }
        }
    }

    /**
     * 事务结束方法，调用连接的commit方法
     */
    public void commitTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.commit();
            } catch (SQLException e) {
                System.out.println("事务提交失败");
            }
        }
    }


}
