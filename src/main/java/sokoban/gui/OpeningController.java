package sokoban.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.NonNull;
import org.tinylog.Logger;
import sokoban.results.GameResultRepository;
import util.javafx.FileChooserHelper;

import javax.inject.Inject;
import java.io.IOException;

public final class OpeningController {

    @Inject
    private FXMLLoader fxmlLoader;

    @Inject
    private GameResultRepository gameResultRepository;

    @FXML
    private TextField playerNameTextField;

    @FXML
    private Label errorLabel;

    public void loadScoreboardAction(
            @NonNull final ActionEvent actionEvent) {

        FileChooserHelper.show(
                        true,
                        (Stage) ((Node) actionEvent.getSource()).getScene().getWindow()
                )
                .ifPresent(file -> {
                    try {
                        gameResultRepository.loadFromFile(file);
                    } catch (IOException ex) {
                        Logger.error(ex.getMessage());
                    }
                });
    }

    public void startAction(
            @NonNull final ActionEvent actionEvent) throws IOException {

        if (playerNameTextField.getText().isEmpty()) {
            errorLabel.setText("Please enter your name!");
        } else {
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayerName(playerNameTextField.getText());
            stage.setScene(new Scene(root));
            stage.show();
            Logger.info("The user's name is set to {}, loading game scene", playerNameTextField.getText()); // TODO
        }
    }

}