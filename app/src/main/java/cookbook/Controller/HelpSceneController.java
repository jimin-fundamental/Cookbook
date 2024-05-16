package cookbook.Controller;

import java.util.List;

import cookbook.model.Help;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.sandec.mdfx.MarkdownView;

public class HelpSceneController {

    @FXML
    private VBox helpVBox;

    @FXML
    private Label helpTitle;

    private Help helpItem;

    private Scene previousScene;

    public void setHelpItem(Help item) {
        helpItem = item;
        helpTitle.setText(item.getTitle());;

        MarkdownView markdownView = new MarkdownView(item.getDescription()) {
            @Override
            protected List<String> getDefaultStylesheets() {
                return List.of("/css/mdfx.css");
            }

            @Override
            public void setLink(Node node, String link, String description) {
                node.setCursor(Cursor.HAND);
                node.setOnMouseClicked(e -> {
                    System.out.println("link: " + link);
                });
            }
        };

        TextArea textArea = new TextArea(item.getDescription());

        markdownView.mdStringProperty().bind(textArea.textProperty());
        markdownView.getStylesheets().add("/css/mdfx.css");

        helpVBox.getChildren().add(markdownView);
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;
    }

    @FXML
    public void backClicked(MouseEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).setScene(previousScene);
    }
}
