package cookbook.Controller;

import cookbook.model.Help;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HelpSceneController {

    @FXML
    private VBox helpVBox;

    @FXML
    private Text helpTitle;

    private Help helpItem;

    public void setHelpItem(Help item) {
        helpItem = item;
        helpTitle.setText(item.getTitle());
    }

    @FXML
    public void BackClicked() {

    }
}

