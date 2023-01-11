package Views_and_Controllers;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseConnection;
import DatabaseControls.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Appointment;
import models.AppointmentDatabase;
import utilities.DeleteConfirm;
import utilities.LanguageControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utilities.popUpController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.ResourceBundle;

/**
 * This class controls the Appointment Main scene
 */
public class AppointmentMainController implements Initializable {

    Stage stage;
    String eTitle;
    String eText;
    String eHeader;
    String eButton;

    @FXML
    private Label appointmentsTitle;

    @FXML
    private Button goToMainButton;

    @FXML
    private Button exitButton;

    //The code below is regarding the view all appointments tab
    @FXML
    private Tab allAppointmentsTab;

    @FXML
    private TableView<Appointment> allViewApptTable;

    @FXML
    private TableColumn<Appointment, Integer> aCustID;

    @FXML
    private TableColumn<Appointment, Integer> aApptID;

    @FXML
    private TableColumn<Appointment, String> aTitle;

    @FXML
    private TableColumn<Appointment, String> aType;

    @FXML
    private TableColumn<Appointment, String> aLocation;

    @FXML
    private TableColumn<Appointment, String> aContact;

    @FXML
    private TableColumn<Appointment, String> aStartTime;

    @FXML
    private TableColumn<Appointment, String> aEndTime;

    @FXML
    private TableColumn<Appointment, String> aDescript;

    @FXML
    private Button aAddApptButton;

    @FXML
    private Button aModApptButton;

    @FXML
    private Button aDeleteApptButton;


    //The code below is regarding the weekview tab
    @FXML
    private Tab weekViewTab;

    @FXML
    private TableView<Appointment> weeklyViewApptTable;

    @FXML
    private TableColumn<Appointment, Integer> wCustID;

    @FXML
    private TableColumn<Appointment, Integer> wApptID;

    @FXML
    private TableColumn<Appointment, String> wTitle;

    @FXML
    private TableColumn<Appointment, String> wType;

    @FXML
    private TableColumn<Appointment, String> wLocation;

    @FXML
    private TableColumn<Appointment, String> wContact;

    @FXML
    private TableColumn<Appointment, String> wStartTime;

    @FXML
    private TableColumn<Appointment, String> wEndTime;

    @FXML
    private TableColumn<Appointment, String> wDescript;

    @FXML
    private Button wAddApptButton;

    @FXML
    private Button wModApptButton;

    @FXML
    private Button wDeleteApptButton;


    //The code below is regarding the monthView tab
    @FXML
    private Tab monthViewTab;

    @FXML
    private TableView<Appointment> monthlyViewApptTable;

    @FXML
    private TableColumn<Appointment, Integer> mCustID;

    @FXML
    private TableColumn<Appointment, Integer> mApptID;

    @FXML
    private TableColumn<Appointment, String> mTitle;

    @FXML
    private TableColumn<Appointment, String> mType;

    @FXML
    private TableColumn<Appointment, String> mLocation;

    @FXML
    private TableColumn<Appointment, String> mContact;

    @FXML
    private TableColumn<Appointment, String> mStartTime;

    @FXML
    private TableColumn<Appointment, String> mEndTime;

    @FXML
    private TableColumn<Appointment, String> mDescript;

    @FXML
    private Button mAddApptButton;

    @FXML
    private Button mModApptButton;

    @FXML
    private Button mDeleteApptButton;


    /**
     * This will send the user to the Add Appointment scene where they can create a new appointment
     * @param event Clicking on the Add Appointment buttons.
     */
    @FXML
    void goToAddAppointment(ActionEvent event) {

        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/AppointmentAdd.fxml"));
            stage.setScene(new Scene(sceneParent));
            stage.show();
        } catch (IOException e) {
            popUpController.popUpWindowSceneError("Add Appointment");
            System.out.println("That screen was not found.");
            e.printStackTrace();
        }

    }

    /**
     * This method handles the Delete Appointments. When a user selects an appointment from the table, it will take that
     * appointment object and remove it from the Database.
     * @param event User clicking on the delete appointment button
     */
    @FXML
    void goToDeleteAppointment(ActionEvent event) {

        Appointment appointment = null;

        if (event.getSource() == aDeleteApptButton) {
            appointment = allViewApptTable.getSelectionModel().getSelectedItem();
        }
        if (event.getSource() == mDeleteApptButton) {
            appointment = monthlyViewApptTable.getSelectionModel().getSelectedItem();
        }
        if (event.getSource() == wDeleteApptButton) {
            appointment = weeklyViewApptTable.getSelectionModel().getSelectedItem();
        }

        if (appointment != null) {

            boolean yes = DeleteConfirm.showBox("Appointment Delete Confirm", "Delete", "Appointment", ("Appt ID: " + appointment.getAppointment_ID() + " of type " + appointment.getType()));

            if(DatabaseQueries.removeAppointmentFromDatabase(appointment) && yes){
                System.out.println("Appointment was deleted");
                popUpController.popUpWindow("Appointment ", "has been successfully deleted", "Appointment Delete", "OK" );
                try {
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/AppointmentMain.fxml"));
                    stage.setScene(new Scene(sceneParent));
                    stage.show();
                } catch (IOException e) {
                    popUpController.popUpWindowSceneError("Appointment Management");
                    System.out.println("Unable to Reload Appointments");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Appointment wasn't deleted");
                return;
            }
        } else {
            System.out.println("No appointment was selected");
            return;
        }
    }

    /**
     * When the user selects an appointment object from the appointments table, this will send that information to the
     * modify appointments scene, pre-populate the data in the scene, and then send the user there.
     * @param event Selecting an appointment and clicking the modify button
     */
    @FXML
    void goToUpdateAppointment(ActionEvent event) {

        Appointment appointment = new Appointment();

        if(event.getSource() == mModApptButton){
           appointment = monthlyViewApptTable.getSelectionModel().getSelectedItem();
        } else if(event.getSource() == wModApptButton) {
            appointment = weeklyViewApptTable.getSelectionModel().getSelectedItem();
        } else if(event.getSource() == aModApptButton) {
            appointment = allViewApptTable.getSelectionModel().getSelectedItem();
        } else {
            appointment = null;
        }

        if(appointment != null) {
            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views_and_Controllers/AppointmentModify.fxml"));
                Parent sceneParent = loader.load();
                AppointmentModifyController controller = loader.getController();

                //FIXME: Put this code into effect when the appointment modify screen is good to go
                controller.popAppointmentInfoFields(appointment);

                stage.setScene(new Scene(sceneParent));
                stage.show();
            } catch (IOException e) {
                popUpController.popUpWindowSceneError("Modify Appointment");
                System.out.println("That screen was not found.");
                e.printStackTrace();
            }
        } else {
            popUpController.popUpWindow(eTitle, eText, eHeader, eButton);
            return;
        }
    }

    /**
     * This will send the user back to the main selection page
     * @param event clicking the Main button
     */
    @FXML
    void goToMainPage(ActionEvent event) {

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
     * This will allow the user to leave the program from the appointments main page. It will close out the database connection
     * first, and then close out the program.
     * @param event
     */
    @FXML
    void quitProgram(ActionEvent event) {
        System.out.println("Exit button clicked from NewAppointmentPage!");
        DatabaseConnection.dBDisconnect();
        System.exit(0);
    }

    /**
     * This will initialize the labels and tables in the main appointment scene
     * @param url
     * @param resourceBundle the language control that will update the labels to the user's language setting
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());
        appointmentsTitle.setText(rb.getString("appointment"));
        goToMainButton.setText(rb.getString("main"));
        exitButton.setText(rb.getString("exit"));
        weekViewTab.setText(rb.getString("weekView"));
        wAddApptButton.setText(rb.getString("addAppt"));
        wModApptButton.setText(rb.getString("modAppt"));
        wDeleteApptButton.setText(rb.getString("delAppt"));
        mAddApptButton.setText(rb.getString("addAppt"));
        mModApptButton.setText(rb.getString("modAppt"));
        mDeleteApptButton.setText(rb.getString("delAppt"));
        monthViewTab.setText(rb.getString("monthView"));
        eTitle = rb.getString("eTitle");
        eText = rb.getString("aText");
        eHeader = rb.getString("aHeader");
        eButton = rb.getString("eButton");

        System.out.println("Language control has worked");

        try {

            //This code below will populate the data for the All Appointments tab
            ObservableList<Appointment> allAppointments;
            allAppointments = AppointmentDatabase.getAllAppointmentsInDatabase();

            allViewApptTable.setItems(allAppointments);
            aCustID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
            aApptID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
            aTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
            aType.setCellValueFactory(new PropertyValueFactory<>("Type"));
            aLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
            aContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
            aStartTime.setCellValueFactory(new PropertyValueFactory<>("StartDateTime"));
            aEndTime.setCellValueFactory(new PropertyValueFactory<>("EndDateTime"));
            aDescript.setCellValueFactory(new PropertyValueFactory<>("Description"));

            //This code below will populate the data for the Monthly view tab
            ObservableList<Appointment> allAppointmentsThisMonth = FXCollections.observableArrayList();
            LocalDateTime whatMonth = LocalDateTime.now();

            for(Appointment a : allAppointments) {
                if(a.getStartDateTime().getYear() == whatMonth.getYear() && a.getStartDateTime().getMonth() == whatMonth.getMonth()) {
                    allAppointmentsThisMonth.add(a);
                }
            }

            monthlyViewApptTable.setItems(allAppointmentsThisMonth);
            mCustID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
            mApptID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
            mTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
            mType.setCellValueFactory(new PropertyValueFactory<>("Type"));
            mLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
            mContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
            mStartTime.setCellValueFactory(new PropertyValueFactory<>("StartDateTime"));
            mEndTime.setCellValueFactory(new PropertyValueFactory<>("EndDateTime"));

            //This code below will populate the data for the weekly view tab
            ObservableList<Appointment> allAppointmentsThisWeek = FXCollections.observableArrayList();

            LocalDate begin = LocalDate.now();

            Integer beginValue = begin.getDayOfWeek().getValue();
            LocalDate weekStart = begin.minusDays(beginValue - 1);
            System.out.println("Week start is .. " + weekStart);

            Integer userDayOfMonth = whatMonth.getDayOfMonth();
            System.out.println("userDayOfMonth = " + userDayOfMonth);

            for(Appointment a : allAppointmentsThisMonth) {

                Integer apptDayOfMonth = a.getStartDateTime().getDayOfMonth();
                System.out.println("apptDayOfMonth = " + apptDayOfMonth);

                if((apptDayOfMonth - userDayOfMonth) < 7 && (apptDayOfMonth - userDayOfMonth) > -7) {

                    //FIXME: This was the attempt to fix the first issue.
                    allAppointmentsThisWeek.add(a);
                    /*if(apptDayOfMonth.compareTo(weekStart.getDayOfMonth()) >= 0) {
                        allAppointmentsThisWeek.add(a);
                    }*/
                }
            }

            weeklyViewApptTable.setItems(allAppointmentsThisWeek);
            wCustID.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
            wApptID.setCellValueFactory(new PropertyValueFactory<>("Appointment_ID"));
            wTitle.setCellValueFactory(new PropertyValueFactory<>("Title"));
            wType.setCellValueFactory(new PropertyValueFactory<>("Type"));
            wLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
            wContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
            wStartTime.setCellValueFactory(new PropertyValueFactory<>("StartDateTime"));
            wEndTime.setCellValueFactory(new PropertyValueFactory<>("EndDateTime"));
            wDescript.setCellValueFactory(new PropertyValueFactory<>("Description"));

            System.out.println("Weekly Appointments view successfully populated");


            System.out.println("All Appointments view successfully populated");

        } catch (SQLException sqlException) {
            System.out.println("Could not initialize because...");
            sqlException.printStackTrace();
        }
    }
}
