package main.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public final class ConnectionUtility {
private ConnectionUtility(){}
    private static Connection con;
    public static Connection getConnection() { // TODO

        if(con == null) {
            try {
                String[] credentials = getDataBaseCredentials();
                String url = credentials[0];
                String user = credentials[1];
                String password = credentials[2];
                con = DriverManager.getConnection(url, user, password);
} catch (FileNotFoundException | SQLException ex ) {
                System.out.println("Properties file not found");
            }
        }
        return con;
    }
    private static String[] getDataBaseCredentials() throws FileNotFoundException {
        File file = new File("src/properties");
        Scanner scanner = new Scanner(file);
        String[] arr= new String[3];
        int index = 0;
        while(scanner.hasNextLine()){
            arr[index++] = scanner.nextLine().split("=")[1];
        }
        return arr;
    }
}
