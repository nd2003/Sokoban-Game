package sokoban.gui;

import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.NonNull;
import lombok.Setter;
import org.tinylog.Logger;
import sokoban.results.GameResultRepository;
import sokoban.state.Direction;
import sokoban.state.Position;
import sokoban.state.SokobanState;
import util.javafx.ControllerHelper;
import util.javafx.Stopwatch;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
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

    public void handleGiveUpFinishButton(
            @NonNull final ActionEvent actionEvent) throws IOException {

        final var buttonText = ((Button) actionEvent.getSource()).getText();
        Logger.debug("{} is pressed", buttonText);
        if (Objects.equals(buttonText, "Give Up")) {
            stopwatch.stop();
        }

        Logger.debug("Saving result");
        gameResultRepository.addOne(createGameResult());

        ControllerHelper.loadAndShowFXML(
                fxmlLoader,
                "/fxml/high-scores.fxml",
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow()
        );
    }

    private void handleSolved(ObservableValue<? extends Boolean> observableValue, boolean oldValue, boolean newValue) {
        if (newValue) {
            Logger.info("Player {} has solved the game in {} steps", playerName, countOfSteps.get());
            stopwatch.stop();
            messageLabel.setText(String.format("Congratulations, %s!", playerName));
            resetButton.setDisable(true);
            giveUpFinishButton.setText("Finish");
        }
    }

    private void populateGrid() {
        for (int row = 0; row < grid.getRowCount(); row++) {
            for (int col = 0; col < grid.getColumnCount(); col++) {
                final var square = new StackPane();
                square.getStyleClass().add("square");
                var objectOnPosition = state.board.getPosition(row,col);

                if (objectOnPosition != SokobanState.WALL &&
                        objectOnPosition != SokobanState.OUT_OF_BOARD &&
                        objectOnPosition != SokobanState.TARGET) {
                    square.getStyleClass().add("light");
                    square.setOnMouseClicked(this::handleMouseClick);
                    grid.add(square, col, row);
                }
                if (objectOnPosition == SokobanState.OUT_OF_BOARD) {
                    square.getStyleClass().add("out");
                    grid.add(square, col, row);
                }
                if (objectOnPosition == SokobanState.WALL) {
                    square.getStyleClass().add("wall");
                    grid.add(square, col, row);
                }
                if(objectOnPosition == SokobanState.TARGET) {
                    square.getStyleClass().add("target");
                    square.setOnMouseClicked(this::handleMouseClick);
                    grid.add(square, col, row);
                }
            }
        }
    }

    private void clearState() {
        for (int row = 0; row < grid.getRowCount(); row++) {
            for (int col = 0; col < grid.getColumnCount(); col++) {
                getGridNodeAtPosition(grid, row, col)
                        .ifPresent(node -> ((StackPane) node).getChildren().clear());
            }
        }
    }

    private void showState() {
        clearState();

        Position playerPosition = state.getPosition(SokobanState.PLAYER_POSITION);
        getGridNodeAtPosition(grid, playerPosition)
                .ifPresent(node -> ((StackPane) node).getChildren().add(pieceViews[0]));

        //Logger.info(playerPosition.toString());

        Position firstBoxPosition = state.getPosition(SokobanState.FIRST_BOX_POSITION);
        getGridNodeAtPosition(grid, firstBoxPosition)
                .ifPresent(node -> ((StackPane) node).getChildren().add(pieceViews[1]));

        //Logger.info(firstBoxPosition.toString());

        Position secondBoxPosition = state.getPosition(SokobanState.SECOND_BOX_POSITION);
        getGridNodeAtPosition(grid, secondBoxPosition)
                .ifPresent(node -> ((StackPane) node).getChildren().add(pieceViews[2]));

        //Logger.info(secondBoxPosition.toString());

        Position thirdBoxPosition = state.getPosition(SokobanState.THIRD_BOX_POSITION);
        getGridNodeAtPosition(grid, thirdBoxPosition)
                .ifPresent(node -> ((StackPane) node).getChildren().add(pieceViews[3]));

        //Logger.info(thirdBoxPosition.toString());

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j <9; j++) {
                System.out.print(state.board.getPosition(i,j) + " ");

            }
            System.out.println();

        }
    }

    private Optional<Direction> getDirectionFromClickPosition(
            final int row,
            final int col) {

        final var playerPosition = state.getPosition(SokobanState.PLAYER_POSITION);
        return Optional.of(Direction.of(row - playerPosition.row(), col - playerPosition.col()));
    }



}
