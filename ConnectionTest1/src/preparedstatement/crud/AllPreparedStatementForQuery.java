package preparedstatement.crud;

import bean.Customers;
import bean.Order;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description 通用的查询表中的单条数据的类
 * @create 2020-05-16 21:40
 */
public class AllPreparedStatementForQuery {

    /**
     * 测试通用查询表数据的方法
     */
    @Test
    public void testPreparedStatementForQuery(){
        String sql = "select id,name,email,birth from customers where id = ?";
        Customers customers = PreparedStatementForQuery(Customers.class, sql, 13);
        System.out.println(customers);

        String sql1 = "select order_id orderId,order_name orderName,order_date orderDate from `order` where order_id = ?";
        Order order = PreparedStatementForQuery(Order.class, sql1, 4);
        System.out.println(order);
    }

    /**
     * 通用的查询表中的一条数据的方法
     */
    public <T> T PreparedStatementForQuery(Class<T> clazz, String sql, Object... args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            //创建与数据库的连接
            conn = JDBCUtils.getConnection();

            //预编译SQL语句
            ps = conn.prepareStatement(sql);

            //填充SQL语句的占位符
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }

            //执行SQL语句并获取返回结果
            rs = ps.executeQuery();
            //获取执行的元数据
            ResultSetMetaData metaData = rs.getMetaData();
            //获取结果的值有几列
            int columnCount = metaData.getColumnCount();
            if (rs.next()) {
                //创建结果的对象
                T t = clazz.getDeclaredConstructor().newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取指定列的值
                    Object columnValue = rs.getObject(i + 1);
                    //获取列名
                    String columnLabel = metaData.getColumnLabel(i + 1);

                    //通过反射将列的值封装在对象相同列明的属性中
                    Field field = clazz.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;

    }

}
