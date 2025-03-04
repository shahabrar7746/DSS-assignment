package main.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public final class ConnectionUtility {
    private ConnectionUtility() {
    }

    private static Connection con;

    public static Connection getConnection() {

        if (con == null) {
            try {
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream("src/config.properties");
                properties.load(fileInputStream);
                String url = properties.getProperty("database.url");
                String user = properties.getProperty("database.username");
                String password = properties.getProperty("database.password");
                con = DriverManager.getConnection(url, user, password);
            } catch (SQLException | IOException ex) {
                System.out.println("Properties file not found");
            }
        }
        return con;
    }

}
