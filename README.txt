This application is formally called:
"Global Consulting Customer and Appointment"


Author: Brian Nason
Student Number: 001003011
Date: June 6th, 2021


Contact Information:
    eMail =     bnason1@my.wgu.edu
    Phone =     (949) 533-7089
    Address =   2515 Old Quarry Rd. Apt. 1323
                San Diego, CA 92108


Application version Number:     02.27.100c
IDE:                            IntelliJ 2020.3 (Community)
JDK:                            Java JDK-11.0.9
JavaFX:                         javafx-sdk-11.0.2
MySQL Connector Driver Number:  mysql-connector-java-8.0.23.jar


Purpose -

The purpose of this program is to create a user-friendly representation of the "Global Consulting"'s database of
Users, Customers, and Appointments and allow the user to view, add, modify, and/or delete appointments and/or Customers.


Revision Notes -

*This Program's initial version 02.27.100a was created on Feb 27th, 2021.
*The Finalized version 02.27.100b was created on April 30th, 2021
*Finalized version 02.27.100b was Submitted for approval on June 3rd, 20201.
*Finalized version 02.27.100b was subsequently returned for revisions.
*Version 02.27.100c was created on June 5th, 2021 to address 4 needed areas of improvement:
1) The Calendar view did not properly populate the appointment tab for "Week View".
        -This was fixed on June 6th, 2021 and added to revised version number 02.27.100c.
2) The first report in the report screen improperly used the appointment "Description" instead of "Type" to group in the view.
        -This was fixed on June 6th, 2021 and added to revised version number 02.27.100c.
3) The expected "login_activity.txt" file was improperly named "LoginLogBook.txt".
        -This was fixed on June 6th, 2021 and added to revised version number 02.27.100c.
4) The "Readme.txt" file was not descriptive enough. It failed to include "The purpose, date, detailed directions on how
to run the application, and the MySQL Connector driver version number, including the update number".
        -This was fixed on June 6th, 2021 and added to revised version number 02.27.100c.


Instruction on how to run the program -

1) To Run Program, make sure "Main.java" is selected in the "Select run/debug configuration".
2) Hold down [Shift + f10] to run the program.
3) Enter your username/password to gain access.
4) Connection and subsequent disconnect to/from the database is handled by the program.


Information about the "Report of My Choice" -

The additional report run in part A3f parses the data from 3 different sources: customer table,
first_level_division table, and countries table, and then arranges it all with JOINs, GROUP BYs, and
ORDER BYs to output a report that generates the number of customers in each country and then sorts them
by their first-level location
