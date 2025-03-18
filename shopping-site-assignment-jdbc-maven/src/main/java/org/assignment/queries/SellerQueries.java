package org.assignment.queries;

public class SellerQueries {
    private static final String SELECT_BASE_QUERY = " SELECT * FROM SELLER ";
    public static  String getAllSellersQuery(){
        return SELECT_BASE_QUERY;
    }
    public static  String getSellersByColumnQuery(String columns[], String operation){
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
}
