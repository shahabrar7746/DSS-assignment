package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtility {
private ConnectionUtility(){}
    private static Connection con;
    public static Connection getConnection() throws SQLException {
       String url = "jdbc:postgresql://172.16.1.195:5331/dbhdemo";
       String user = "dbhuser";
       String password = "Fy2aWdXt";
        if(con == null) {
            con = DriverManager.getConnection(url, user, password);
        }
        return con;

    }
}
