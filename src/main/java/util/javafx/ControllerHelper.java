package util.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.IOException;

/**
 * Provides helper methods for controllers.
 */
public final class ControllerHelper {

    /**
     * Loads an FXML resource using the provided FXMLLoader, sets it as the root for the stage's scene,
     * and displays the scene on the stage.
     *
     * @param fxmlLoader   the FXMLLoader instance used to load the FXML resource
     * @param resourceName the name of the FXML resource to load
     * @param stage        the stage on which the FXML scene will be displayed
     * @throws IOException if an error occurs during loading of the FXML resource
     */
    public static void loadAndShowFXML(FXMLLoader fxmlLoader, String resourceName, Stage stage) throws IOException {
        Logger.trace("Loading FXML resource {}", resourceName);
        fxmlLoader.setLocation(fxmlLoader.getClass().getResource(resourceName));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

}