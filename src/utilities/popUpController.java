package utilities;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class popUpController {

    /**
     * This creates a specific popup window for the add/delete of a customer to the database
     * @param SorF stands for success or fail. will populate the error message in the popup
     * @param cName stands for customer Name. will populate the error message in the popup
     * @param YorN stands for Yes or no. will populate the error message in the popup
     * @param button Sets the popup button "OK" text to the language selection of the user
     */
    public static void popUpAddCustomerSorF(String SorF, String cName, String YorN, String button) {
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(SorF);

        Label popUpMessage = new Label(cName + " " + YorN);

        Button okButton = new Button(button);
        okButton.setOnAction(e -> popUp.close());

        VBox popUpBox = new VBox();

        popUpBox.getChildren().addAll(popUpMessage, okButton);
        popUpBox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(popUpBox, 300, 100);
        popUp.setScene(scene);
        popUp.showAndWait();

    }

    /**
     * This creates a generic popup for if there is a window/scene load error.
     * @param clickedButton this is the name of the scene that it can't find to display in the popup
     */
    public static void popUpWindowSceneError(String clickedButton) {

        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle("Scene not found");

        /**
         * @param popUpMessage Sets up the message to display by combining a couple variables.
         * @param standardMessage This is the message that is placed between the custom message and the ok button
         */
        Label popUpMessage = new Label("The page for " + clickedButton + " was not found");
        //Label standardMessage = new Label("Please fix this error and try again.");

        /**
         * @param okButton sets up the okay button. When the button is clicked, the window disappears.
         */
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> popUp.close());

        VBox popUpBox = new VBox();
        //popUpBox.getChildren().addAll(popUpMessage, standardMessage, okButton);
        popUpBox.getChildren().addAll(popUpMessage, okButton);
        popUpBox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(popUpBox, 300, 100);
        popUp.setScene(scene);
        popUp.showAndWait();

    }

    /**
     * A generic popup window to use for small/unlikely errors
     * @param input is the error type itself to add to the actual display message
     * @param message is combined with the input parameter to make a complete message
     * @param title sets the title of the popup window
     * @param button sets the text of the button
     */
    public static void popUpWindow(String input, String message, String title, String button) {
        /**
         * @param popUp Sets up a new stage for the popup window to display to the user
         */
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.setTitle(title);

        /**
         * @param popUpMessage Sets up the message to display by combining a couple variables.
         * @param standardMessage This is the message that is placed between the custom message and the ok button
         */
        Label popUpMessage = new Label(input + " " + message);

        /**
         * @param okButton sets up the okay button. When the button is clicked, the window disappears.
         */
        Button okButton = new Button(button);
        okButton.setOnAction(e -> popUp.close());

        VBox popUpBox = new VBox();
        //popUpBox.getChildren().addAll(popUpMessage, standardMessage, okButton);
        popUpBox.getChildren().addAll(popUpMessage, okButton);
        popUpBox.setAlignment(Pos.CENTER);


        Scene scene = new Scene(popUpBox, 450, 150);
        popUp.setScene(scene);
        popUp.showAndWait();

    }

}
