package org.assignment.queries;

public class SellerQueries {
    public static  String getAllSellersQuery(){
        return "SELECT * FROM SELLER";
    }
    public static  String getSellersByColumnQuery(String columns[], String operation){
        StringBuilder builder = new StringBuilder(" SELECT * FROM SELLER ");
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
}
