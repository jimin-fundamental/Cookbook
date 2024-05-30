package cookbook.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import cookbook.repository.ThemesRepository;
import cookbook.SceneModifier;
import cookbook.model.Help;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelpItemController implements Initializable{

    @FXML
    private Text helpTitle;

    @FXML
    private Label helpDescription;

    private Help helpItem;

    public void setHelpItem(Help item) {
        helpItem = item;
        helpTitle.setText(item.getTitle());
        helpDescription.setText(item.getDescription().replace("\n", "").replace("\r", ""));
    }

    @FXML
    public void helpItemClicked(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/HelpScene.fxml"));
            ScrollPane helpViewScene = loader.load();
            HelpSceneController helpController = loader.getController(); // Get the controller
            helpController.setHelpItem(helpItem);
            helpController.setPreviousScene((Scene) ((Node) event.getSource()).getScene());
            SceneModifier.change_scene(
                    helpViewScene,
                    (Stage) ((Node) event.getSource()).getScene().getWindow());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helpTitle.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(helpTitle.getScene());
            }
        });
    }
}