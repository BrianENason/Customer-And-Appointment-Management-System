package Views_and_Controllers;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseQueries;
import utilities.LanguageControl;
import utilities.LoginLogBook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.TimeControl;
import utilities.popUpController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

/**
 * This class controls the login screen
 */
public class LoginScreenControl implements Initializable {

    Stage stage;

    private String eHeader;
    private String eTitle;
    private String eText;
    private String eButton;

    @FXML
    private Label loginMessage;

    @FXML
    private Label uNameLabel;

    @FXML
    private Label uPassLabel;

    @FXML
    private TextField UserNameTextField;

    @FXML
    private PasswordField PasswordTextField;

    @FXML
    private Label zoneId;

    @FXML
    private Button loginButton;

    /**
     * When user selects login, then this will get the username and password entered by the user. It will run the username
     * into the database table user, extract the associated user password, and then compare the password from the database
     * to the user-entered one. If they match, then the user is given access to the program. If not, a popup tells them that
     * the username and passwords don't match. They will not be given access until the user and pass match.
     * @param event clicking the Login button
     * @throws IOException
     * @throws SQLException
     */
    @FXML
    void onClickGoToMain(ActionEvent event) throws IOException, SQLException {

        String login = UserNameTextField.getText();
        String password = PasswordTextField.getText();

        String requestedPass = DatabaseQueries.loginControl(login);

        if (requestedPass == null) {
            System.out.println("No password was found for user");
            popUpController.popUpWindow(eTitle, eText, eHeader, eButton);
            LoginLogBook.logBookEntry(login, password, false);
            return;
        }

        if (password.equals(requestedPass)) {
            System.out.println("Passwords match");
            LoginLogBook.logBookEntry(login, password, true);
            LoginLogBook.userBook(login);

            try {
                TimeControl.apptTimeCheck();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }

            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/MainSelectionScene.fxml"));

                stage.setScene(new Scene(sceneParent));
                stage.show();
            } catch (IOException e) {
                popUpController.popUpWindow(eTitle, eText, eHeader, eButton);
                System.out.println("That screen was not found.");
                e.printStackTrace();
            }
        } else {
            LoginLogBook.logBookEntry(login, password, false);
            popUpController.popUpWindow(eTitle, eText, eHeader, eButton);
        }

    }


    /**
     * This will set up the program to respond to the user's preferred language. It changes the labels and warnings
     * to the language of choice (English or French only)
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());

        loginMessage.setText(rb.getString("message"));
        uNameLabel.setText(rb.getString("username"));
        uPassLabel.setText(rb.getString("password"));
        UserNameTextField.setPromptText(rb.getString("username"));
        PasswordTextField.setPromptText(rb.getString("password"));
        zoneId.setText(rb.getString("language") + "\n" + TimeControl.getZone(ZonedDateTime.now()));
        loginButton.setText(rb.getString("login"));
        eHeader = rb.getString("eHeader");
        eTitle = rb.getString("eTitle");
        eText = rb.getString("eText");
        eButton = rb.getString("eButton");

    }
}
