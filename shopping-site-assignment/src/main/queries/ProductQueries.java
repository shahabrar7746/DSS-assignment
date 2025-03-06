package main.queries;

public class ProductQueries {
    public static  String getAllProducts(){
        return "SELECT * FROM PRODUCT";
    }
    public static  String getProductsByColumns(String columns[], String operation){
        StringBuilder builder = new StringBuilder(" SELECT * FROM PRODUCT ");
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
