package exer;

import preparedstatement.crud.JDBCUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.SimpleFormatter;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description 从控制台向数据库的表Customers中插入一条数据
 * @create 2020-05-17 18:50
 */
public class Exer1 {

    public static void main(String[] args) {
        Exer1 e = new Exer1();
        Scanner scan = new Scanner(System.in);

        String sql = "insert into customers(name,email,birth) values(?,?,?)";
        System.out.print("请输入名字：");
        String name = scan.next();
        System.out.print("请输入邮箱：");
        String email = scan.next();
        System.out.print("请输入生日(年-月-日)：");
        String birth = scan.next();
        int isTrue = e.updateSql(sql, name, email, birth);
        if (isTrue > 0) {
            System.out.println(sql.substring(0, sql.indexOf(" ")) + "成功!");
        }else{
            System.out.println(sql.substring(0, sql.indexOf(" ")) + "失败。");
        }

    }

    public int updateSql(String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = JDBCUtils.getConnection();

            ps = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

//            ps.execute();
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps);
        }
        return 0;
    }

}
