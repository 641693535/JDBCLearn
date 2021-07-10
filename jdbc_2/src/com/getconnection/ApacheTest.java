package com.getconnection;

import com.bean.Customers;
import com.utils.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Handler;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-26 17:45
 */
public class ApacheTest {

    @Test
    public void testInsert(){
        Connection conn = null;
        try {
            conn = JDBCUtils.getConnection1();
            QueryRunner runner = new QueryRunner();
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            int insertCount = runner.update(conn, sql, "蔡徐坤", "caixukun@qq.com", "1996-09-08");
            System.out.println("成功添加了" + insertCount + "条记录！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, null);
        }
    }


    @Test
    public void testSearch1() throws SQLException {
        QueryRunner runner = new QueryRunner();
        Connection conn = JDBCUtils.getConnection1();

        String sql = "select id,name,birth,email from customers where id = ?";

        BeanHandler<Customers> handler = new BeanHandler<>(Customers.class);

        Customers query = runner.query(conn, sql, handler, 24);
        System.out.println(query);
        JDBCUtils.closeResource(conn, null);
    }
}
