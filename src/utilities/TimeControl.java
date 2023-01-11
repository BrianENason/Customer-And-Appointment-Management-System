package utilities;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class TimeControl {

    public static void apptTimeCheck() throws SQLException {

        ObservableList<Appointment> allAppointments = DatabaseQueries.getAllAppointments();
        LocalDateTime isNow = LocalDateTime.now();

        for(Appointment a : allAppointments){

            LocalDateTime l = localizeApptTimes(a.getStartDateTime());

            if (l.compareTo(isNow.plusMinutes(15)) <= 0 && isNow.compareTo(l) <= 0) {
                popUpController.popUpWindow("You have the following appointment comming up in less than 15 minutes:",
                        "\nID: " + a.getAppointment_ID() + " on " + l, "15 Minute Appointment Alert!", "OK");
                return;
            }
        }

        popUpController.popUpWindow("You have no upcomming appointments ", "to worry about.", "15 Minute Appointment Alert", "Ok");
        return;

    }

    public static LocalDateTime localizeApptTimes (LocalDateTime t) {

        ZoneId u = ZoneId.systemDefault();

        ZonedDateTime z = t.atZone(ZoneId.of("GMT"));
        z = z.withZoneSameInstant(u);
        LocalDateTime l = z.toLocalDateTime();
        return l;

    }

    public static LocalDateTime generalizeApptTimes (LocalDateTime t) {

        ZoneId u = ZoneId.systemDefault();

        ZonedDateTime z = t.atZone(u);
        z = z.withZoneSameInstant(ZoneId.of("GMT"));
        LocalDateTime l = z.toLocalDateTime();
        return l;

    }

    /**
     * This Method converts the system time into the time for all offices. Mostly used for debugging purposes.
     */
    public static void timeZoneConversions() {

        Date date = new Date();
        DateFormat defaultFormat = new SimpleDateFormat("hh:mm:ss ' on ' MMMM dd, yyy");

        /**
         This will convert the system time to the New York and Montreal office time since they share the same time zone
         */
        TimeZone officeTime = TimeZone.getTimeZone("America/New_York");
        defaultFormat.setTimeZone(officeTime);
        System.out.println("In New York and Montreal, the time is: " + defaultFormat.format(date));

        /**
         This will convert the system time to the Phoenix Arizona office time
         */
        TimeZone arizonaTime = TimeZone.getTimeZone("America/Phoenix");
        defaultFormat.setTimeZone(arizonaTime);
        System.out.println("In Phoenix, the time is: " + defaultFormat.format(date));

        /**
         This will convert the system time to the London England office time
         */
        TimeZone londonTime = TimeZone.getTimeZone("Europe/London");
        defaultFormat.setTimeZone(londonTime);
        System.out.println("In London, the time is: " + defaultFormat.format(date));

        /**
         This will convert the system time to GMT/UTC
         */
        TimeZone utcTime = TimeZone.getTimeZone("GMT");
        defaultFormat.setTimeZone(utcTime);
        System.out.println("In UTC, the time is: " + defaultFormat.format(date));

        /**
         This will convert the system time to PST which is my time zone so I can check to make sure it's all working
         */
        TimeZone pstTime = TimeZone.getTimeZone("PST");
        defaultFormat.setTimeZone(pstTime);
        System.out.println("In PST, the time is: " + defaultFormat.format(date));

    }

    public static ObservableList<ZonedDateTime> meetingStartCalculation() {

        ZoneId userZID = ZoneId.systemDefault();
        ZonedDateTime gmtZDT = ZonedDateTime.of (LocalDate.now(ZoneId.of("GMT")) , LocalTime.of ( 12 , 00 ) , ZoneId.of ( "GMT" ) );
        ZonedDateTime userZDT = gmtZDT.withZoneSameInstant(userZID);

        ObservableList<ZonedDateTime> gmtZDTObservableList = FXCollections.observableArrayList(gmtZDT);
        ObservableList<ZonedDateTime> userZDTObservableList = FXCollections.observableArrayList(userZDT);

        DateTimeFormatter time = DateTimeFormatter.ofPattern("H:mm");

        for(int i = 0; i < 28; i++) {
            userZDT = userZDT.plusMinutes(30);
            userZDTObservableList.add(userZDT);

        }

        for (ZonedDateTime z : userZDTObservableList) {
            System.out.print(z.format(time) + " ");
        }

        return userZDTObservableList;
    }



    public static ZonedDateTime userTimeToGMT(ZonedDateTime userTime) {

        ZonedDateTime userTimeToGMT = userTime.withZoneSameInstant(ZoneId.of("GMT"));
        return userTimeToGMT;

    }

    public static ZonedDateTime gmtToNewYork(ZonedDateTime gmtTime) {

        ZonedDateTime gmtToNewYork = gmtTime.withZoneSameInstant(ZoneId.of("America/New_York"));
        return gmtToNewYork;

    }

    public static String formatTime(ZonedDateTime timeToFormat) {

        var formTime = DateTimeFormatter.ofPattern("hh:mm:ss a");
        String formatTime = timeToFormat.format(formTime);

        return formatTime;

    }

    public static String formatDate(ZonedDateTime timeToFormat) {

        var formTime = DateTimeFormatter.ofPattern("MMMM dd, yyy");
        String formatDate = timeToFormat.format(formTime);

        return formatDate;

    }

    public static String getZone(ZonedDateTime userTime) {

        ZoneId userZone = userTime.getZone();
        String zone = userZone.toString();

        return zone;

    }


}
