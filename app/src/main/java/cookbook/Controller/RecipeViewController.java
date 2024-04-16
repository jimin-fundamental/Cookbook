package cookbook.Controller;

import java.io.IOException;
import java.lang.String;
import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;

import cookbook.SceneModifier;

public class RecipeViewController implements Initializable{

    @FXML
    private GridPane recipeContainer;

    @FXML
    private MenuItem changeProfiles;
    
    @FXML
    private VBox vBox;

    private List<String> recipeList;

    @FXML
    void changeProfileClicked(ActionEvent event) throws IOException {
        SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/LoginScene.fxml")), (Stage)vBox.getScene().getWindow());
    }

    private List<String> availableRecipes(){
        List<String> recipes = new ArrayList<>();
        recipes.add("pizza");
        recipes.add("lasagna");
        recipes.add("scrambled eggs");
        recipes.add("carbonara");
        recipes.add("potato slices");
        recipes.add("chili con carne");
        recipes.add("chili sin carne");
        recipes.add("sausages");
        recipes.add("burger");
        recipes.add("bread");
        return recipes;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recipeList = new ArrayList<>(availableRecipes());
        int column = 0;
        int row = 1;
        try {
            for (String recipe : recipeList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cookbook.view/RecipeItem.fxml"));
                StackPane recipeBox = fxmlLoader.load();
                RecipeItemController controller = fxmlLoader.getController();
                controller.setRecipeData(recipe);
                if(column == 5){
                    column = 0;
                    ++row;
                }
                recipeContainer.add(recipeBox, column++, row);
                    
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            

    }

}
