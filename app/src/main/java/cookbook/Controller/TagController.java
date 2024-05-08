package cookbook.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TagController {
    @FXML
    private Label tagsLabel;

    public void setTagName(String name) {
        this.tagsLabel.setText(name);
    }

}
