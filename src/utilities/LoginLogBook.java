package utilities;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZonedDateTime;

/**
 * This class creates and maintains a user log in a text file
 */
public class LoginLogBook {

    private static final String FILENAME = "login_activity.txt"; //Sets the filename for the logbook and places it in the program's folders if it doesn't already exist in it.
    private static final String USERNAME = "UserLogBook.txt"; //Sets the filename for the logbook and places it in the program's folders if it doesn't already exist in it.

    public static void userBook(String username) {

        try (FileWriter loginLogBookFile = new FileWriter(USERNAME, false);
             BufferedWriter bookWriter = new BufferedWriter(loginLogBookFile);
             PrintWriter bookPrinter = new PrintWriter(bookWriter)) {
            bookPrinter.println(username);
        } catch (IOException e) {
            System.out.println("User Book Error: " + e.getMessage());
        }
    }

    /**
     * This will take the user's entered login info and store it as well as the user's success/fail into a notepad file
     * @param username the username the user enters in the login screen
     * @param password the password the user enters in the login screen
     * @param success Boolean. Whether or not the login was a success.
     */
    public static void logBookEntry(String username, String password, boolean success) {

        try (FileWriter loginLogBookFile = new FileWriter(FILENAME, true);
             BufferedWriter bookWriter = new BufferedWriter(loginLogBookFile);
             PrintWriter bookPrinter = new PrintWriter(bookWriter)) {
            bookPrinter.println("******************\n");
            bookPrinter.println("At: " + ZonedDateTime.now() + " \nUsername: " + username + "\nand Password: " + password +
                    " \nwere entered into the login screen with " + (success ? " Great Success!\n" : " Disappointing Failure!\n"));
        } catch (IOException e) {
            System.out.println("Log Book Error: " + e.getMessage());
        }
    }
}