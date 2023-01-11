package main;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import DatabaseControls.DatabaseConnection;
import DatabaseControls.DatabaseQueries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * This will initialize the connection to the database, get the exact time the program is loaded, and then use that
     * time to run a check against the database for any appointments that will be happening within 15 minutes of the user login.
     * This will all happen BEFORE the login screen is displayed to the user.
     * @param args
     */
    public static void main(String[] args) {

        DatabaseConnection.dBConnect();
        DatabaseQueries.getTime();
        //FIXME: This is where you are going to put the data check and the loading information BEFORE the actual login screen

        //FIXME: This line is for debug purposes
        System.out.println("\nYou are connected to: \n" + DatabaseConnection.getConnection());
        launch(args); //This is what will launch the program to the user.
        DatabaseConnection.dBDisconnect(); //This will process when the user ends the program to make sure the Database gets disconnected.
    }

    /**
     * The override main that loads the acutal program once the Database is connected and the appointment time check is completed.
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("/scenes/SplashScreen.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/Views_and_Controllers/LoginScreen.fxml"));
        primaryStage.setTitle("Brian Nason Final Project for: Software II - Advanced Java Concepts – C195");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
