package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection databaseConnection = null;
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/bcs";

    public DatabaseConnection() {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "Kosong");
        try {
            connection = DriverManager.getConnection(url, "root", "Kosong");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        return databaseConnection == null ? databaseConnection = new DatabaseConnection() : databaseConnection;
    }

    public Connection getConnection() {
        return connection;
    }
}
