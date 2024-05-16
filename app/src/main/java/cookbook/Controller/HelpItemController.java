package cookbook.Controller;

import cookbook.model.Help;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

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
    public void helpItemClicked() {
        
    }
}
