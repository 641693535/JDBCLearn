package preparedstatement.crud;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-14 22:05
 */
public class PreparedStatementUpdateTest {


    @Test
    public void testUpdate(){
        String sql = "delete from customers where id = ?";
        update(sql, 2);
    }

    /**
     * 通用的增删改方法
     * @param sql 执行语句的部分
     * @param args 填充执行语句的部分
     */
    public void update(String sql,Object ...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //获取数据库的连接
            conn = JDBCUtils.getConnection();
            //获取Statement实例
            ps = conn.prepareStatement(sql);
            //填充SQL语句
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行SQL语句
            ps.execute();
            System.out.println(sql.substring(0, sql.indexOf(" ")) + "数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

    /**
     * 修改数据库中的一条数据
     */
    @Test
    public void test2() throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            //1.连接数据库
            conn = JDBCUtils.getConnection();
//            //--获取连接数据库的基本信息
//            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");
//            Properties pros = new Properties();
//            pros.load(is);
//
//            String user = pros.getProperty("user");
//            String password = pros.getProperty("password");
//            String url = pros.getProperty("url");
//            String driverClass = pros.getProperty("driverClass");
//            //--注册驱动
//            Class.forName(driverClass);
//            //--连接
//            conn = DriverManager.getConnection(url, user, password);

            //2.预编译SQL语句，创建PreparedStatement实例
            String sql = "update customers set name = ? where id = ?";
            ps = conn.prepareStatement(sql);
            //3.填充SQL语句
            ps.setString(1, "小明");
            ps.setInt(2, 19);

            //4.执行PreparedStatement实例
            ps.execute();
            System.out.println(sql.substring(0, sql.indexOf(" ")) + "数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.资源的关闭
            JDBCUtils.closeResource(conn, ps);
//            if (ps != null) {
//                ps.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
        }
    }

    /**
     * 向Customers表中添加一条数据
     */
    @Test
    public void test1() {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            //数据库的连接
            //获取数据库的四个基本信息
            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("jdbc.properties");

            Properties pros = new Properties();
            pros.load(is);

            String user = pros.getProperty("user");
            String password = pros.getProperty("password");
            String url = pros.getProperty("url");
            String driverClass = pros.getProperty("driverClass");

            Class.forName(driverClass);
            conn = DriverManager.getConnection(url, user, password);

            System.out.println(conn);

            //写入数据
            //创建preparedstatement实例
            String sql = "insert into customers(name,email,birth) values(?,?,?)";
            ps = conn.prepareStatement(sql);
            //填充sql语句
            Scanner scan = new Scanner(System.in);
            ps.setString(1, "任小明");
            ps.setString(2, "Renxm@gmail.com");
            //转换Date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date parse = sdf.parse("2001-11-21");
            ps.setDate(3, new java.sql.Date(parse.getTime()));

            //执行SQL语句
            ps.execute();
            System.out.println(sql.substring(0, sql.indexOf(" ")) + "数据成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
