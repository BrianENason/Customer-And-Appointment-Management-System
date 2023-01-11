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
import utilities.DeleteConfirm;
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

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AppointmentModifyController implements Initializable {

    Stage stage;
    private Integer appointmentID;
    Appointment hold = new Appointment();


    @FXML
    private Label modApptTitle;

    @FXML
    private TextField apptIDField;

    @FXML
    private TextField apptTitle;

    @FXML
    private TextField apptDescript;

    @FXML
    private TextField apptLocation;

    @FXML
    private TextField userIdTextField;

    @FXML
    private ComboBox<String> contactNameComboBox;

    @FXML
    private TextField apptType;

    @FXML
    private DatePicker startDateSelect;

    @FXML
    private ComboBox<LocalTime> startTimeSelect;

    @FXML
    private ComboBox<LocalTime> endTimeSelect;

    @FXML
    private ComboBox<Integer> customerIdComboBox;

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
    private Button exitButton;

    @FXML
    private Button cancelApptButton;

    @FXML
    private Button modApptButton;

    @FXML
    void goToApptMain(ActionEvent event) {

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
     * When the user selects the modify button, several things happen in this method:
     * 1) The currently being modified appointment is removed from the database
     * 2) The process will then check to make sure all fields are populated, correct, and don't clash
     * 3) when it's good to go, it creates a NEW appointment object and inserts it into the Datbase.
     * @param event clicking the modify button.
     */
    @FXML
    void modifyAppt(ActionEvent event) {

        if(DeleteConfirm.showBox("Appointmet Modify", "Modify", "Appointment ID", String.valueOf((hold.getAppointment_ID())))) {

            /**
             * We first have to remove the Appointment from the database so that the checks work. This is done here.
             */
            DatabaseQueries.removeAppointmentFromDatabase(hold);

            LocalTime apptStart = startTimeSelect.getValue();
            LocalTime apptEnd = endTimeSelect.getValue();

            /**
             * Checks to make sure a start time has been selected. If not, a popup window informs the user then returns
             * back to the page to make the changes.
             */
            if (apptStart == (null)) {
                popUpController.popUpWindow("No start time selected", "\nPlease select a start time", "Start Time Error", "OK");
                return;
            }

            /**
             * Checks to make sure an end time has been selected. If not, a popup window informs the user then returns
             * back to the page to make the changes.
             */
            if (apptEnd == (null)) {
                popUpController.popUpWindow("No end time selected", "\nPlease select an end time", "End Time Error", "OK");
                return;
            }

            /**
             * Checks to make sure the start and end times are different. Put a popup if they are the same and then returns the user
             * back to the form to make the changes
             */
            if (apptStart.equals(apptEnd)) {
                popUpController.popUpWindow("Start time/End time error!", "\nYour start and end times cannot be the same!", "Start/End Error", "OK");
                return;
            }

            /**
             * Checks to make sure the end time is after the start time. Put a popup if they are the same and then returns the user
             * back to the form to make the changes
             */
            if (apptStart.isAfter(apptEnd)) {
                popUpController.popUpWindow("Start time/End time error!", "\nYour end time MUST be AFTER your Start time!", "Start/End Error", "OK");
                return;
            }

            LocalDate startDate = startDateSelect.getValue();

            /**
             * Checks to make sure the start date is selected. Put a popup if they are the same and then returns the user
             * back to the form to make the changes
             */
            if (startDate == null) {
                popUpController.popUpWindow("You haven't chosen a START date", "\nPlease do so now", "Appt Date Error", "OK");
                return;
            }


            /**
             * Now that the data fields have been checked, we start to pull and store the data for them
             */
            Integer apptId = Integer.parseInt(apptIDField.getText());
            String title = apptTitle.getText();
            String descript = apptDescript.getText();
            String location = apptLocation.getText();
            String contactName = contactNameComboBox.getValue();
            String type = apptType.getText();
            LocalDateTime startDateTime = LocalDateTime.of(startDate, apptStart);
            LocalDateTime endDateTime = LocalDateTime.of(startDate, apptEnd);
            Integer customerID = customerIdComboBox.getValue();
            Integer userSID = Integer.parseInt(userIdTextField.getText());

            /**
             * We check to make sure all fields have values. If not, then a popup informs the user and then sends them back
             * to the form to make the changes.
             */
            if (title.equals("") | descript.equals("") | location.equals("") | type.equals("") | customerID == null | contactName == null) {
                popUpController.popUpWindow("Missing data or no data entered!",
                        "\nPlease check that all fields have data!", "Error", "OK");
                return;
            }

            /**
             * We now generalize the appointment start/end times BACK to GMT for Database Storage
             */
            startDateTime = TimeControl.generalizeApptTimes(startDateTime);
            endDateTime = TimeControl.generalizeApptTimes(endDateTime);

            /**
             * We then check to make sure the new appointment data doens't clash with any other customer appointments by
             * pulling all the appointment data from the Database and relate it to the customer.
             */
            ObservableList<Appointment> custSpecificAppts = DatabaseQueries.checkForCustomerAppointments(customerID);

            assert custSpecificAppts != null;
            for (Appointment a : custSpecificAppts) {

                /**
                 * We first check to make sure the selected start/end times are not already assigned to an appt. If they are,
                 * an error message occurs that an appt already exists and then it kicks the user back to the screen
                 */
                if (a.getStartDateTime().compareTo(startDateTime) == 0 || a.getEndDateTime().compareTo(endDateTime) == 0) {
                    popUpController.popUpWindow("Customer has an Appointment at that time", "\nPlease select a new time", "Appt Time Error", "OK");
                    return;
                }

                /**
                 * Then we check to see if the new start time is after an existing start time but before an existing end time. If so,
                 * then we know we can't schedule the start to be then because it would fall in between an existing appt. It will
                 * show a popup saying that it overlaps an existing appt time, then kick the user back to the main screen.
                 */
                if (startDateTime.compareTo(a.getStartDateTime()) > 0 && startDateTime.compareTo(a.getEndDateTime()) < 0) {
                    popUpController.popUpWindow("Customer has an Appointment at that time", "\nPlease select a new time", "Appt Time Error", "OK");
                    return;
                }

                /**
                 * Finally we check to see if the new end time is after an existing start time but before an existing end time. If so,
                 * then we know we can't schedule the start to be then because it would fall in between an existing appt. It will
                 * show a popup saying that it overlaps an existing appt time, then kick the user back to the main screen.
                 */
                if (endDateTime.compareTo(a.getEndDateTime()) < 0 && endDateTime.compareTo(a.getStartDateTime()) > 0) {
                    popUpController.popUpWindow("Customer has an Appointment at that time", "\nPlease select a new time", "Appt Time Error", "OK");
                    return;
                }

            }

            /**
             * If all is good, then we create the new appointment, add it to the database, and then "Bob's your uncle", we're done.
             */
            Appointment appointment = new Appointment(apptId, title, descript, location, contactName, type, startDateTime, endDateTime, customerID, userSID);

            System.out.println("Now to add to the database...");

            if (DatabaseQueries.addAppointmentToDatabase(appointment)) {
                System.out.println("The appointment was added!!");
                popUpController.popUpAddCustomerSorF("Success! ", "The appointment ", " has been modified!", "OK");
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
                popUpController.popUpAddCustomerSorF("Fail! ", "The appointment ", " has NOT been added!", "OK");
                return;
            }
        }
    }

    @FXML
    void quitProgram(ActionEvent event) {
        System.out.println("Exit button clicked from ModifyAppointmentPage!");
        DatabaseConnection.dBDisconnect();
        System.exit(0);
    }

    //FIXME: Write code that Sets the outgoing times to UTC
    public ZonedDateTime timeToUTC(ZonedDateTime userApptTime) {
        ZonedDateTime utcTime = null;

        return utcTime;
    }

    //FIXME: This will compile a list of available times from the New York office to the local time of the user.
    //Business hours are EST 08:00-22:00
    public ObservableList<ZonedDateTime> availableTimesLocal() {

        ObservableList<ZonedDateTime> localApptTimes = null;

        ZonedDateTime userTime = ZonedDateTime.now();
        ZonedDateTime utcTime = TimeControl.userTimeToGMT(userTime);

        return localApptTimes;
    }

    public void populateApptStartTime() {

        DateTimeFormatter time = DateTimeFormatter.ofPattern("H:mm");
        ObservableList<LocalTime> justTimes = FXCollections.observableArrayList();

        ObservableList<ZonedDateTime> appointmentTimes = FXCollections.observableArrayList(TimeControl.meetingStartCalculation());

        for(ZonedDateTime z : appointmentTimes){
            z.format(time);
            justTimes.add(LocalTime.from(z));
        }

        startTimeSelect.setItems(justTimes);
        endTimeSelect.setItems(justTimes);
    }



    /**
     * This will populate the form fields with the selected appointment object's data.
     * @param a The appointment that the user wishes to modify from the Appointment Main screen
     */
    public void popAppointmentInfoFields(Appointment a){

        /**
         * First we fill the basic information that is easy to get
         */
        hold = a;
        appointmentID = a.getAppointment_ID();
        apptIDField.setText(appointmentID.toString());
        apptTitle.setText(a.getTitle());
        apptDescript.setText(a.getDescription());
        apptLocation.setText(a.getLocation());
        apptType.setText(a.getType());

        /**
         * Now we populate the start and end time combo box with all the user-time-zone true appt times
         */
        populateApptStartTime();

        /**
         * with the start end time combo boxes populated, we set the selected items in them based on the appointment sent in
         */
        startTimeSelect.getSelectionModel().select(LocalTime.from(a.getStartDateTime()));
        endTimeSelect.getSelectionModel().select(LocalTime.from(a.getEndDateTime()));
        startDateSelect.setValue(LocalDate.from(a.getStartDateTime()));

        /**
         * This is to select the contact person from the appointment ID
         */
        contactNameComboBox.getSelectionModel().select(a.getContact());

        /**
         * This is to populate the customerID combo box with all the customers in the database at that point, and then
         * select that once customer from the appointment object that is associated with the appointment object.
         */
        customerIdComboBox.setItems(DatabaseQueries.getCustomerIDs());
        customerIdComboBox.getSelectionModel().select(a.getCustomer_ID());

        /**
         * sets the userID field with the user id associated with the appointment, not necessarily the one who is
         * logged into the program. It is a disabled field, so the value cannot be changed regardless of the user.
         */
        userIdTextField.setText(String.valueOf(a.getUser_ID()));

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());
        modApptTitle.setText(rb.getString("modAppt"));
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
        modApptButton.setText(rb.getString("modAppt"));

        contactNameComboBox.setItems(DatabaseQueries.getContacts());
    }
}
