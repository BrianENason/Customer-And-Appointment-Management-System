package DatabaseControls;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import utilities.DeleteConfirm;
import utilities.TimeControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;
import models.Customer;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;

public class DatabaseQueries {

    /**
     * This will send through the inputted username, match it to an existing user's password, then return
     * the password in the database for that user to check it against the user-entered password
     * @param userName this is input from the login screen
     * @return returns the password that matches the input userName
     * @throws SQLException
     */
    public static String loginControl(String userName) throws SQLException {

        String password;

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String userQuery = "SELECT Password FROM users WHERE User_Name=" + "'" + userName + "'";
            ResultSet userPasswordResult = statement.executeQuery(userQuery);

            if (userPasswordResult.next()) {
                password = userPasswordResult.getString("Password");
                System.out.println("The password test returns... " + password);
                return password;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return null;
        }
    }


    /**
     * This method is used to take in the named city in the Customer object and convert it to a Division_Id to relate to the
     * Country-level information in the Database.
     * @param city This is the Customer object's city (of residence - for example, New York, etc.)
     * @return this returns the Database's integer value for the named city.
     */
    public static String getDivision_ID (String city) {

        String Division_ID; // To hold the results of the database query so it can be returned to the main program

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT Division_ID FROM first_level_divisions WHERE Division='" + city + "'";
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                Division_ID = result.getString("Division_ID");
                return Division_ID;
            }
        }catch(SQLException e){
            System.out.println("SQL Exception! - " + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * This is a small method that is used in part to populate a combo box in the CustomerInfoModifyController.
     * What it does is takes in the Division_ID from the Customer object that is passed into the CustomerInfoModifyController
     * and Converts it to the associated COUNTRY_ID number. This number will thn be used to pre-select the Customer object's
     * Country in their address.
     * @param Division_ID This input is used in the SQL Query to pull the equivalent Country ID variable to the Division ID
     * @return integer value of the Country_ID to be used in other SQL queries for pulling the name of the country.
     */
    public static Integer getCountryFromDivision (String Division_ID){

        String cString; //the variable to hold the COUNTRY_ID String Result
        Integer cInt; //the int variable to hold the converted COUNTRY_ID String-to-int value


        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division='" + Division_ID + "'";
            ResultSet result = statement.executeQuery(query);
            while(result.next()){
                cString = result.getString("COUNTRY_ID");
                cInt = Integer.parseInt(cString);
                return cInt;
            }

        }catch(SQLException e){
            System.out.println("SQL Exception! - " + e.getMessage());
            return null;
        }
        return null;
    }

    /**
     * This takes in the countryID input from the Customer create/edit screens's combo boxes and uses that country code
     * to output a string list of first-level locations to be used to populate the first-level/location combo box.
     * @param countryID this is taken in from the ComboBox on either the CustomerInfoAdd or CustomerInfoModify screens
     * @return sends back an array filled with first-level locations to populate the combo box on the method-calling scene
     */
    public static ObservableList getLocations(Integer countryID) {

        ObservableList<String> firstLevel = FXCollections.observableArrayList("- Please select your location -");

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String locationQuery = "SELECT Division FROM first_level_divisions WHERE COUNTRY_ID=" + countryID;
            ResultSet report1Results = statement.executeQuery(locationQuery);
            while (report1Results.next()) {
                assert firstLevel != null;
                firstLevel.add(report1Results.getString("Division"));
            }
            statement.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
        }
        return firstLevel;
    }

    /**
     * This method tanslates the "Division_ID" from the customer table to translate it into actual location names
     * @param divisionID comes from the customer table
     * @return the name of the place with the divisionID that matches the customer table
     * @throws SQLException
     */
    public static String getFirstLevelName(String divisionID) throws SQLException {

        String firstLevelName;

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String userQuery = "SELECT Division FROM first_level_divisions WHERE Division_ID=" + "'" + divisionID + "'";
            ResultSet rs = statement.executeQuery(userQuery);

            if (rs.next()) {
                firstLevelName = rs.getString("Division");
                return firstLevelName;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return null;
        }
    }


    /**
     * This will take in a newly-created appointment object and add it to the appointments database table
     * @param appointment a newly created appointment
     * @return true if the addition was a success, false if it was a fail
     */
    public static boolean addAppointmentToDatabase(Appointment appointment) {

        Integer Appointment_ID = appointment.getAppointment_ID();
        String Title = appointment.getTitle();
        String Description = appointment.getDescription();
        String Location = appointment.getLocation();
        String Contact_Name = appointment.getContact();
        String Type = appointment.getType();
        LocalDateTime Start = appointment.getStartDateTime();
        LocalDateTime End = appointment.getEndDateTime();
        Integer Customer_ID = appointment.getCustomer_ID();
        Integer User_ID = appointment.getUser_ID();

        Integer Contact_ID = DatabaseQueries.getContactID(Contact_Name);

        try {
            PreparedStatement pStatement = DatabaseConnection.getConnection().prepareStatement(
                    "INSERT INTO appointments (Appointment_ID, Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID) " +
                            "VALUES (?,?,?,?,?,?,?,?,?,?)");
            pStatement.setInt(1, Appointment_ID);
            pStatement.setString(2, Title);
            pStatement.setString(3, Description);
            pStatement.setString(4, Location);
            pStatement.setInt(5, Contact_ID);
            pStatement.setString(6, Type);
            pStatement.setTimestamp(7, Timestamp.valueOf(Start));
            pStatement.setTimestamp(8, Timestamp.valueOf(End));
            pStatement.setInt(9, Customer_ID);
            pStatement.setInt(10, User_ID);

            pStatement.executeUpdate();
            System.out.println("The SQL Executed appropreately");
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return false;
        }
    }

    /**
     * This will take a selected appointment and delete it from the database
     * @param appointment the appointment object the user selects from the main appointment screen's table
     * @return true if it is a success, false if it fails
     */
    public static boolean removeAppointmentFromDatabase(Appointment appointment) {

        Integer appointmentID = appointment.getAppointment_ID();

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "DELETE FROM appointments WHERE Appointment_ID =" + appointmentID;
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return false;
        }
    }

    /**
     * This method takes the appointment ID in, queries the database for a matching appointment, and then deletes
     * that matching appointment from the database.
     * @param appointmentID This is the unique ID of the user
     * @return
     */
    public static boolean removeApptFromDataByID (Integer appointmentID) {

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "DELETE FROM appointments WHERE Appointment_ID=" + appointmentID;
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return false;
        }
    }

    /**
     * This method pulls the info from the appointments table and translates it into an appointment object to be used in the code
     * @return Appointment Obect
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description, " +
                    "appointments.Location, appointments.Contact_ID, appointments.Type, appointments.Start, appointments.End, " +
                    "appointments.Customer_ID, appointments.User_ID FROM appointments";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                Appointment appointment = new Appointment(
                        rs.getInt("Appointment_ID"),
                        rs.getString("Title"),
                        rs.getString("Description"),
                        rs.getString("Location"),
                        getContactName(rs.getString("Contact_ID")),
                        rs.getString("Type"),
                        rs.getTimestamp("Start").toLocalDateTime(),
                        rs.getTimestamp("End").toLocalDateTime(),
                        rs.getInt("Customer_ID"),
                        rs.getInt("User_ID"));
                allAppointments.add(appointment);
            }
            statement.close();
            return allAppointments;
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }


    /**
     * Simple method to remove the Customer from the database by parsign the Customer Object for the customer's ID number,
     * which is also used in the Database to identify the customer. The query then deletes any customer record with the
     * same ID number as the Customer object passed into this method.
     * @param customer This is the Customer object that the user of the program wishes to delete from the database.
     * @return Returns a boolean value of true if the process was a success, false if it was a failure or if an error was thrown.
     */
    public static boolean removeCustomerFromDatabase(Customer customer) {

        String customerID = customer.getCustomer_ID().toString(); //convert the customer object's id into a string to use it in the SQL query

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "DELETE FROM customers WHERE Customer_ID =" + customerID;
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return false;
        }
    }

    /**
     * When a customer object is modified in the main program, this method updates the customer record in the database.
     * @param customer This is the representation of the customer inside the main program. The data contained in the object
     *                 is parsed through and added as an update to an existing customer row in the database.
     * @return true if the process was a success, false if it failed or if an error happens.
     */
    public static boolean addModCustomerToDatabase(Customer customer) {

        Integer ID = customer.getCustomer_ID();
        String Customer_ID = customer.getCustomer_ID().toString();
        String Customer_Name = customer.getCustomer_Name();
        String Address = customer.getAddress();
        String Division_ID = customer.getDivision_ID();
        String Phone = customer.getPhone();
        String Postal_Code = customer.getPostal_Code();

        try {
            PreparedStatement pStatement = DatabaseConnection.getConnection().prepareStatement(
                    "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Phone = ?, Division_ID = ?, Postal_Code = ? WHERE Customer_ID = ?");
            pStatement.setString(1, Customer_ID);
            pStatement.setString(2, Customer_Name);
            pStatement.setString(3, Address);
            pStatement.setString(4, Phone);
            pStatement.setString(5, Division_ID);
            pStatement.setString(6, Postal_Code);
            pStatement.setString(7, Customer_ID);

            pStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return false;
        }

    }

    public static ObservableList<Appointment> checkForCustomerAppointments(Integer customerID){

        ObservableList<Appointment> custSpecificAppts = FXCollections.observableArrayList();

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT Appointment_ID, Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID FROM appointments WHERE Customer_ID='" + customerID + "'";
            ResultSet allAppts = statement.executeQuery(query);
            while(allAppts.next()) {
                Appointment appointment = new Appointment(
                        allAppts.getInt("Appointment_ID"),
                        allAppts.getString("Title"),
                        allAppts.getString("Description"),
                        allAppts.getString("Location"),
                        getContactName(allAppts.getString("Contact_ID")),
                        allAppts.getString("Type"),
                        allAppts.getTimestamp("Start").toLocalDateTime(),
                        allAppts.getTimestamp("End").toLocalDateTime(),
                        allAppts.getInt("Customer_ID"),
                        allAppts.getInt("User_ID"));
                custSpecificAppts.add(appointment);
            }
            statement.close();
            return custSpecificAppts;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }

/*        ObservableList<Timestamp> custApptTimes = FXCollections.observableArrayList();

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT Customer_ID, Start, End FROM appointments WHERE Customer_ID='" + customerID + "'";
            ResultSet apptTimes = statement.executeQuery(query);
            while(apptTimes.next()) {
                custApptTimes.add(
                        apptTimes.getTimestamp("Start"));
                        //apptTimes.getTimestamp("Start"), apptTimes.getTimestamp("End"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }*/



    }

    /**
     * This will take in a customer id, run it through the appointments table in the database, and return a matching
     * appointment if it finds one linked to the customer.
     * @param customerID the Customer ID that is being queried to match to any/all appointments
     * @return True if there is an appointment matched, false if not.
     */
    public static ObservableList<Appointment> ifCustHasApptDoYouDelete(Integer customerID) throws NullPointerException {

        ObservableList<Appointment> allRelatedAppts = FXCollections.observableArrayList();


        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT Appointment_ID, Title, Description, Location, Contact_ID, Type, Start, End, Customer_ID, User_ID FROM appointments WHERE Customer_ID=" + customerID;
            ResultSet hasAppt = statement.executeQuery(query);
            while (hasAppt.next()) {
                Appointment a = new Appointment(
                        hasAppt.getInt("Appointment_ID"),
                        hasAppt.getString("Title"),
                        hasAppt.getString("Description"),
                        hasAppt.getString("Location"),
                        getContactName(hasAppt.getString("Contact_ID")),
                        hasAppt.getString("Type"),
                        hasAppt.getTimestamp("Start").toLocalDateTime(),
                        hasAppt.getTimestamp("End").toLocalDateTime(),
                        hasAppt.getInt("Customer_ID"),
                        hasAppt.getInt("User_ID"));
                allRelatedAppts.add(a);
            }
            statement.close();
            return allRelatedAppts;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    /**
     * Used to add a newly created Customer Object from the main program into the database as a new customer row.
     * @param customer The new customer object we wish to parse and add to the database.
     * @return True if the customer was successfuly added to the database, false if not or if error happens.
     */
    public static boolean addCustomerToDatabase(Customer customer) {

        String Customer_ID = customer.getCustomer_ID().toString();
        String Customer_Name = customer.getCustomer_Name();
        String Address = customer.getAddress();
        String Division_ID = customer.getDivision_ID();
        String Phone = customer.getPhone();
        String Postal_Code = customer.getPostal_Code();

        try {
            PreparedStatement pStatement = DatabaseConnection.getConnection().prepareStatement(
                    "INSERT INTO customers (Customer_ID, Customer_Name, Address, Phone, Division_ID, Postal_Code) " +
                            "VALUES (?,?,?,?,?,?)");
            pStatement.setString(1, Customer_ID);
            pStatement.setString(2, Customer_Name);
            pStatement.setString(3, Address);
            pStatement.setString(4, Phone);
            pStatement.setString(5, Division_ID);
            pStatement.setString(6, Postal_Code);

            pStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return false;
        }
    }

    /**
     * This will go through the database and retrieve the customerID's to use in the Appointments page
     * @return an ObservableList of integer values of customer_ID's
     */
    public static ObservableList getCustomerIDs() {

        ObservableList<Integer> getCustomerID = FXCollections.observableArrayList();

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String contactQuery = "SELECT Customer_ID FROM customers ORDER BY Customer_ID ASC";
            ResultSet contactResults = statement.executeQuery(contactQuery);
            while(contactResults.next()) {
                assert getCustomerID != null;
                getCustomerID.add(contactResults.getInt("Customer_ID"));
            }
            statement.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
        }

        return getCustomerID;
    }

    /**
     * This method pulls the info form the customers table in the database and translates it into an array of customer objects to be used throughout the code
     * @return anotherGetAllCustomers ObservableArrayList of customer objects
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        ObservableList<Customer> anotherGetAllCustomers = FXCollections.observableArrayList();

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address, " +
                    "customers.Postal_Code, customers.Division_ID, customers.Phone FROM customers";
            ResultSet rs = statement.executeQuery(query);

            while(rs.next()){
                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        getFirstLevelName(rs.getString("Division_ID")),
                        rs.getString("Phone"));

                anotherGetAllCustomers.add(customer);
            }
            statement.close();
            return  anotherGetAllCustomers;
        }catch(SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }


    /**
     * This report shows the total number of customer appointments by type and month
     * @return report to view in the textfield on the Reports scene
     */
    public static String firstReport(){
        String firstReportText = "Report Error or No Report Found";

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT Type, MONTHNAME(Start) as 'Month', COUNT(*) as 'Total' FROM appointments GROUP BY Type, MONTHNAME(Start)";
            //String query = "SELECT Description, MONTHNAME(Start) as 'Month', COUNT(*) as 'Total' FROM appointments GROUP BY Description, MONTHNAME(Start)";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportOneText = new StringBuilder();
            reportOneText.append(String.format("%1$-50s %2$-50s %3$s \n", "Month", "Type of Appointment", "Total Appointments"));
            reportOneText.append(String.join("", Collections.nCopies(130, "*")));
            reportOneText.append("\n");
            while(results.next()) {
                reportOneText.append(String.format("%1$-50s %2$-50s %3$d \n",
                        results.getString("Month"), results.getString("Type"), results.getInt("Total")));
                        //results.getString("Month"), results.getString("description"), results.getInt("Total")));
            }
            statement.close();
            return reportOneText.toString();
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
        }

        return firstReportText;
    }

    /**
     * This report generates a schedule for each contact in the organization
     * @return report to view in the textfield on the Reports scene
     */
    public static String secondReport(){
        String secondReportText = "Report Error or No Report Found";

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT contacts.Contact_Name, appointments.Appointment_ID, appointments.Title, appointments.Type, appointments.Description, appointments.Start, appointments.End, appointments.Customer_ID " +
                    "FROM contacts JOIN appointments ON appointments.Contact_ID=contacts.Contact_ID " +
                    "ORDER BY appointments.Contact_ID, appointments.Start";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportTwoText = new StringBuilder();

            reportTwoText.append(String.format("%1$-15s %2$-15s %3$-15s %4$-25s %5$25s %6$-22s %7$-22s %8$-10s \n", "Contact Name", "Appointment ID",
                    "Appointment Title", "Appointment Type", "Appointment Description", "Start Date/Time", "End Date/Time", "Customer ID"));
            reportTwoText.append(String.join("", Collections.nCopies(200, "*")));
            reportTwoText.append("\n");
            while(results.next()) {
                reportTwoText.append(String.format("%1$-25s %2$-10d %3$-40s %4$-40s %5$50s %6$-22s %7$-22s %8$-10d \n",
                        results.getString("Contact_Name"), results.getInt("Appointment_ID"), results.getString("Title"), results.getString("Type"),
                        results.getString("Description"), results.getString("Start"), results.getString("End"), results.getInt("Customer_ID")));
                reportTwoText.append("\n");
            }
            statement.close();
            return reportTwoText.toString();
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
        }

        return secondReportText;
    }

    /**
     * This report generates the number of customers in each country and then sorts them by their first-level location
     * @return report to view in the textfield on the Reports scene
     */
    public static String thirdReport() {
        String thirdReportText = "Report Error or No Report Found";

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String query = "SELECT customers.Division_ID, first_level_divisions.Division_ID, " +
                    "first_level_divisions.Division, first_level_divisions.COUNTRY_ID, countries.Country_ID, countries.Country, COUNT(*) as 'Total' " +
                    "FROM customers JOIN first_level_divisions ON first_level_divisions.Division_ID=customers.Division_ID " +
                    "JOIN countries ON countries.Country_ID=first_level_divisions.COUNTRY_ID " +
                    "GROUP BY first_level_divisions.Division_ID " +
                    "ORDER BY countries.Country";
            ResultSet results = statement.executeQuery(query);
            StringBuilder reportThreeText = new StringBuilder();
            reportThreeText.append(String.format("%1$-25s %2$-25s %3$-3s \n", "Country Name", "First Level", "Customer QTY"));
            reportThreeText.append(String.join("", Collections.nCopies(200, "*")));
            reportThreeText.append("\n");

            while(results.next()){
                reportThreeText.append(String.format("%1$-25s %2$-25s %3$-3s \n",
                        results.getString("Country"), results.getString("Division"), results.getInt("Total")));
                reportThreeText.append("\n");
            }
            statement.close();
            return reportThreeText.toString();

        }catch(SQLException e){
            System.out.println("SQL Exception! - " + e.getMessage());
        }
        return thirdReportText;
    }


    /**
     * This creates a List of String objects filled with Contact names from the database table contacts to populate a
     * combo box on the AppointmentAdd and AppointmentModify screens.
     * @return ObservableList of String objects containing all the contact names from the contact table in the DB
     */
    public static ObservableList getContacts() {

        ObservableList<String> getContactsName = FXCollections.observableArrayList("- Select Contact Name -");

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String contactQuery = "SELECT Contact_Name FROM contacts";
            ResultSet contactResults = statement.executeQuery(contactQuery);
            while(contactResults.next()) {
                assert getContactsName != null;
                getContactsName.add(contactResults.getString("Contact_Name"));
            }
            statement.close();

        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
        }
        return getContactsName;
    }

    /**
     * Gets the ID value of the contact the user selects from the contact combo box in the appointments screens
     * @param contactName The name of the contact for the appointment
     * @return a single integer value representing the contact id number of the contact name the user selects from the combo box.
     */
    public static Integer getContactID(String contactName) {

        Integer contactID;

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String userQuery = "SELECT Contact_ID FROM contacts WHERE Contact_Name=" + "'" + contactName + "'";
            ResultSet rs = statement.executeQuery(userQuery);

            if (rs.next()) {
                contactID = rs.getInt("Contact_ID");
                return contactID;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return null;
        }
    }

    /**
     * This method translates the "Contact ID" from the database in the name of the contact to be used in the appointment form
     * @param contactID this is input from the appointments database
     * @return the name of the contact related to the input contactID
     * @throws SQLException
     */
    public static String getContactName(String contactID) throws SQLException {

        String contactName;

        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String userQuery = "SELECT Contact_Name FROM contacts WHERE Contact_ID='" + contactID + "'";
            ResultSet rs = statement.executeQuery(userQuery);

            if (rs.next()) {
                contactName = rs.getString("Contact_Name");
                return contactName;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return null;
        }
    }




    /**
     * This will create a unique appointment id for a user-created appointment by parsing a list of all appointment id's,
     * pulling out the largest value, then adding 1 to it to set a new, unique appointment
     * @return an integer value of a new appointment ID
     * @throws SQLException
     */
    public static Integer getNewApptID() throws SQLException {
        Integer apptID = 0;

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String appointmentQuery = "SELECT Appointment_ID FROM appointments";
            ResultSet appointmentIdResult = statement.executeQuery(appointmentQuery);

            while(appointmentIdResult.next()){
                if (appointmentIdResult.getInt("Appointment_ID") > apptID) {
                    apptID = appointmentIdResult.getInt("Appointment_ID");
                }
            }
            statement.close();
            return apptID + 1;

        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    /**
     * This will take in all the ID's from the customers table in the database, find out the largest one, and then add
     * one to it to create a new/unique ID for a newly created customer.
     * @return the new Customer ID
     * @throws SQLException
     */
    public static Integer getNewCustomerID() throws SQLException {
        Integer custID = 0;

        try{
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String customerQuery = "SELECT Customer_ID FROM customers";
            ResultSet customerIdResult = statement.executeQuery(customerQuery);

            while(customerIdResult.next()){
                if (customerIdResult.getInt("Customer_ID") > custID) {
                    custID = customerIdResult.getInt("Customer_ID");
                }
            }
            statement.close();
            return custID + 1;

        }catch(SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    /**
     * This will take the name of the user logged into the system and return their id number.
     * @param userName the name of the current user of the program
     * @return user id in integer form
     */
    public static Integer getUserID (String userName) {
        try {
            Statement statement = DatabaseConnection.getConnection().createStatement();
            String userQuery = "SELECT User_ID FROM users WHERE User_Name=" + "'" + userName + "'";
            ResultSet userIDResult = statement.executeQuery(userQuery);

            if (userIDResult.next()) {
                Integer userID = Integer.valueOf(userIDResult.getString("User_ID"));
                System.out.println("The userID test returns... " + userID.toString());
                return userID;
            } else {
                return null;
            }
        } catch (SQLException e) {
            System.out.println("SQL Exception! - " + e.getMessage());
            return null;
        }
    }


    /**
     * This Method is the most basic of the get time methods. It is mostly here for debug
     */
    public static void getTime() {

        ZonedDateTime currentZone = ZonedDateTime.now();
        ZoneId zone = currentZone.getZone();

        System.out.println("The current time is: " + TimeControl.formatTime(currentZone) + ",");
        System.out.println("on: " + currentZone.getDayOfWeek() + " " + TimeControl.formatDate(currentZone) + ",");
        System.out.println("and you are in: " + zone + ".\n");

        ZonedDateTime toUTC = TimeControl.userTimeToGMT(currentZone);
        ZonedDateTime toNY = TimeControl.gmtToNewYork(toUTC);

        System.out.println("In New York, the time is: " + TimeControl.formatTime(toNY));
        System.out.println("on: " + TimeControl.formatDate(toNY) + ".\n");
        System.out.println("In UTC, the time is: " + TimeControl.formatTime(toUTC) + "\non: " + TimeControl.formatDate(toUTC) + ".");
    }

}