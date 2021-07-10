package com.getconnection;

import com.utils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-26 17:43
 */
public class DruidTest {

    @Test
    public void testGetConnection() throws SQLException {
        Connection conn = JDBCUtils.getConnection1();
        System.out.println(conn);
    }

}
