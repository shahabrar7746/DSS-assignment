package util;

import entities.Customer;
import enums.Roles;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

public final  class CustomerUtility {
   private static Connection con;

private CustomerUtility(){}
    public static Connection getConnection(String url, String user, String password) throws SQLException {
        if(con == null) {
            con = DriverManager.getConnection(url, user, password);
        }
         return con;

    }


}
