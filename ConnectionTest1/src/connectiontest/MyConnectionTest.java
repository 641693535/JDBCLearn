package connectiontest;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description 连接数据库
 * @create 2020-05-14 17:47
 */
public class MyConnectionTest {

    public static void main(String[] args) throws Exception {

        //获取连接数据库的四个基本信息

        InputStream is = MyConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");

        Properties properties = new Properties();
        properties.load(is);

        String user = properties.getProperty("user");
        String password = properties.getProperty("password");
        String url = properties.getProperty("url");
        String driverClass = properties.getProperty("driverClass");

        Class.forName(driverClass);

        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);
        connection.close();

    }

}
