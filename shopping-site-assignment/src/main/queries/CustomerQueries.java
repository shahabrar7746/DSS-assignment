package main.queries;

import java.util.Arrays;

public class CustomerQueries {

   public static  String getAllCustomerQuery(){
       return "SELECT * FROM CUSTOMER ";
   }
   public static String getCustomerOrAdminByColumn(String columns[], String operation){
       StringBuilder builder = new StringBuilder(" SELECT * FROM CUSTOMER ");
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
   public static  String addCustomerQuery(String columns[]){
       StringBuilder builder = new StringBuilder("INSERT INTO CUSTOMER ");
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

   public static String updateCustomerQuery(String columns[], String customer_id){
       StringBuilder builder = new StringBuilder(" UPDATE CUSTOMER SET ");
       builder.append(columns[0]);
       builder.append(" = ? ");
       for(int i = 1;i<columns.length;i++){
           builder.append(",");
           builder.append(columns[i]);
           builder.append(" = ? ");
       }
       builder.append(" WHERE ");
       builder.append(customer_id);
       builder.append(" = ? ");
       return builder.toString();
   }
   public static String deleteCustomerQuery(String column){
       StringBuilder builder = new StringBuilder(" DELETE FROM CUSTOMER ");
       builder.append(" WHERE ");
       builder.append(column);
       builder.append(" = ? ");
       return builder.toString();

   }

}
