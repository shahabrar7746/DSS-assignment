package org.assignment.queries;

public final class OrderQueries {
    public static  String getAllOrdersQuery(){
        return "SELECT * FROM ORDERS";
    }
    public  static  String getOrdersByColumns(String columns[], String operation){
        StringBuilder builder = new StringBuilder(" SELECT * FROM ORDERS ");
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
        StringBuilder builder = new StringBuilder(" DELETE FROM ORDERS ");
        builder.append(" WHERE ");
        builder.append(orderIdColumnName);
        builder.append(" = ? ");
        return builder.toString();
    }
    public static String addOrderQuery(String columns[]){
        StringBuilder builder = new StringBuilder("INSERT INTO ORDERS ");
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
        return "select orders.* from customer, orders where customer.customer_id = orders.customer_id and orders.status = 'DELIVERED'";
    }
    public static String getOrdersByProductName(){
        return "SELECT ORDERS.* FROM ORDERS, PRODUCT WHERE orders.product_id = product.product_id AND product.product_name = ?";
    }

}
