package sokoban.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.tinylog.Logger;
import sokoban.results.GameResult;
import sokoban.results.GameResultRepository;
import util.javafx.FileChooserHelper;

import javax.inject.Inject;
import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;


/**
 * Controller class for managing the high score view.
 */
public final class HighScoreController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultRepository gameResultRepository;

    @FXML
    private TableView<GameResult> highScoreTable;

    @FXML
    private TableColumn<GameResult, String> player;

    @FXML
    private TableColumn<GameResult, Integer> steps;

    @FXML
    private TableColumn<GameResult, Duration> duration;

    @FXML
    private TableColumn<GameResult, ZonedDateTime> created;

    @FXML
    private void initialize() {
        Logger.debug("Loading high scores...");

        List<GameResult> highScoreList = gameResultRepository.findBest(10);

        player.setCellValueFactory(new PropertyValueFactory<>("player"));
        steps.setCellValueFactory(new PropertyValueFactory<>("steps"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        created.setCellValueFactory(new PropertyValueFactory<>("created"));

        duration.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Duration item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(DurationFormatUtils.formatDuration(item.toMillis(), "H:mm:ss"));
                }
            }
        });

        created.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);

            @Override
            protected void updateItem(ZonedDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.format(formatter));
                }
            }
        });

        ObservableList<GameResult> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

    /**
     * Handles the restart button action by loading the opening screen.
     *
     * @param actionEvent the ActionEvent triggered by the user
     * @throws IOException if an I/O error occurs during loading of the opening screen
     */
    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        fxmlLoader.setLocation(getClass().getResource("/fxml/opening.fxml"));
        Parent root = fxmlLoader.load();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Handles the save scoreboard button action by allowing the user to choose a file to save the scoreboard.
     *
     * @param actionEvent the ActionEvent triggered by the user
     */
    public void handleSaveScoreboard(ActionEvent actionEvent) {
        FileChooserHelper.show(
                        false,
                        (Stage) ((Node) actionEvent.getSource()).getScene().getWindow()
                )
                .ifPresent(file -> {
                    try {
                        gameResultRepository.saveToFile(file);
                    } catch (IOException ex) {
                        Logger.error(ex.getMessage());
                    }
                });

        Logger.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
    }
}
