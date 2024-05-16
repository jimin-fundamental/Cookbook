package cookbook.Controller;

import java.io.IOException;

import cookbook.SceneModifier;
import cookbook.model.Help;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HelpItemController {

    @FXML
    private Text helpTitle;

    @FXML
    private Label helpDescription;

    private Help helpItem;

    public void setHelpItem(Help item) {
        helpItem = item;
        helpTitle.setText(item.getTitle());
        helpDescription.setText(item.getDescription().replace("\n", ""));
    }

    public void setHelpDescription(String description) {
        helpDescription.setText(description);
    }

    @FXML
    public void helpItemClicked(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cookbook.view/HelpScene.fxml"));
            ScrollPane helpViewScene = loader.load();
            HelpSceneController helpController = loader.getController(); // Get the controller
            helpController.setHelpItem(helpItem);
            SceneModifier.change_scene(
                    helpViewScene,
                    (Stage) ((Node) event.getSource()).getScene().getWindow());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
