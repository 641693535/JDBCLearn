package com.getconnection;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.hamcrest.core.Is;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-26 15:15
 */
public class DBCPTest {

    @Test
    public void testGetConnection() throws SQLException {
        BasicDataSource source = new BasicDataSource();
        //四个基本信息
        source.setDriverClassName("com.mysql.jdbc.Driver");
        source.setUrl("jdbc:mysql:///test");
        source.setUsername("root");
        source.setPassword("toor");

        Connection conn = source.getConnection();
        System.out.println(conn);
        conn.close();
    }
    /*
    使用配置文件
     */
    @Test
    public void testGetConnection1() throws Exception {

        Properties prop = new Properties();
        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbcdbcp.properties");
        prop.load(is);
        DataSource source = BasicDataSourceFactory.createDataSource(prop);
        Connection conn = source.getConnection();
        System.out.println(conn);
        conn.close();
    }

}
