package cookbook.Controller;

import java.io.IOException;

import cookbook.SceneModifier;
import cookbook.model.Help;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelpSceneController {

    @FXML
    private VBox helpVBox;

    @FXML
    private Label helpTitle;

    private Help helpItem;

    private Scene previousScene;

    public void setHelpItem(Help item) {
        helpItem = item;
        helpTitle.setText(item.getTitle());
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    public void backClicked(MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(previousScene);
    }
}
