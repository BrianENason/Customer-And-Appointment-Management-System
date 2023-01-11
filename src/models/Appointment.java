package models;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import java.time.LocalDateTime;

/**
 * This Class creates an Appointment object to hold the appointment data from the Database.
 */
public final class Appointment {

    private Integer Appointment_ID;
    private String Title;
    private String Description;
    private String Location;
    private String Contact;
    private String Type;
    private LocalDateTime StartDateTime;
    private LocalDateTime EndDateTime;
    private Integer Customer_ID;


    private Integer User_ID;

    public Appointment() {}

    /**
     * This constructor will create the Appoinmtnet Object.
     * @param Appointment_ID the ID Number of the appointment
     * @param Title the title of the appointment
     * @param Description the purpose of the appointment
     * @param Location where the appointment will take place
     * @param Contact who in the company is to be contacted about the appointment
     * @param Type what type of appointment this will be
     * @param StartDateTime when the appointment is scheduled to begin
     * @param EndDateTime when the appointment is scheduled to end.
     * @param Customer_ID the ID of the customer who is connected to the appointment
     */
    public Appointment(int Appointment_ID, String Title, String Description, String Location, String Contact,
                       String Type, LocalDateTime StartDateTime, LocalDateTime EndDateTime, int Customer_ID, int User_ID) {
        this.Appointment_ID = Appointment_ID;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.Contact = Contact;
        this.Type = Type;
        this.StartDateTime = StartDateTime;
        this.EndDateTime = EndDateTime;
        this.Customer_ID = Customer_ID;
        this.User_ID = User_ID;
    }

    /**
     * Below are all the getter's and setter's for the Appoinmtnet Object.
     */
    public Integer getAppointment_ID() {
        return Appointment_ID;
    }

    public void setAppointment_ID(Integer appointment_ID) {
        Appointment_ID = appointment_ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public LocalDateTime getStartDateTime() {
        return StartDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        StartDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return EndDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        EndDateTime = endDateTime;
    }

    public Integer getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(Integer customer_ID) {
        Customer_ID = customer_ID;
    }

    public Integer getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(Integer user_ID) {
        User_ID = user_ID;
    }

}





