package Views_and_Controllers;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseConnection;
import DatabaseControls.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import models.Appointment;
import utilities.LanguageControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.TimeControl;
import utilities.popUpController;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * This Class controls the Appointment Add scene
 */
public class AppointmentAddController implements Initializable {

    Stage stage;

    @FXML
    private Label addApptTitle;

    @FXML
    private Label apptId;

    @FXML
    private Label title;

    @FXML
    private Label descript;

    @FXML
    private Label location;

    @FXML
    private Label contact;

    @FXML
    private Label type;

    @FXML
    private Label startDateTime;

    @FXML
    private Label endDateTime;

    @FXML
    private TextField apptIDField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField locationField;

    @FXML
    private TextField userID;

    @FXML
    private ComboBox<String> contactNameComboBox;

    @FXML
    private TextField typeField;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private ComboBox<LocalTime> startTimeCombo;

/*    @FXML
    private DatePicker endDatePicker;*/

    @FXML
    private ComboBox<LocalTime> endTimeCombo;

    @FXML
    private ComboBox<Integer> customerIdComboBox;

    @FXML
    private Button exitButton;

    @FXML
    private Button cancelApptButton;

    @FXML
    private Button addApptButton;

    /**
     * This adds the Appointment to the database
     * @param event on Add Appointment button click
     */
    @FXML
    void addAppointment(ActionEvent event) {

        LocalTime apptStart = startTimeCombo.getValue();
        LocalTime apptEnd = endTimeCombo.getValue();

        /**
         * Checks to make sure a start time has been selected. If not, a popup window informs the user then returns
         * back to the page to make the changes.
         */
        if(apptStart == (null)) {
            popUpController.popUpWindow("No start time selected", "\nPlease select a start time", "Start Time Error", "OK");
            return;
        }

        /**
         * Checks to make sure an end time has been selected. If not, a popup window informs the user then returns
         * back to the page to make the changes.
         */
        if(apptEnd == (null)) {
            popUpController.popUpWindow("No end time selected", "\nPlease select an end time", "End Time Error", "OK");
            return;
        }

        /**
         * Checks to make sure the start and end times are different. Put a popup if they are the same and then returns the user
         * back to the form to make the changes
         */
        if(apptStart.equals(apptEnd)) {
            popUpController.popUpWindow("Start time/End time error!", "\nYour start and end times cannot be the same!", "Start/End Error", "OK");
            return;
        }

        /**
         * Checks to make sure the end time is after the start time. Put a popup if they are the same and then returns the user
         * back to the form to make the changes
         */
        if(apptStart.isAfter(apptEnd)) {
            popUpController.popUpWindow("Start time/End time error!", "\nYour end time MUST be AFTER your Start time!", "Start/End Error", "OK");
            return;
        }

        LocalDate startDate = startDatePicker.getValue();

        /**
         * Checks to make sure the start date is selected. Put a popup if they are the same and then returns the user
         * back to the form to make the changes
         */
        if (startDate == null) {
            popUpController.popUpWindow("You haven't chosen a START date", "\nPlease do so now", "Appt Date Error", "OK");
            return;
        }

        Integer apptId = Integer.parseInt(apptIDField.getText());
        String title = titleField.getText();
        String descript = descriptionField.getText();
        String location = locationField.getText();
        String contactName = contactNameComboBox.getValue();
        String type = typeField.getText();
        LocalDateTime startDateTime = LocalDateTime.of(startDate, apptStart);
        LocalDateTime endDateTime = LocalDateTime.of(startDate, apptEnd);
        //LocalDateTime endDateTime = LocalDateTime.of(endDate, apptEnd);
        Integer customerID = customerIdComboBox.getValue();
        Integer userSID = Integer.parseInt(userID.getText());

        if(title.equals("") | descript.equals("") | location.equals("") | type.equals("") | customerID == null | contactName == null) {
            popUpController.popUpWindow("Missing data or no data entered!",
                    "\nPlease check that all fields have data!", "Error", "OK");
            return;
        }

        startDateTime = TimeControl.generalizeApptTimes(startDateTime);
        endDateTime = TimeControl.generalizeApptTimes(endDateTime);
/*
        if(startDateTime.getDayOfMonth() != endDateTime.getDayOfMonth()) {
            popUpController.popUpWindow("Appt start and end date must match.", "\nPlease select a new start or end date", "Date Error", "OK");
            return;
        }*/

        ObservableList<Appointment> custSpecificAppts = DatabaseQueries.checkForCustomerAppointments(customerID);
        //custSpecificAppts = ;

        assert custSpecificAppts != null;
        for (Appointment a : custSpecificAppts) {

            /**
             * We first check to make sure the selected start/end times are not already assigned to an appt. If they are,
             * an error message occurs that an appt already exists and then it kicks the user back to the screen
              */
            if(a.getStartDateTime().compareTo(startDateTime) == 0 || a.getEndDateTime().compareTo(endDateTime) == 0){
                popUpController.popUpWindow("Customer has an Appointment at that time", "\nPlease select a new time", "Appt Time Error", "OK");
                return;
            }

            /**
             * Then we check to see if the new start time is after an existing start time but before an existing end time. If so,
             * then we know we can't schedule the start to be then because it would fall in between an existing appt. It will
             * show a popup saying that it overlaps an existing appt time, then kick the user back to the main screen.
             */
            if(startDateTime.compareTo(a.getStartDateTime()) > 0 && startDateTime.compareTo(a.getEndDateTime()) < 0) {
                popUpController.popUpWindow("Customer has an Appointment at that time", "\nPlease select a new time", "Appt Time Error", "OK");
                return;
            }

            /**
             * Finally we check to see if the new end time is after an existing start time but before an existing end time. If so,
             * then we know we can't schedule the start to be then because it would fall in between an existing appt. It will
             * show a popup saying that it overlaps an existing appt time, then kick the user back to the main screen.
             */
            if(endDateTime.compareTo(a.getEndDateTime()) < 0 && endDateTime.compareTo(a.getStartDateTime()) > 0 ) {
                popUpController.popUpWindow("Customer has an Appointment at that time", "\nPlease select a new time", "Appt Time Error", "OK");
                return;
            }

        }

        Appointment appointment = new Appointment(apptId, title, descript, location, contactName, type, startDateTime, endDateTime, customerID, userSID);

        System.out.println("Now to add to the database...");

        if(DatabaseQueries.addAppointmentToDatabase(appointment)) {
            System.out.println("The appointment was added!!");
            popUpController.popUpAddCustomerSorF("Success! ", "The appointment ", " has been added!" , "OK");
            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/AppointmentMain.fxml"));
                stage.setScene(new Scene(sceneParent));
                stage.show();
            } catch (IOException e) {
                popUpController.popUpWindowSceneError("Customer Info Main Screen");
                System.out.println("That screen was not found.");
                e.printStackTrace();
            }

        } else {
            System.out.println("The appointment was NOT added!!");
            popUpController.popUpAddCustomerSorF("Fail! ", "The appointment ", " has NOT been added!" , "OK");
            return;
        }
    }

    /**
     * This will send the user back to the main appointment screen without adding a user
     * @param event click cancel button
     */
    @FXML
    void goToAppointmentsMain(ActionEvent event) {

        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/AppointmentMain.fxml"));
            stage.setScene(new Scene(sceneParent));
            stage.show();
        } catch (IOException e) {
            popUpController.popUpWindowSceneError("Main Screen");
            System.out.println("That screen was not found.");
            e.printStackTrace();
        }
    }

    /**
     * Quits the program entirely including disconnecting from the database
     * @param event exit/quit button clicked
     */
    @FXML
    void quitProgram(ActionEvent event) {
        System.out.println("Exit button clicked from AddAppointmentPage!");
        DatabaseConnection.dBDisconnect();
        System.exit(0);
    }

    /**
     * This method is used to populate the appointment start times combo box, translating the NY Office Hours to the user's
     * TimeZone.
     */
    public void populateApptStartTime() {

        DateTimeFormatter time = DateTimeFormatter.ofPattern("H:mm");
        ObservableList<LocalTime> justTimes = FXCollections.observableArrayList();

        ObservableList<ZonedDateTime> appointmentTimes = FXCollections.observableArrayList(TimeControl.meetingStartCalculation());

        for(ZonedDateTime z : appointmentTimes){
            z.format(time);
            justTimes.add(LocalTime.from(z));
        }

        startTimeCombo.setItems(justTimes);
        endTimeCombo.setItems(justTimes);
    }

    /**
     * This method helps set the userID field by reading the user login name from the text file, sending the name to the
     * database, and returning the user's id number
     * @return the integer value of the user's ID based on their login name
     * @throws IOException
     */
    public static Integer setUserID() throws IOException {
        Integer userID = 9999;
        FileInputStream username = null;

        try {
            username = new FileInputStream("UserLogBook.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(username));
        String user = reader.readLine();
        reader.close();

        userID = DatabaseQueries.getUserID(user);

        return userID;
    }

    /**
     * This will initialize some of the basic information in the scene including combo boxes and id fields
     * @param url
     * @param resourceBundle Makes the labels language-dependent
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        populateApptStartTime();

        contactNameComboBox.setItems(DatabaseQueries.getContacts());

        try {
            userID.setText(String.valueOf(setUserID()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            apptIDField.setText(DatabaseQueries.getNewApptID().toString());
        } catch (SQLException e) {
            System.out.println("Error generating new appointment ID!");
            System.out.println("Appointment ID will temporarily be set to 9999");
            apptIDField.setText("9999");
        }

        customerIdComboBox.setItems(DatabaseQueries.getCustomerIDs());

        /*        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());
        addApptTitle.setText(rb.getString("addAppt"));
        apptIDField.setPromptText(rb.getString("idFieldText"));
        apptId.setText(rb.getString("apptId"));
        title.setText(rb.getString("title"));
        descript.setText(rb.getString("descript"));
        location.setText(rb.getString("location"));
        contact.setText(rb.getString("contact"));
        type.setText(rb.getString("type"));
        startDateTime.setText(rb.getString("startDate"));
        endDateTime.setText(rb.getString("endDate"));
        exitButton.setText(rb.getString("exit"));
        cancelApptButton.setText(rb.getString("cancel"));
        addApptButton.setText(rb.getString("addAppt"));*/


    }
}
