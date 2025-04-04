package org.assignment.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@Slf4j
public final class ConnectionUtility {
    private ConnectionUtility() {
    }

    private static Connection con;

    public static Connection getConnection() {

        if (con == null) {
            try (FileInputStream fileInputStream = new FileInputStream("src/main/resources/config.properties")){
                Properties properties = new Properties();

                properties.load(fileInputStream);
                String url = properties.getProperty("url");
                con = DriverManager.getConnection(url,properties);
            } catch (SQLException | IOException ex) {
                System.out.println("Properties file not found");
            }
        }
        return con;
    }
    private static EntityManager manager;
    public static EntityManager getEntityManager()
    {
        if(manager == null)
        {
            final EntityManagerFactory factory = Persistence.createEntityManagerFactory("myPersistenceUnit");
            manager = factory.createEntityManager();
        }
        return manager;
    }
}
