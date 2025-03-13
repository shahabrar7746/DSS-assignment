package org.assignment.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class ConnectionUtility {
    private ConnectionUtility() {
    }

    private static Connection con;

    public static Connection getConnection() {

        if (con == null) {
            try {
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
                properties.load(fileInputStream);
                String url = properties.getProperty("url");
                con = DriverManager.getConnection(url,properties);
            } catch (SQLException | IOException ex) {
                System.out.println("Properties file not found");
            }
        }
        return con;
    }

}
