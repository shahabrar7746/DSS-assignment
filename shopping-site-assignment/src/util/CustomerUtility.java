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
public static Map<Long, Customer> getCustomersFromResultSet(ResultSet set) throws SQLException {
   Map<Long, Customer> map = new ConcurrentHashMap<>();

    while(set.next()){
        Long id = set.getLong("customer_id");
        String email = set.getString("email");
        String password = set.getString("password");
        String name = set.getString("name");
        String address = set.getString("address");
        Roles roles  = Roles.valueOf( set.getString("role"));
        Timestamp timestamp = set.getTimestamp("registered_on");
        map.put(id, new Customer(id,name,email, password, address, timestamp, roles));
    }

    return map;
}

}
