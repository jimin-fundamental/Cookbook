package cookbook.Controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import cookbook.repository.ThemesRepository;
import cookbook.model.Help;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.sandec.mdfx.MarkdownView;

public class HelpSceneController implements Initializable{

    @FXML
    private VBox helpVBox;

    @FXML
    private Label helpTitle;

    private Scene previousScene;

    public void setHelpItem(Help item) {
        helpTitle.setText(item.getTitle());;

        MarkdownView markdownView = new MarkdownView(item.getText()) {
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

        TextArea textArea = new TextArea(item.getText());

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        helpVBox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                System.out.println("Scene is now set.");
                ThemesRepository.applyTheme(helpVBox.getScene());
            }
        });
    }
}
