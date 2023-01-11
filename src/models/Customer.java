package models;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */


/**
 * This class creates a Customer object to hold the Customer data from the database and allows us to manipulate
 * the data outside of the database before sticking it back in
 */
public final class Customer {

    private Integer Customer_ID;
    private String Customer_Name;
    private String Address;
    private String Postal_Code;
    private String Division_ID;
    private String Phone;


    public Customer() {}

    /**
     * The Constructor of the Customer Object with data pulled from the Database.
     * @param Customer_ID unique customer ID number from database
     * @param Customer_Name the name of the customer
     * @param Address the customer's street and city location
     * @param Postal_Code the customer's postal code (Zip code, etc.)
     * @param Division_ID the integer representation of the customer's state/province
     * @param Phone the customer's phone
     */
    public Customer(int Customer_ID, String Customer_Name, String Address, String Postal_Code, String Division_ID, String Phone) {
        this.Customer_ID = Customer_ID;
        this.Customer_Name = Customer_Name;
        this.Address = Address;
        this.Postal_Code = Postal_Code;
        this.Phone = Phone;
        this.Division_ID = Division_ID;

    }

    /**
     * The below code is all the getters and setters for the Customer object.
     */
    public Integer getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(Integer customer_ID) {
        Customer_ID = customer_ID;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPostal_Code() {return Postal_Code;}

    public void setPostal_Code(String postal_Code) {
        Postal_Code = postal_Code;
    }

    public String getDivision_ID() {
        return Division_ID;
    }

    public void setDivision_ID(String division_ID) {
        Division_ID = division_ID;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

}

