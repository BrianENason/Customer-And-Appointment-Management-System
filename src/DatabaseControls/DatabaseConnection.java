package DatabaseControls;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import java.sql.*;


/**
 * This class will control the connection to the database.
 */
public class DatabaseConnection {
    private static final String dBName = "WJ065Kw";
    private static final String dBServerName = "wgudb.ucertify.com";
    private static final String dBPort = "3306";
    private static final String dBUrl = "jdbc:mysql://" + dBServerName + "/" + dBName;
    private static final String dBUserName = "U065Kw";
    private static final String dBPassword = "53688686519";
    private static final String dBDriver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn;

    /**
     * This method connects to the database at the initialization of the program
     */
    public static void dBConnect() {
        try {
            Class.forName(dBDriver);
            conn = DriverManager.getConnection(dBUrl, dBUserName, dBPassword);
            System.out.println("You are now connected to the Database.\n");
        } catch (ClassNotFoundException e) {
            System.out.println("Couldn't find Class: \n");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("SQL Exception: \n");
            e.printStackTrace();
        }
    }

    /**
     * This method closes the connection to the database at the completion of the program
     */
    public static void dBDisconnect() {
        try {
            conn.close();
            System.out.println("You are now disconnected from the Database.");
        } catch (SQLException e) {
            System.out.println("SQL Exception: \n");
            e.printStackTrace();
        }
    }

    /**
     * This returns a connection to the database. Used for database interaction
     * @return connection to the databse to do queries
     */
    public static Connection getConnection() {
        return conn;
    }





}
