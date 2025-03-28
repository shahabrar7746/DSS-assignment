package com.foodorder.app.utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtility {
//
//    public static void chaloConnetion() {
//        Properties properties = new Properties();
//        FileInputStream fileInputStream = null;
//        try {
//            fileInputStream = new FileInputStream("src/main/resources/config.properties");
//            System.out.println(fileInputStream.available());
//
//            properties.load(fileInputStream);
//            String url = properties.getProperty("url");
//            System.out.println( "url came"+ url);
//            Connection connection = DriverManager.getConnection(url, properties);
//            System.out.println("connection establish");
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        System.out.println();
//    }

    private static final Logger log = LogManager.getLogger(ConnectionUtility.class);
    private static Connection connection;

    private ConnectionUtility() {
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties properties = new Properties();
                FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties");
                properties.load(fileInputStream);
                String url = properties.getProperty("url");
                connection = DriverManager.getConnection(url, properties);

            } catch (SQLException | IOException e) {
                log.error("properties file not found");
            }
        }
        return connection;
    }
}
