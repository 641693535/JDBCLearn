package com.transaction;

import com.bean.User;
import com.utils.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-19 11:15
 */
public class TransactionTest {

    @Test
    public void testSelect() {
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            System.out.println(conn.getTransactionIsolation());
            String sql = "select user,password,balance from user_table where user = ?";
            User cc = transactionSelect(conn, User.class, sql, "CC");
            System.out.println(cc);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, null);
        }
    }
    @Test
    public void testUpdate(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "update user_table set balance = ? where user = ?";
            update(conn,sql, 5000,"CC");

            Thread.sleep(15000);
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    /**
     * @param conn 外面传进来的连接，为了保证整个执行过程为同一个连接
     * @param clazz 指定查询的表名
     * @param sql 执行语句
     * @param args 填充执行语句
     * V2.0，考虑到事务
     */
    public <T> T transactionSelect(Connection conn, Class<T> clazz, String sql, Object... args) {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //预备SQL语句
            ps = conn.prepareStatement(sql);

            //填充SQL语句
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行
            rs = ps.executeQuery();
            //获取结果元数据
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            if (rs.next()) {
                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取结果
                    Object resultObject = rs.getObject(i + 1);
                    //获取列名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    //通过反射填充到类中
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, resultObject);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps, rs);
        }
        return null;
    }

    public void update(Connection conn,String sql, Object... args) {

        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, ps);
        }
    }

}
