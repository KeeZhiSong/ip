import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sigmawolf.SigmaWolf;

/**
 * A GUI for SigmaWolf using FXML.
 */
public class Main extends Application {

    private SigmaWolf sigmaWolf = new SigmaWolf("./data/sigmawolf.txt");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("SigmaWolf - Alpha Chat");
            stage.setMinHeight(400);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setSigmaWolf(sigmaWolf);
            stage.show();
            stage.toFront();
            stage.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
