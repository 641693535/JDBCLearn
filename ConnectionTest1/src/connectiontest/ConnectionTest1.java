package connectiontest;

import jdk.jfr.StackTrace;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-13 15:57
 */
public class ConnectionTest1 {

    //连接的方式一：
    @Test
    public void testConnection1() throws SQLException {
        //获取Driver类的实例
        Driver driver = new com.mysql.jdbc.Driver();

        //注册驱动
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        //连接的基本信息
        info.setProperty("user", "root");
        info.setProperty("password", "toor");
        //连接
        Connection conn = driver.connect(url, info);
        System.out.println(conn);
        conn.close();
    }
    //连接的方式二：在方式一的基础上迭代,使用反射
    @Test
    public void testConnection2() throws Exception {

        //获取类的实例
        Class clazz = Class.forName("com.mysql.jdbc.Driver");

        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        //注册驱动
        String url = "jdbc:mysql://localhost:3306/test";
        Properties info = new Properties();
        //连接的基本信息
        info.setProperty("user", "root");
        info.setProperty("password", "toor");

        //连接
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
        connect.close();
    }

    //方式三：使用DriverManager来代替Driver
    @Test
    public void testConnection3() throws Exception {
        //1.提供连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "toor";

        //2.获取Driver的实例对象
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.getDeclaredConstructor().newInstance();

        //3.注册驱动
        DriverManager.registerDriver(driver);

        //4.连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
        connection.close();

    }

    //方式四：
    @Test
    public void testConnection4() throws Exception {

        //1.提供连接的基本信息
        String url = "jdbc:mysql://localhost:3306/test";
        String user = "root";
        String password = "toor";

        //2.加载Driver驱动
        Class.forName("com.mysql.jdbc.Driver");

        //3.连接数据库
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
        conn.close();
    }

    //方式五：最终版！将连接数据库需要的基本信息封装在配置文件中，读取配置文件，获取连接信息。
    @Test
    public void testConnection5() throws Exception {

        //1.读取配置文件中的四个基本信息
        InputStream is = ConnectionTest1.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties pros = new Properties();
        pros.load(is);

        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverClass = pros.getProperty("driverClass");

        //注册驱动
        Class.forName(driverClass);
        //连接数据库
        Connection conn = DriverManager.getConnection(url, user, password);
        System.out.println(conn);
        conn.close();
    }


}
