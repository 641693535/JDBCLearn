package com.getconnection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-23 19:23
 */
public class C3P0Test {

    @Test
    public void getConnectionTest() throws Exception{
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass( "com.mysql.jdbc.Driver" ); //loads the jdbc driver
        cpds.setJdbcUrl( "jdbc:mysql://localhost:3306/test" );
        cpds.setUser("root");
        cpds.setPassword("toor");

        //设置初始数据库连接数
        cpds.setInitialPoolSize(10);

        //从数据库连接池中获取链接
        Connection conn = cpds.getConnection();
        System.out.println(conn);
        conn.close();

    }

    //使用XML文件
    @Test
    public void testgetConnection1() throws Exception {
        ComboPooledDataSource cpds = new ComboPooledDataSource("helloc3p0");
        Connection conn = cpds.getConnection();
        System.out.println(conn);
        conn.close();
    }


}
