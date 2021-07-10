package preparedstatement.crud;

import bean.Customers;
import org.junit.Test;

import javax.sound.midi.Soundbank;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-18 16:14
 */
public class PreparedStatementForBlob {

    /**
     * 向表中插入一条Blob数据
     */
    @Test
    public void test1() {
        Connection conn = null;
        PreparedStatement ps = null;
        FileInputStream fis = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "insert into customers(name,email,birth,photo) values(?,?,?,?)";
            ps = conn.prepareStatement(sql);

            ps.setObject(1, "测试图");
            ps.setObject(2, "123123213@gmail.com");
            ps.setObject(3, "1993-05-18");
            fis = new FileInputStream("girl.jpg");
            ps.setBlob(4, fis);

            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JDBCUtils.closeResource(conn, ps);
        }

    }

    /**
     * 向表中读取一条Blob数据
     */
    @Test
    public void test2() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        InputStream bs = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "select id,name,email,photo from customers where id = ?";
            ps = conn.prepareStatement(sql);
            ps.setObject(1, 25);

            rs = ps.executeQuery();
            if (rs.next()) {
                Customers customer = new Customers();
                Object id = rs.getObject("id");
                Object name = rs.getObject("name");
                Object email = rs.getObject("email");
                Blob photo = rs.getBlob("photo");

                bs = photo.getBinaryStream();
                FileOutputStream fos = new FileOutputStream((String) name + ".jpg");
                int len;
                byte[] b = new byte[1024];
                while ((len = bs.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
            try {
                bs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用PreparedStatement批量插入
     * 方式一：速度快于Statement！
     */
    @Test
    public void test3() {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();

            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);

            for (int i = 1; i <= 10000; i++) {
                ps.setObject(1, "name_" + i);
                ps.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为：" + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
    /**
     * 使用PreparedStatement批量插入
     * 方式二：速度快于PreparedStatement方式一！
     */
    @Test
    public void test4(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 100000; i++) {
                ps.setObject(1, "name_" + i);

                //1.攒SQL
                ps.addBatch();

                if (i % 500 == 0) {
                    //2.执行SQL
                    ps.executeBatch();
                    //3.清除SQL
                    ps.clearBatch();
                }
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为：" + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }
    /**
     * 使用PreparedStatement批量插入
     * 方式三：速度快于PreparedStatement方式二！
     */
    @Test
    public void test5(){
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            long start = System.currentTimeMillis();
            conn = JDBCUtils.getConnection();

            //设置不允许自动提交
            conn.setAutoCommit(false);

            String sql = "insert into goods(name) values(?)";
            ps = conn.prepareStatement(sql);
            for (int i = 1; i <= 100000; i++) {
                ps.setObject(1, "name_" + i);

                //1.攒SQL
                ps.addBatch();

                if (i % 500 == 0) {
                    //2.执行SQL
                    ps.executeBatch();
                    //3.清除SQL
                    ps.clearBatch();
                }
            }
            //提交
            conn.commit();
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为：" + (end - start));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
    }

}
