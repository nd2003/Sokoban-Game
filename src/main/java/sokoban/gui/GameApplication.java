package sokoban.gui;

import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.AbstractModule;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;
import sokoban.results.GameResultRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;


/**
 * The main class of the Sokoban game application.
 */
public final class GameApplication extends Application {

    private final GuiceContext context = new GuiceContext(this, () -> List.of(
            new AbstractModule() {
                @Override
                protected void configure() {
                    bind(GameResultRepository.class).in(Singleton.class);
                }
            }
    ));

    @Inject
    private FXMLLoader fxmlLoader;

    /**
     * Starts the Sokoban game application.
     *
     * @param stage the primary stage for the application
     * @throws IOException if an I/O error occurs during loading of the opening screen
     */
    @Override
    public void start(Stage stage) throws IOException {
        Logger.info("Starting application");
        context.init();
        fxmlLoader.setLocation(getClass().getResource("/fxml/opening.fxml"));
        Parent root = fxmlLoader.load();
        stage.setTitle("Sokoban Game");
        stage.setResizable(false);
        stage.setScene(new Scene(root));
        stage.show();
    }

}
