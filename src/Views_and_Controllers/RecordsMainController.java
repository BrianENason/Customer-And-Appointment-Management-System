package Views_and_Controllers;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseConnection;
import DatabaseControls.DatabaseQueries;
import utilities.LanguageControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import utilities.popUpController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This controls the reports screen
 */
public class RecordsMainController implements Initializable {

    Stage stage;

    @FXML
    private RadioButton totApptRadio;

    @FXML
    private ToggleGroup report;

    @FXML
    private RadioButton apptScheduleRadio;

    @FXML
    private RadioButton myChoiceRadio;

    @FXML
    private RadioButton clearReportRadio;
    @FXML
    private Label reportMainTitle;
    @FXML
    private Label titleOfReport;
    @FXML
    private TextArea reportArea;
    @FXML
    private Button backButton;
    @FXML
    private Button exitButton;

    /**
     * This will display a report that shows the total number of customer appointments by type and month when the user
     * selects the appropriate radio button.
     * @param event user selecting the report 1 button
     */
    @FXML
    void printReport1(ActionEvent event) {
        reportArea.setPrefColumnCount(3);
        reportArea.setText(DatabaseQueries.firstReport());
    }

    /**
     * This report will show a schedule for each contact in the organization. It will include appointment ID, title,
     * type and description, start date and time, end date and time, and customer ID
     * @param event user selecting the report 2 button
     */
    @FXML
    void printReport2(ActionEvent event) {
        reportArea.setText(DatabaseQueries.secondReport());
    }

    /**
     * This is my choice of report. It generates the number of customers in each country and then sorts them by their first-level location
     * @param event user selecting the report 3 button.
     */
    @FXML
    void printReport3(ActionEvent event) {
        reportArea.setText(DatabaseQueries.thirdReport());
    }

    /**
     * Sets up a default display in the report window that says to select a report to display.
     * @param event user selecting radio 1 button
     */
    @FXML
    void printReportDefault(ActionEvent event) {
        reportArea.setText(defaultReport());
    }

    /**
     * This will close out the database connection and then exit the user from the program.
     * @param event
     */
    @FXML
    void exitProgram(ActionEvent event) {
        System.out.println("Exit button clicked from RecordsMain page!");
        DatabaseConnection.dBDisconnect();
        System.exit(0);
    }

    /**
     * This will send the user back to the main screen when they select the main button.
     */
    @FXML
    void goToMain(ActionEvent event) {

        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/MainSelectionScene.fxml"));
            stage.setScene(new Scene(sceneParent));
            stage.show();
        } catch (IOException e) {
            popUpController.popUpWindowSceneError("Main Screen");
            System.out.println("That screen was not found.");
            e.printStackTrace();
        }

    }


    /**
     * This is to set the report area to default whenever needed including on first page visit     *
     * @return
     */
    public String defaultReport() {

        String defaultReportTitle = "No report selected";
        return defaultReportTitle;

    }

    /**
     * this will initialize the labels in the scene with the user's language preference.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());
        reportMainTitle.setText(rb.getString("reports"));
        titleOfReport.setText(rb.getString("defaultReport"));
        reportArea.setText(defaultReport());
        backButton.setText(rb.getString("main"));
        exitButton.setText(rb.getString("exit"));

    }
}
