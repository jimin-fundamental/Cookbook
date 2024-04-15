package cookbook.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class StartSceneController implements Initializable{

    @FXML
    private ListView<String> recipeListView;

    @FXML
    private Font x1;

    @FXML
    private Color x2;

    @FXML
    private Font x3;

    @FXML
    private Color x4;

    String[] recipes = {"pasta", "pizza"};

    String selectedRecipe;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recipeListView.getItems().addAll(recipes);
        recipeListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                selectedRecipe = recipeListView.getSelectionModel().getSelectedItem();
            }
            
        });
    }

}