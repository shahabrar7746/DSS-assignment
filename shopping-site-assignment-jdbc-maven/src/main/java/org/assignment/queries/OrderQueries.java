package org.assignment.queries;

public final class OrderQueries {
    private static final String SELECT_BASE_QUERY = " SELECT * FROM orders ";
    private static final String     DELETE_BASE_QUERY = " DELETE FROM orders ";
    private static final String INSERT_BASE_QUERY = " INSERT INTO orders ";
    public static  String getAllOrdersQuery(){
        return SELECT_BASE_QUERY;
    }
    public  static  String getOrdersByColumns(String columns[], String operation){
        StringBuilder builder = new StringBuilder(SELECT_BASE_QUERY);
        builder.append(" WHERE ");
        builder.append(columns[0]);
        builder.append(" = ? ");
        for(int i = 1;i<columns.length;i++) {
            builder.append(" " + operation + " ");
            builder.append(columns[i]);
            builder.append(" = ? ");
        }
        return builder.toString();
    }
    public static  String deleteOrderQuery(String orderIdColumnName){
        StringBuilder builder = new StringBuilder(DELETE_BASE_QUERY);
        builder.append(" WHERE ");
        builder.append(orderIdColumnName);
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
        return "SELECT orders.* FROM customer, orders WHERE customer.customer_id = orders.customer_id AND orders.status = 'DELIVERED'";
    }
    public static String getOrdersByProductName(){
        return "SELECT orders.* FROM orders, product WHERE orders.product_id = product.product_id AND product.product_name = ?";
    }

}
