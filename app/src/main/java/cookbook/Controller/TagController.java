package cookbook.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TagController {
    @FXML
    private Label tagsLabel;

    @FXML
    private StackPane tagsPane;

    public void setTagName(String name) {
        tagsLabel.setText(name);
    }

    public String getTagName() {  // Ensure it returns String
        return tagsLabel.getText();
    }

    public void setCustom(){
        tagsPane.setStyle("-fx-background-color: blue; -fx-background-radius: 4;");
    }
}

