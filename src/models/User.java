package models;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseQueries;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class simply takes the username and gets the id from it
 */
public class User implements Initializable {

    String userName;
    Integer userID;

    public void populateUserInfo(String userName) {
        userName = userName;
        userID = DatabaseQueries.getUserID(userName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
