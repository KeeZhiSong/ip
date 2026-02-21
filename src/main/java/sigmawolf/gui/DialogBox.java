package sigmawolf.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box so the ImageView is on the left and text on the right,
     * and clips the avatar into a circle.
     */
    private void flipBase() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);

        // Clip avatar into a circle
        double radius = displayPicture.getFitWidth() / 2;
        displayPicture.setClip(new Circle(radius, radius, radius));
    }

    /**
     * Flips the dialog box for a normal SigmaWolf reply.
     */
    private void flip() {
        flipBase();
        dialog.getStyleClass().add("reply-label");
    }

    /**
     * Flips the dialog box for an error reply with error-specific styling.
     */
    private void flipAsError() {
        flipBase();
        dialog.getStyleClass().add("error-label");
    }

    private void applyCommandStyle(String commandType) {
        switch (commandType) {
        case "add":
            dialog.getStyleClass().add("add-label");
            break;
        case "marked":
            dialog.getStyleClass().add("marked-label");
            break;
        case "delete":
            dialog.getStyleClass().add("delete-label");
            break;
        case "find":
            dialog.getStyleClass().add("find-label");
            break;
        default:
            // No special styling
        }
    }

    /**
     * Creates a dialog box for user messages (no avatar, right-aligned).
     *
     * @param text The text to display.
     * @param img The user's avatar image (not displayed).
     * @return A DialogBox displaying the user's message.
     */
    public static DialogBox getUserDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.displayPicture.setVisible(false);
        db.displayPicture.setManaged(false);
        return db;
    }

    /**
     * Creates a dialog box for SigmaWolf's replies.
     *
     * @param text The text to display.
     * @param img The SigmaWolf avatar image.
     * @return A DialogBox displaying SigmaWolf's reply.
     */
    public static DialogBox getDukeDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        return db;
    }

    /**
     * Creates a dialog box for SigmaWolf's replies with command-specific styling.
     *
     * @param text The text to display.
     * @param img The SigmaWolf avatar image.
     * @param commandType The type of command for styling (e.g., "add", "delete").
     * @return A DialogBox displaying SigmaWolf's reply with styling.
     */
    public static DialogBox getDukeDialog(String text, Image img, String commandType) {
        var db = new DialogBox(text, img);
        db.flip();
        db.applyCommandStyle(commandType);
        return db;
    }

    /**
     * Creates a dialog box for error messages with distinct error styling.
     *
     * @param text The error text to display.
     * @param img The SigmaWolf avatar image.
     * @return A DialogBox displaying the error message.
     */
    public static DialogBox getErrorDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flipAsError();
        return db;
    }
}
