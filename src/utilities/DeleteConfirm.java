package utilities;

/**
 * @author Brian Nason, Student Number xxxxxxxx
 */


import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This Class is of its own as it controls the Delete Confirm window that is presented to the user whenever they wish to
 * remove/delete an object
 */
public class DeleteConfirm{

    /**     *
     * @param confirm This is used to confirm or deny whether the user wants to delete/remove an object.
     */
    static boolean confirm;

    /**
     * Lambda is used here. This creates a new selection popup window. It allows the user to approve or deny something,
     * returning a boolean value in the process.
     *
     * Lambdas are used in the button trigger methods to make it cleaner as to what is going on and to compress the button
     * action into a clear interface
     *
     * @param title The title of the box
     * @param type Delete vs. modify
     * @param typeObject the type of object in question (Customer or appointment)
     * @param addtlInfo any other info to pass to the box for the customer to see
     * @return if the user selects yes, then boolean return is true, otherwise boolean is false.
     */
    public static boolean apptDeleteMod(String title, String type, String typeObject, String addtlInfo) {

        /**
         * @param deleteConfirmBox Sets up a new Stage with the title
         */
        Stage deleteConfirmBox = new Stage();
        deleteConfirmBox.initModality(Modality.APPLICATION_MODAL);
        deleteConfirmBox.setTitle(title);

        /**
         * This will take the passed variables and put them together into a confirmation sentence
         * @param deleteConfirmMessage sets up the information into the scene/stage to display to the user
         */
        Label deleteConfirmMessage = new Label();
        deleteConfirmMessage.setText("Are you sure you want to " + type + " selected " + typeObject + "?\n " + addtlInfo);

        /**
         * Sets up a button for the user to click "Yes" to confirm the action. If so, the return is true and the box is closed
         */
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            confirm = true;
            deleteConfirmBox.close();
        });

        /**
         * Sets up a button for the user to click "No" to deny the action. If so, the return is false and the box is closed
         */
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            confirm = false;
            deleteConfirmBox.close();
        });


        HBox buttonPane = new HBox(20, yesButton, noButton);
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonPane.setPadding(new Insets(30));

        StackPane labelPane = new StackPane(deleteConfirmMessage);
        labelPane.setAlignment(Pos.TOP_CENTER);
        labelPane.setPadding(new Insets(20));

        StackPane deleteConfirmPane = new StackPane(labelPane, buttonPane);

        Scene scene = new Scene(deleteConfirmPane, 350, 150);
        deleteConfirmBox.setScene(scene);
        deleteConfirmBox.showAndWait();

        return confirm;

    }

    /**
     * Lambdas are used here. This method shows a popup for the user to select whether or not they want the action performed.
     *
     * Lambdas are used in the button trigger methods to make it cleaner as to what is going on and to compress the button
     * action into a clear interface
     *
     *
     * @param title Title is the title of the box set by the window it is called from
     * @param type Type is either add or remove depending on the window it is called from
     * @param objectName This is the name of the object (either Appointment or customer) the user wishes to remove/delete
     * @param typeObject This is the type of the object (either Appointment or customer) the user wishes to remove/delete
     * @return This returns T or F depending on if the user wants to delete or not.
     */
    public static boolean showBox(String title, String type, String typeObject, String objectName) {

        /**
         * @param deleteConfirmBox Sets up a new Stage with the title
         */
        Stage deleteConfirmBox = new Stage();
        deleteConfirmBox.initModality(Modality.APPLICATION_MODAL);
        deleteConfirmBox.setTitle(title);

        /**
         * This will take the passed variables and put them together into a confirmation sentence
         * @param deleteConfirmMessage sets up the information into the scene/stage to display to the user
         */
        Label deleteConfirmMessage = new Label();
        deleteConfirmMessage.setText("Are you sure you want to " + type + "\n selected " + typeObject + ": " + objectName + "?");

        /**
         * Sets up a button for the user to click "Yes" to confirm the action. If so, the return is true and the box is closed
         */
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            confirm = true;
            deleteConfirmBox.close();
        });

        /**
         * Sets up a button for the user to click "No" to deny the action. If so, the return is false and the box is closed
         */
        Button noButton = new Button("No");
        noButton.setOnAction(e -> {
            confirm = false;
            deleteConfirmBox.close();
        });


        HBox buttonPane = new HBox(20, yesButton, noButton);
        buttonPane.setAlignment(Pos.BOTTOM_CENTER);
        buttonPane.setPadding(new Insets(30));

        StackPane labelPane = new StackPane(deleteConfirmMessage);
        labelPane.setAlignment(Pos.TOP_CENTER);
        labelPane.setPadding(new Insets(20));

        StackPane deleteConfirmPane = new StackPane(labelPane, buttonPane);

        Scene scene = new Scene(deleteConfirmPane, 350, 150);
        deleteConfirmBox.setScene(scene);
        deleteConfirmBox.showAndWait();

        return confirm;

    }
}