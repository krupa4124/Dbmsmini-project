package crime;

import java.sql.*;

/**
 * Singleton-style DB connection helper.
 * Change PASSWORD to your MySQL root password before running.
 */
public class DBConnection {

    private static final String URL      = "jdbc:mysql://localhost:3306/crime_management?useSSL=false&serverTimezone=UTC";
    private static final String USER     = "root";
    private static final String PASSWORD = "12345";   // ← CHANGE THIS

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
