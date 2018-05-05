package com.yinkai.dao;

import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.GenerousBeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.RowProcessor;
import org.apache.commons.dbutils.handlers.*;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class BaseDAO<T> {
    protected final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public BaseDAO() {
        super();
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    /**
     * 查询单条记录<br>
     * 获取结果集第一行数据，并放入map集合返回<br>
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<String, Object> findOneMap(String sql, Object[] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        return qr.query(JdbcUtils.getConnection(), sql, new MapHandler(), params);
    }

    /**
     * 查询单条记录<br>
     * 用于获取结果集第一行数据，并将其封装到javabean对象返回 <br>
     * 数据库字段名采用的下划线间隔 对应的bean采用的是驼峰命名 所以要用GenerousBeanProcessor来处理映射关系
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public T findOneBean(String sql, Object[] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());
        return qr.query(JdbcUtils.getConnection(), sql, new BeanHandler<T>(entityClass, processor), params);
    }

    /**
     * 查询结果集<br>
     * 将结果集中每一行数据都封装到map里面，然后放入list返回。
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public List<Map<String, Object>> findAllMap(String sql, Object[] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        return qr.query(JdbcUtils.getConnection(), sql, new MapListHandler(), params);
    }

    /**
     * 查询结果集-默认返回当前类的泛型<br>
     * 将结果集中每一行都封装成javabean并存入list返回。
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public List<T> findAllBean(String sql, Object[] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());
        return qr.query(JdbcUtils.getConnection(), sql, new BeanListHandler<T>(entityClass, processor), params);
    }

    /**
     * 查询结果集-自定义泛型<br>
     * 将结果集中每一行都封装成javabean并存入list返回。
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public List findAllBean(String sql, Object[] params, Class clazz) throws SQLException {
        QueryRunner qr = new QueryRunner();
        RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());
        return (List) qr.query(JdbcUtils.getConnection(), sql, new BeanListHandler(clazz, processor), params);

    }

    /**
     * 返回指定列数据<br>
     * colName 如果为空则默认返回第一列数据
     *
     * @param sql
     * @param params
     * @param colName
     * @return
     * @throws SQLException
     */
    public Object findOneCol(String sql, Object[] params, String colName) throws SQLException {
        QueryRunner qr = new QueryRunner();
        if (colName == null || colName.trim().length() == 0) {
            return qr.query(JdbcUtils.getConnection(), sql, new ScalarHandler<Object>(), params);
        } else {
            return qr.query(JdbcUtils.getConnection(), sql, new ScalarHandler<Object>(colName), params);
        }
    }

    /**
     * 插入一条记录，返回影响行数
     *
     * @param sql
     * @param params
     * @throws SQLException
     */
    public int insert1(String sql, Object[] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        return qr.update(JdbcUtils.getConnection(), sql, params);
    }

    /**
     * 插入一条记录，并以map数据格式返回新增的记录。
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public Map<String, Object> insert2(String sql, Object[] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        return qr.insert(JdbcUtils.getConnection(), sql, new MapHandler(), params);
    }

    /**
     * 插入一条记录，并以javabean格式返回新增记录
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public T insert3(String sql, Object[] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());
        return qr.insert(JdbcUtils.getConnection(), sql, new BeanHandler<T>(entityClass, processor), params);
    }

    /**
     * 插入一条记录，并以javabean格式返回新增记录-自定义泛型
     *
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public Object insert4(String sql, Object[] params, Class clazz) throws SQLException {
        QueryRunner qr = new QueryRunner();
        RowProcessor processor = new BasicRowProcessor(new GenerousBeanProcessor());
        return qr.insert(JdbcUtils.getConnection(), sql, new BeanHandler(clazz, processor), params);
    }

    /**
     * 批量插入
     *
     * @param sql
     * @param params
     * @throws SQLException
     */
    public void insertBatch(String sql, Object[][] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        qr.batch(JdbcUtils.getConnection(), sql, params);
    }

    /**
     * 更新
     *
     * @param sql
     * @param params
     * @return 影响记录数
     * @throws SQLException
     */
    public int update(String sql, Object[] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        return qr.update(JdbcUtils.getConnection(), sql, params);
    }

    /**
     * 批量更新
     *
     * @param sql
     * @param params
     * @return 已修改记录数集合
     * @throws SQLException
     */
    public int[] updateBatch(String sql, Object[][] params) throws SQLException {
        QueryRunner qr = new QueryRunner();
        return qr.batch(JdbcUtils.getConnection(), sql, params);
    }

    /**
     * 删除
     *
     * @param sql
     * @param params
     * @return 影响记录数
     * @throws SQLException
     */
    public int delete(String sql, Object[] params) throws SQLException {
        return this.update(sql, params);
    }

    /**
     * 批量删除
     *
     * @param sql
     * @param params
     * @return 已删除记录数集合
     * @throws SQLException
     */
    public int[] deleteBatch(String sql, Object[][] params) throws SQLException {
        return this.updateBatch(sql, params);
    }



}