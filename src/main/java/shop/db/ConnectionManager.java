package shop.db;

import java.sql.*;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class ConnectionManager {

    private static Connection con;

    public Connection getConnection() {

        Properties prop = new Properties();
        try {
            prop.load(new BufferedReader(new InputStreamReader(Objects.requireNonNull(ConnectionManager.class.getClassLoader().getResourceAsStream("config/config.properties")))));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            Class.forName(prop.getProperty("db.driver"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            con = DriverManager.getConnection(prop.getProperty("db.url"), prop.getProperty("db.username"), prop.getProperty("db.password"));
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return con;

    }
}
