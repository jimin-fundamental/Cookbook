package cookbook.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TagController {
    @FXML
    private Label tagsLabel;

    public void setTagName(String name) {
        tagsLabel.setText(name);
    }

    public String getTagName() {  // Ensure it returns String
        return tagsLabel.getText();
    }
}

