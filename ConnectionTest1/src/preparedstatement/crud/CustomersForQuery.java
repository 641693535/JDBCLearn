package preparedstatement.crud;

import bean.Customers;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-15 22:51
 */
public class CustomersForQuery {

    @Test
    public void testQueryForCustomers(){
        String sql = "select `id`,`name`,`birth`,`email` from customers where id = ?";
        Customers customers = queryForCustomers(sql, 18);
        System.out.println(customers);

        sql = "select `id`,`name` from customers where name = ?";
        Customers resultSelect = queryForCustomers(sql, "任小明");
        System.out.println(resultSelect);

    }

    /**
     * 对于表的通用的内容查询
     */
    public Customers queryForCustomers(String sql,Object...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //创建连接
            conn = JDBCUtils.getConnection();

            //预备SQL语句
            ps = conn.prepareStatement(sql);
            //填充SQL语句
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行
            rs = ps.executeQuery();
            //获取执行的元数据
            ResultSetMetaData metaData = rs.getMetaData();
            //从执行的元数据中获取列数
            int columnCount = metaData.getColumnCount();
            if (rs.next()) {
                //创建一个Customers对象
                Customers cust = new Customers();
                for (int i = 0; i < columnCount; i++) {
                    //获取每一列的值
                    Object columnValue = rs.getObject(i + 1);
                    //获取每一列的名字
                    String columnName = metaData.getColumnName(i + 1);

                    //将ColumnValue放入Customer的ColumnName中.通过反射
                    Field field = Customers.class.getDeclaredField(columnName);
                    field.setAccessible(true);
                    field.set(cust, columnValue);
                }
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }


    /**
     * 对于表的内容的查询
     */
    @Test
    public void test1() {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();

            String sql = "select id,`name`,email,`birth` from customers where id = ?";
            ps = conn.prepareStatement(sql);

            ps.setObject(1, 1);
            rs = ps.executeQuery();

            if(rs.next()){
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String email = rs.getString(3);
                Date date = rs.getDate(4);

                Customers customers = new Customers(id, name, email, date);
                System.out.println(customers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
    }

}
