package models;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**
 * The point of this class is to create the customer object itself. It is a middle between the Customer controller and
 * the Database query that will actually perform the pull to get the information to populate the Customer object.
 * I have put this in its own class for expandability and updatability.
 */
public class CustomerDatabase {

    private static ObservableList<Customer> allCustomersInDatabase = FXCollections.observableArrayList();

    /**
     * This will empty the list of customers, then send the clean list through to the Datbase Query to populate it,
     * ensuring that each time the populate customers is asked for, the lsit contains all the most up-to-date information.
     * @return an ObesrvableList filled with n number of customer objects where n is the number of customers that exist
     * in the database at the moment of query.
     * @throws SQLException
     */
    public static ObservableList getAllCustomersInDatabase() throws SQLException {

        try{
            allCustomersInDatabase.clear();
            allCustomersInDatabase.setAll(DatabaseQueries.getAllCustomers());
/*
            System.out.println(allCustomersInDatabase);
            System.out.println("\nThe Following customers are in the database:");

            for(Customer c : allCustomersInDatabase){
                System.out.println(c.getCustomer_ID() + ": " + c.getCustomer_Name() + " at: " + c.getAddress() + " in: " +
                        c.getPostal_Code() + " In: " + c.getDivision_ID() + " Contact Phone: " + c.getPhone());
            }
*/

            return allCustomersInDatabase;

        }catch(SQLException e){
            System.out.println("Database Connection Error: " + e.getMessage());
            return null;
        }
    }


    public static Customer getCustomer(Integer id) throws SQLException {
        Customer selectCustomer = null;
        try{
            allCustomersInDatabase.clear();
            allCustomersInDatabase.setAll(DatabaseQueries.getAllCustomers());

            for(Customer c : allCustomersInDatabase){
                if (c.getCustomer_ID() == id){
                    selectCustomer=c;
                    return selectCustomer;
                }
            }
        } catch (SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
        }
        return null;
    }

    /**
     * Used to generate a new Customer ID based on the the largest existing Customer ID plus one
     * @return returns a new Customer ID
     * @throws SQLException
     */
    public static Integer getNewCustomerID() throws SQLException {
        try{
            Integer customerID = (DatabaseQueries.getNewCustomerID() + 1);
            return customerID;
        } catch (SQLException e) {
            System.out.println("Database Connection Error: " + e.getMessage());
        }
        return null;
    }

}
