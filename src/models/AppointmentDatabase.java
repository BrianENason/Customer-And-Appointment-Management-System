package models;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import utilities.TimeControl;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * The point of this class is to create the appointment object itself. It is a middle between the Appointment controller and
 * the Database query that will actually perform the pull to get the information to populate the Appointment object.
 * I have put this in its own class for expandability and updatability.
 */
public class AppointmentDatabase {

    private static ObservableList<Appointment> allAppointmentsInDatabase = FXCollections.observableArrayList();


    /**
     * This will empty the list of appointments, then send the clean list through to the Datbase Query to populate it,
     * ensuring that each time the populate appointments is asked for, the lsit contains all the most up-to-date appointments
     * and their information.
     * @return an ObesrvableList filled with n number of appointment objects where n is the number of appointments
     * that exist in the database at the moment of query.
     * @throws SQLException
     */
    public static ObservableList getAllAppointmentsInDatabase() throws SQLException {

        try{
            allAppointmentsInDatabase.clear();
            allAppointmentsInDatabase.setAll(DatabaseQueries.getAllAppointments());

            for(Appointment a : allAppointmentsInDatabase) {
                a.setStartDateTime(TimeControl.localizeApptTimes(a.getStartDateTime()));
                a.setEndDateTime(TimeControl.localizeApptTimes(a.getEndDateTime()));
            }

            return allAppointmentsInDatabase;

        }catch(SQLException e){
            System.out.println("Database Connection Error: " + e.getMessage());
            return null;
        }
    }

}
