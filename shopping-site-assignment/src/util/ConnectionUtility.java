package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConnectionUtility {
private ConnectionUtility(){}
    private static Connection con;
    public static Connection getConnection(String url, String user, String password) throws SQLException {
        if(con == null) {
            con = DriverManager.getConnection(url, user, password);
        }
        return con;

    }
}
