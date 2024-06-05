package sokoban.gui;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import lombok.Setter;
import org.tinylog.Logger;
import sokoban.results.GameResultRepository;
import sokoban.state.SokobanState;
import util.javafx.Stopwatch;

import javax.inject.Inject;
import java.time.Instant;
import java.util.stream.Stream;

public final class GameController {
    @FXML
    private Label messageLabel;

    @FXML
    private GridPane grid;

    @FXML
    private TextField numberOfMovesField;

    @FXML
    private Label stepsLabel;

    @FXML
    private Button resetButton;

    @FXML
    private Button giveUpFinishButton;

    @FXML
    private Label stopwatchLabel;

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultRepository gameResultRepository;

    @Setter
    private String playerName;

    private BooleanProperty isSolved = new SimpleBooleanProperty();

    private IntegerProperty countOfSteps = new SimpleIntegerProperty();

    private ImageView[] pieceViews;

    private SokobanState state;

    private Stopwatch stopwatch = new Stopwatch();

    private Instant startTime;

    @FXML
    private void initialize() {
        // creating bindings
        stepsLabel.textProperty().bind(countOfSteps.asString());
        stopwatchLabel.textProperty().bind(stopwatch.hhmmssProperty());
        isSolved.addListener(this::handleSolved);

        // loading images
        pieceViews = Stream.of("man.png", "box.png", "box_2.png", "box_3.png")
                .map(s -> "/images/" + s)
                .peek(s -> Logger.debug("Loading image resource {}", s))
                .map(Image::new)
                .map(ImageView::new)
                .toArray(ImageView[]::new);

        state = new SokobanState();
        // populating grid
        populateGrid();

        // registering listeners
        registerKeyEventHandler();

        // starting new game
        Platform.runLater(() -> messageLabel.setText(String.format("Good luck, %s!", playerName)));
        resetGame();
    }

    
}
