package preparedstatement.crud;

import bean.Customers;
import bean.Order;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-17 13:58
 */
public class GetForList {

    @Test
    public void testGetList(){
        String sql = "select id,name,email from customers where id < ?";
        List<Customers> customers = testGetForList(Customers.class, sql, 10);
        customers.forEach(System.out::println);

        String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id < ?";
        List<Order> orders = testGetForList(Order.class, sql1, 4);
        orders.forEach(System.out::println);
    }

    /**
     * 查询多行数据
     */
    public <T> List<T> testGetForList(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //获取SQl连接
            conn = JDBCUtils.getConnection();
            //预装SQL语句
            ps = conn.prepareStatement(sql);
            //填充SQL语句
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行并获取数据
            rs = ps.executeQuery();
            //获取执行结果中的元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            //创建返回的数组集合
            ArrayList<T> list = new ArrayList<>();
            //获取执行结果
            while (rs.next()) {
                //创建对象
                T t = clazz.getDeclaredConstructor().newInstance();
                //给t对象的各个属性赋值
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = rs.getObject(i + 1);
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //通过反射给t对象的ColumnName属性赋值columnValue值
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

}
