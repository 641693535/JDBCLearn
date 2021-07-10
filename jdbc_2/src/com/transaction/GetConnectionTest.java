package com.transaction;

import com.utils.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-18 22:03
 */
public class GetConnectionTest {

    @Test
    public void testGetConnection() {
        try {
            Connection conn = JDBCUtils.getConnection();
            System.out.println(conn);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testUpdate() {

        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection();
            conn.setAutoCommit(false);
            String sql = "update user_table set balance = balance - 100 where user = ?";
            Update(conn, sql, "AA");

            //模拟阻塞
            System.out.println(10 / 0);

            String sql1 = "update user_table set balance = balance + 100 where user = ?";
            Update(conn, sql1, "BB");

            System.out.println("转账成功！");
            conn.commit();
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }

    public void Update(Connection conn, String sql, Object... args) throws SQLException {

        PreparedStatement ps = conn.prepareStatement(sql);

        for (int i = 0; i < args.length; i++) {
            ps.setObject(i + 1, args[i]);
        }
        ps.executeUpdate();

        JDBCUtils.closeResource(null, ps);

    }

}
