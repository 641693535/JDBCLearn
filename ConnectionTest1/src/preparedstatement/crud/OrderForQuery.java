package preparedstatement.crud;

import bean.Order;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

/**
 * @author RZBlegion    Email:641693535@qq.com
 * @Description
 * @create 2020-05-16 19:57
 */
public class OrderForQuery {

    @Test
    public void testQueryForOrder(){
        String sql = "select order_id orderId,order_name orderName from `Order` where order_id = ?";
        Order order = queryForOrder(sql, 1);
        System.out.println(order);

        sql = "select order_id orderId,order_name orderName,order_date orderDate from `Order` where order_id = ?";
        order = queryForOrder(sql, 2);
        System.out.println(order);
    }

    /**
     * 对于表的通用的内容查询
     */
    public Order queryForOrder(String sql,Object...args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            //创建与数据库的连接
            conn = JDBCUtils.getConnection();

            //预备SQL语句
            ps = conn.prepareStatement(sql);
            //填充SQL语句
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
            //执行得到一个结果集
            rs = ps.executeQuery();
            //获取执行元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            //读取返回值
            if (rs.next()) {
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    //获取每一列的值
                    Object columnValue = rs.getObject(i + 1);

                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //通过发射保存在Order对象中
                    Field field = Order.class.getDeclaredField(columnLabel);
                    field.setAccessible(true);
                    field.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(conn, ps, rs);
        }
        return null;
    }

}
