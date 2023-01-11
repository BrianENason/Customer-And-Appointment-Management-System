package Views_and_Controllers;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseConnection;
import utilities.LanguageControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utilities.TimeControl;
import utilities.popUpController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

/**
 * This class controls the Main Menu Selection Screen
 */
public class MainSelectionSceneController implements Initializable {

    Stage stage;

    @FXML
    private Label selectionlabel;

    @FXML
    private Label locationLabel;

    @FXML
    private Button customerButton;

    @FXML
    private Button appointmentButton;

    @FXML
    private Button recordsButton;

    @FXML
    private Button exitButton;

    /**
     * Sends the user to the Appointments Main screen
     * @param event user Selects Appointment Management
     * @throws IOException
     */
    @FXML
    void goToAppointmentManagement(ActionEvent event) throws IOException {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/AppointmentMain.fxml"));
            stage.setScene(new Scene(sceneParent));
            stage.show();
        } catch (IOException e) {
            popUpController.popUpWindowSceneError("Appointment Management");
            System.out.println("That screen appt was not found.");
            e.printStackTrace();
        }
    }

    /**
     * sends the user to the Customer Management screen
     * @param event when user Clicks the Customer Management button
     */
    @FXML
    void goToCustomerManagement(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/CustomerInfoMain.fxml"));
            stage.setScene(new Scene(sceneParent));
            stage.show();
        } catch (IOException e) {
            popUpController.popUpWindowSceneError("Customer Management");
            System.out.println("That screen was not found.");
            e.printStackTrace();
        }

    }

    /**
     * Sends the user to the reports scene
     * @param event when user selects Records/reports button
     */
    @FXML
    void goToRecordsManagement(ActionEvent event) {
        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/RecordsMain.fxml"));
            stage.setScene(new Scene(sceneParent));
            stage.show();
        } catch (IOException e) {
            popUpController.popUpWindowSceneError("Records");
            System.out.println("That screen was not found.");
            e.printStackTrace();
        }

    }

    /**
     * Closes the connection to the database and then closes the program
     * @param event user selecting exit button
     */
    @FXML
    void quitProgram(ActionEvent event) {
        System.out.println("Exit button clicked from MainSelectionScene!");
        DatabaseConnection.dBDisconnect();
        System.exit(0);
    }


    /**
     * Sets the labels to the language preference of the user.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());
        locationLabel.setText(rb.getString("language") + "\n" + TimeControl.getZone(ZonedDateTime.now()));
        selectionlabel.setText(rb.getString("selection"));
        customerButton.setText(rb.getString("customer"));
        appointmentButton.setText(rb.getString("appointment"));
        recordsButton.setText(rb.getString("records"));
        exitButton.setText(rb.getString("exit"));
    }
}
