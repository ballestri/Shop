package shop.db;

import java.sql.*;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

public class ConnectionManager {

    private static Connection con;
    private String username;
    private String password;
    private String driver;
    private String url;
    private Properties prop;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    void loadProperties() {
        prop = new Properties();
        try {
            prop.load(new BufferedReader(new InputStreamReader(Objects.requireNonNull(ConnectionManager.class.getClassLoader().getResourceAsStream("config/config.properties")))));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        setDriver(prop.getProperty("db.driver"));
        setUsername(prop.getProperty("db.username"));
        setPassword(prop.getProperty("db.password"));
        setUrl(prop.getProperty("db.url"));
    }

    public Connection getConnection() {

        loadProperties();
        try {
            Class.forName(getDriver());
            con = DriverManager.getConnection(getUrl(), getUsername(), getPassword());
        } catch (SQLException | ClassNotFoundException sqlException) {
            sqlException.printStackTrace();
        }
        return con;
    }
}
