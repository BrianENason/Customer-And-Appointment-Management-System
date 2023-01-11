package Views_and_Controllers;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseQueries;
import models.Customer;
import utilities.LanguageControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilities.popUpController;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * This class controls the Add Customer scene
 */
public class CustomerInfoAddController implements Initializable {

    private final ObservableList<String> countries = FXCollections.observableArrayList("-Please select Country", "United States", "England", "Canada");
    Stage stage;
    String Sorf;
    String sorF;
    String Yorn;
    String yorN;
    String button;

    @FXML
    private Label addCustTitle;

    @FXML
    private Label custID;

    @FXML
    private Label name;

    @FXML
    private Label address;

    @FXML
    private Label phone;

    @FXML
    private TextField custIdField;

    @FXML
    private TextField customerName;

    @FXML
    private TextField addressField;

    @FXML
    private TextField postCodeField;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private ComboBox<String> firstLevelComboBox;

    @FXML
    private TextField phoneField;

    @FXML
    private Button goBack;

    @FXML
    private Button addCustButton;



    /**
     * This will take the input from the country combo box and populate the first level division data into the location
     * dropdown.
     * @param event
     */
    @FXML
    void populateFirstLevel(ActionEvent event) {
        int locationCode = countryComboBox.getSelectionModel().getSelectedIndex();
        if (locationCode >= 1) {

            firstLevelComboBox.setItems(null);
            firstLevelComboBox.setPromptText("- Please select your location -");
            firstLevelComboBox.setItems(DatabaseQueries.getLocations(locationCode));
        }
        if (locationCode == 0) {
            firstLevelComboBox.setPromptText("- You must first select Country from above -");
            firstLevelComboBox.setItems(null);

        }
    }

    /**
     * This will add the new customer to the database based on the inputs in the form. The Customer_ID is generated
     * at the initialization of the page.
     * @param event Clicking the Add button
     * @throws SQLException In case there is an SQL connection error, it will prevent the system from trying
     * to add the customer
     */
    @FXML
    void addCustomer(ActionEvent event) throws SQLException {

        Integer Customer_ID = Integer.parseInt(custIdField.getText());
        String Customer_Name = customerName.getText();
        String Address = addressField.getText();
        String Phone = phoneField.getText();
        String city = firstLevelComboBox.getValue();
        String Division_ID = DatabaseQueries.getDivision_ID(city);
        String Post_Code = postCodeField.getText();

        if(Customer_Name.equals("") | Address.equals("") | Phone.equals("") | Post_Code.equals("") | city == null) {
            popUpController.popUpWindow("Missing data or no data entered!",
                    "\nPlease check that all fields have data!", "Error", "OK");
            return;
        }

        if (Division_ID == null) {
            popUpController.popUpWindow("Error ","finding the customer's city code.\n Customer will not be added!", "Data Error!", "OK");
            return;
        }

        Customer customer = new Customer(Customer_ID, Customer_Name, Address, Post_Code, Division_ID, Phone);

        if(DatabaseQueries.addCustomerToDatabase(customer)) {
            popUpController.popUpAddCustomerSorF(sorF, Customer_Name, Yorn, button);
            try {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/CustomerInfoMain.fxml"));
                stage.setScene(new Scene(sceneParent));
                stage.show();
            } catch (IOException e) {
                popUpController.popUpWindowSceneError("Customer Info Main Screen");
                System.out.println("That screen was not found.");
                e.printStackTrace();
            }

        } else {
            popUpController.popUpAddCustomerSorF(Sorf, Customer_Name, yorN, button);
        }

    }

    /**
     * When the Cancel button is clicked on the Add form, then the user is sent back to the Customer main
     * @param event the event is the clicking of the Cancel button.
     */
    @FXML
    void goBack(ActionEvent event) {

        try {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent sceneParent = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/CustomerInfoMain.fxml"));
            stage.setScene(new Scene(sceneParent));
            stage.show();
        } catch (IOException e) {
            popUpController.popUpWindowSceneError("Customer Info Main Screen");
            System.out.println("That screen was not found.");
            e.printStackTrace();
        }

    }

    /**
     * This will initialize the form with the proper language settings for the fields. It will also generate a new
     * customer id based on the largest existing customer id plus 1
     * @param url
     * @param resourceBundle The Language Bunle in the Language package
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());
        addCustTitle.setText(rb.getString("addNewCust"));
        custID.setText(rb.getString("customerId"));
        name.setText(rb.getString("name"));
        address.setText(rb.getString("address"));
        phone.setText(rb.getString("phone"));
        customerName.setPromptText(rb.getString("firstLastName"));
        addCustButton.setText(rb.getString("addCust"));
        goBack.setText(rb.getString("cancel"));
        Sorf = rb.getString("fail");
        sorF = rb.getString("success");
        Yorn = rb.getString("yes");
        yorN = rb.getString("no");
        button = rb.getString("eButton");

        countryComboBox.setItems(countries);
        firstLevelComboBox.setPromptText("- Please select Country from above -");

        /**
         * This will generate a new Customer_ID to be used in the creation of the new customer.
         */
        try {
            custIdField.setText(DatabaseQueries.getNewCustomerID().toString());
        } catch (SQLException e) {
            System.out.println("Error generating new customer ID!");
            System.out.println("Customer ID will temporarily be set to 9999");
            custIdField.setText("9999");
        }

    }
}

