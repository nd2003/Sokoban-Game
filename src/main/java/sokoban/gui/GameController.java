package sokoban.gui;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import lombok.NonNull;
import lombok.Setter;
import org.tinylog.Logger;
import sokoban.results.GameResultRepository;
import sokoban.state.Direction;
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

    private void resetGame() {

        countOfSteps.set(0);
        isSolved.set(false);

        // initializing watch
        startTime = Instant.now();
        if (stopwatch.getStatus() == Animation.Status.PAUSED) {
            stopwatch.reset();
        }
        stopwatch.start();

        // initializing board
        clearState();
        showState();
    }

    private void registerKeyEventHandler() {
        final KeyCombination restartKeyCombination = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        final KeyCombination quitKeyCombination = new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN);
        Platform.runLater(() -> grid.getScene().setOnKeyPressed(
                keyEvent -> {
                    if (restartKeyCombination.match(keyEvent)) {
                        Logger.debug("Restarting game...");
                        state = new SokobanState();
                        resetGame();
                    } else if (quitKeyCombination.match(keyEvent)) {
                        Logger.debug("Exiting...");
                        Platform.exit();
                    } else if (keyEvent.getCode() == KeyCode.UP) {
                        Logger.debug("Up arrow pressed");
                        performMove(Direction.UP);
                    } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                        Logger.debug("Right arrow pressed");
                        performMove(Direction.RIGHT);
                    } else if (keyEvent.getCode() == KeyCode.DOWN) {
                        Logger.debug("Down arrow pressed");
                        performMove(Direction.DOWN);
                    } else if (keyEvent.getCode() == KeyCode.LEFT) {
                        Logger.debug("Left arrow pressed");
                        performMove(Direction.LEFT);
                    }
                }
        ));
    }

    @FXML
    private void handleMouseClick(
            @NonNull final MouseEvent event) {

        final var source = (Node) event.getSource();
        final var row = GridPane.getRowIndex(source);
        final var col = GridPane.getColumnIndex(source);

        Logger.debug("Click on square ({},{})", row, col);

        getDirectionFromClickPosition(row, col)
                .ifPresentOrElse(this::performMove,
                        () -> Logger.warn("Click does not correspond to any direction"));
    }

    private void performMove(final Direction direction) {
    }

    private void performMove(
            final Direction direction) {

        if (state.isLegalMove(direction)) {
            Logger.info("Move: {}", direction);

            state.makeMove(direction);
            Logger.trace("New state: {}", state);

            countOfSteps.set(countOfSteps.get() + 1);

            showState();
            if (state.isSolved()) {
                isSolved.set(true);
            }
        } else {
            Logger.warn("Invalid move: {}", direction);
        }
    }

    public void handleResetButton(ActionEvent actionEvent) {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        Logger.info("Resetting game");
        stopwatch.stop();
        state = new SokobanState();
        resetGame();
    }



}
