package Views_and_Controllers;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseConnection;
import DatabaseControls.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Appointment;
import models.Customer;
import models.CustomerDatabase;
import utilities.DeleteConfirm;
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
import utilities.popUpController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class is the controller for the Customer Info Main screen. Here the user can view/add/update/and delete customers
 * from the Database.
 */
public class CustomerInfoMainController implements Initializable {

    Stage stage;
    String eTitle;
    String eText;
    String eHeader;
    String eButton;

    @FXML
    private Label customerLabel;

    @FXML
    private Button addCustButton;

    @FXML
    private Button updateCustButton;

    @FXML
    private Button delCustButton;

    @FXML
    private Button goBackButton;

    @FXML
    private Button exitButton;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> id;

    @FXML
    private TableColumn<Customer, String> name;

    @FXML
    private TableColumn<Customer, String> address;

    @FXML
    private TableColumn<Customer, String> postalCode;

    @FXML
    private TableColumn<Customer, String> phone;

    @FXML
    private TableColumn<Customer, String> state;

    /**
     * This sends the user to the add customer screen
     * @param event User clicks Add Customer button
     */
    @FXML
    void goToAddCustomer(ActionEvent event) {

        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/CustomerInfoAdd.fxml"));
            stage.setScene(new Scene(sceneParent));
            stage.show();
        } catch (IOException e) {
            popUpController.popUpWindowSceneError("Add New Customer");
            System.out.println("That screen was not found.");
            e.printStackTrace();
        }

    }

    /**
     * This takes the user-selected customer's information and sends it and the user to the Customer Info Modify screen.
     * If there is no user selected, the program doesn't let the user proceed to the screen.
     * @param event User clicks the Update Customer button
     */
    @FXML
    void goToUpdateCustomer(ActionEvent event) {

        Customer customer = customerTable.getSelectionModel().getSelectedItem();

        if (customer != null) {
            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views_and_Controllers/CustomerInfoModify.fxml"));
                Parent sceneParent = loader.load();
                CustomerInfoModifyController controller = loader.getController();
                controller.popCustomerInfoFields(customer);
                stage.setScene(new Scene(sceneParent));
                stage.show();
            } catch (IOException e) {
                popUpController.popUpWindowSceneError("Modify Customer Button");
                System.out.println("That screen was not found.");
                e.printStackTrace();
            }
        } else {
            popUpController.popUpWindow(eTitle, eText, eHeader, eButton);
            return;
        }

    }

    /**
     * This takes the user-selected customer and deletes them from the database.
     * If no customer is selected, it warns the user and doesn't let them proceed.
     * @param event user selecting customer and then clicking delete
     * @throws SQLException in case a connection to the database can't be made
     */
    @FXML
    void deleteCustomer(ActionEvent event) throws SQLException {

        Customer customer = customerTable.getSelectionModel().getSelectedItem();

        if(customer == null) {
            popUpController.popUpWindow("No Customer was selected.\n", "Please select a customer from the table.", "No Customer Selected", "Okay!");
            System.out.println("No customer was selected");
            return;
        }

        ObservableList<Appointment> allRelatedAppts = DatabaseQueries.ifCustHasApptDoYouDelete(customer.getCustomer_ID());

        if(allRelatedAppts != null){
            boolean delete = DeleteConfirm.apptDeleteMod("Appointment Delete Confirm", "Delete", "customer", "Doing so will also delete their appointments.");

            if(delete == false) {
                return;
            }

            if (delete == true) {
                for (Appointment a : allRelatedAppts) {
                    int i = a.getAppointment_ID();
                    DatabaseQueries.removeApptFromDataByID(i);
                }
                popUpController.popUpWindow("Customer appointment", " has been deleted.", "Appointment Delete Confirm", "Ok");
            }
        }

        if (DatabaseQueries.removeCustomerFromDatabase(customer)) {
            popUpController.popUpWindow((customer.getCustomer_Name()), " has been deleted from the database", "Customer Deleted", "Okay");
            System.out.println("Customer was deleted");
            refreshTable();
            return;
        } else {
            System.out.println("Customer wasn't deleted");
            return;
        }
    }

    /**
     * This sends the user back to the main screen
     * @param event user clicking the cancel/main screen button
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
     * The user has the option to close out the whole program from the Customer main screen. If they choose to do so,
     * then it disconnects the program from the database and closes out the window.
     * @param event user selecting the exit program button
     */
    @FXML
    void closeProgram(ActionEvent event) {
        System.out.println("Exit button clicked from CustomerMainPage!");
        DatabaseConnection.dBDisconnect();
        System.exit(0);
    }

    /**
     * Whenever an update is made to the customer database, this method will re-populate the database with the most up-
     * to date information.
     * @throws SQLException
     */
    public void refreshTable() throws SQLException {

        try {
            customerTable.setItems(CustomerDatabase.getAllCustomersInDatabase());
            id.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
            name.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
            address.setCellValueFactory(new PropertyValueFactory<>("Address"));
            postalCode.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
            state.setCellValueFactory(new PropertyValueFactory<>("Division_ID"));
            phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This initializes the Language and displayed information before the user sees the page
     * @param url
     * @param resourceBundle This controls the language of the labels
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());
        customerLabel.setText(rb.getString("customer"));
        addCustButton.setText(rb.getString("addCust"));
        updateCustButton.setText(rb.getString("modCust"));
        delCustButton.setText(rb.getString("delCust"));
        goBackButton.setText(rb.getString("main"));
        exitButton.setText(rb.getString("exit"));
        eTitle = rb.getString("eTitle");
        eText = rb.getString("cText");
        eHeader = rb.getString("cHeader");
        eButton = rb.getString("eButton");

        try {
            customerTable.setItems(CustomerDatabase.getAllCustomersInDatabase());
            id.setCellValueFactory(new PropertyValueFactory<>("Customer_ID"));
            name.setCellValueFactory(new PropertyValueFactory<>("Customer_Name"));
            address.setCellValueFactory(new PropertyValueFactory<>("Address"));
            postalCode.setCellValueFactory(new PropertyValueFactory<>("Postal_Code"));
            state.setCellValueFactory(new PropertyValueFactory<>("Division_ID"));
            phone.setCellValueFactory(new PropertyValueFactory<>("Phone"));


        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
