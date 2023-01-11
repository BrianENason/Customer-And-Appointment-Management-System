package Views_and_Controllers;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseQueries;
import javafx.scene.control.*;
import models.Customer;
import models.CustomerDatabase;
import utilities.LanguageControl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ResourceBundle;

/**
 * This class controls the Customer Info Modify Scene
 */
public class CustomerInfoModifyController implements Initializable {

    Stage stage;
    String Sorf;
    String sorF;
    String Yorn;
    String yorN;
    String button;

    private final ObservableList<String> countries = FXCollections.observableArrayList("-Please select Country", "United States", "England", "Canada");
    private Integer customer_id;

    @FXML
    private Label modCustTitle;

    @FXML
    private Label custID;

    @FXML
    private Label name;

    @FXML
    private Label address;

    @FXML
    private TextField custPostCode;

    @FXML
    private Label phone;

    @FXML
    private TextField custIdField;

    @FXML
    private TextField customerName;

    @FXML
    private TextField customerAddress;

    @FXML
    private TextField customerPhone;

    @FXML
    private Button modCustButton;

    @FXML
    private Button goBack;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private ComboBox<String> firstLevelComboBox;

    /**
     * This will add the modifications that have been made on the modify customer form to the Database. It will check to
     * make sure there is data in every field, and that data is useful. It won't let the user continue if any fields are
     * empty or any selections not made
     * @param event when user clicks modify, it will save all changes made
     * @throws SQLException
     */
    @FXML
    void addCustomer(ActionEvent event) throws SQLException {

        Integer Customer_ID = Integer.parseInt(custIdField.getText());
        String Customer_Name = customerName.getText();
        String Address = customerAddress.getText();
        String Phone = customerPhone.getText();
        String city = firstLevelComboBox.getValue();
        String Division_ID = DatabaseQueries.getDivision_ID(city);
        String Post_Code = custPostCode.getText();

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

        if(DatabaseQueries.addModCustomerToDatabase(customer)) {
            popUpController.popUpAddCustomerSorF("Success", Customer_Name, "Has been UPDATED in the database.", button);
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
     * This is used by the initializer to populate the first level address information for the form and is dependent
     * on the country selection combo box.
     * @param event When the user makes any changes to the Country combo box
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
     * This will populate the first level combo box in initialization to handle the existing customer's information
     * before the modifications happen
     */
    public void popFirstLevelComboBox () {

            int locationCode = countryComboBox.getSelectionModel().getSelectedIndex();
            if (locationCode >= 1) {
                firstLevelComboBox.setItems(null);
                firstLevelComboBox.setItems(DatabaseQueries.getLocations(locationCode));
            }
            if (locationCode == 0) {
                firstLevelComboBox.setItems(null);
        }
    }


    /**
     * This will take the customer object that is passed from the main screen, extract the data from it, and place
     * the exising data into the appropriate fields.
     * @param c the Customer object that is being passed to the modify form
     */
    public void popCustomerInfoFields(Customer c){

        ObservableList<String> firstLevel = FXCollections.observableArrayList("- Please select your location -");

        countryComboBox.setItems(countries);

        customer_id = c.getCustomer_ID();
        custIdField.setText(customer_id.toString());
        customerName.setText(c.getCustomer_Name());
        customerAddress.setText(c.getAddress());
        customerPhone.setText(c.getPhone());
        custPostCode.setText(c.getPostal_Code());
        String divID = c.getDivision_ID();
        //System.out.println("The divID is " + divID);

        int Division_ID = DatabaseQueries.getCountryFromDivision(divID);

        countryComboBox.getSelectionModel().select(Division_ID);
        System.out.println("The Division_ID is... " + Division_ID);

        firstLevel = DatabaseQueries.getLocations(Division_ID);
        firstLevelComboBox.setItems(firstLevel);
        firstLevelComboBox.getSelectionModel().select(divID);

    }

    /**
     * Sends the user back to the main customer scene
     * @param event when user selects the cancel button.
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
     * Initializes a lot of the data and labels into their language form as well as populates the first level combo
     * box and the customer id combo box before the user sees the scene
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ResourceBundle rb = LanguageControl.getResourceBundle(LanguageControl.getUserLocation());

        modCustTitle.setText(rb.getString("modCustTitle"));
        custID.setText(rb.getString("customerId"));
        name.setText(rb.getString("name"));
        address.setText(rb.getString("address"));
        phone.setText(rb.getString("phone"));
        custIdField.setPromptText(rb.getString("idFieldText"));
        customerName.setPromptText(rb.getString("firstLastName"));
        modCustButton.setText(rb.getString("modCust"));
        goBack.setText(rb.getString("cancel"));
        Sorf = rb.getString("fail");
        sorF = rb.getString("success");
        Yorn = rb.getString("yes");
        yorN = rb.getString("no");
        button = rb.getString("eButton");

        //countryComboBox.setItems(countries);

        popFirstLevelComboBox();

        //This is to test the database and the customers
        try {
            CustomerDatabase.getAllCustomersInDatabase();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

    }
}

