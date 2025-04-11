package org.assignment.queries;

import java.util.stream.IntStream;

public final class OrderQueries {
    public static final String SELECT_BASE_QUERY = " SELECT * FROM orders ";
    private static final String DELETE_BASE_QUERY = " DELETE FROM orders ";
    private static final String INSERT_BASE_QUERY = " INSERT INTO orders ";

    public static  String getAllOrdersQuery(){
        return SELECT_BASE_QUERY;
    }


    public  static  String getOrdersByColumns(String columns[], String operation){
        StringBuilder builder = new StringBuilder(SELECT_BASE_QUERY);
        builder.append(" WHERE ");
        builder.append(columns[0]);
        builder.append(" = ? ");
        IntStream.range(1, columns.length).forEach(index -> {
            builder.append(" " + operation + " ");
            builder.append(columns[index]);
            builder.append(" = ? ");
        });
        return builder.toString();
    }
    public static  String deleteOrderQuery(String orderId){
        StringBuilder builder = new StringBuilder(DELETE_BASE_QUERY);
        builder.append(" WHERE ");
        builder.append(orderId);
        builder.append(" = ? ");
        return builder.toString();
    }
    public static String addOrderQuery(String columns[]){
        StringBuilder builder = new StringBuilder(INSERT_BASE_QUERY);
        builder.append("(");
        builder.append(columns[0]);
        for (int i =1;i<columns.length;i++){
            builder.append(",");
            builder.append(columns[i]);
        }
        builder.append(" ) ");
        builder.append(" VALUES( ?");
        for(int i =1;i<columns.length;i++){
            builder.append(",?");
        }
        builder.append(" ) ");
        return builder.toString();
    }
    public static String deliveredOrderQuery(){
        return "SELECT orders.* FROM user, orders WHERE user.customer_id = orders.customer_id AND orders.status = 'DELIVERED'";
    }
    public static String getOrdersByProductAndCustomer(){
        return "SELECT * FROM orders WHERE orders.customer_id = ? AND orders.product_id = ?";
    }

}
