package cookbook.Controller;

import cookbook.model.Help;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class HelpSceneController {

    @FXML
    private VBox helpVBox;

    @FXML
    private Label helpTitle;

    private Help helpItem;

    public void setHelpItem(Help item) {
        helpItem = item;
        helpTitle.setText(item.getTitle());
    }

    @FXML
    public void backClicked() {

    }
}

