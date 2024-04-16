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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import cookbook.DatabaseManager;
import cookbook.SceneModifier;
import cookbook.model.Recipe;
import cookbook.repository.MySqlRecipeRepository;

public class RecipeViewController implements Initializable{

    @FXML
    private GridPane recipeContainer;

    @FXML
    private MenuItem changeProfiles;
    
    @FXML
    private VBox vBox;

    private List<Recipe> recipeList;
    private MySqlRecipeRepository recipeRepos;

    @FXML
    void changeProfileClicked(ActionEvent event) throws IOException {
        SceneModifier.change_scene(FXMLLoader.load(getClass().getResource("/cookbook.view/LoginScene.fxml")), (Stage)vBox.getScene().getWindow());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //has to be the first to initialize the repository
        recipeRepos = new MySqlRecipeRepository(new DatabaseManager());

        recipeList = recipeRepos.getAllRecipes();
        int column = 0;
        int row = 1;
        try {
            for (Recipe recipe : recipeList){
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/cookbook.view/RecipeItem.fxml"));
                VBox recipeBox = fxmlLoader.load();
                RecipeItemController controller = fxmlLoader.getController();
                controller.setRecipeData(recipe);
                if(column == 5){
                    column = 0;
                    ++row;
                }
                recipeContainer.add(recipeBox, column++, row);
                GridPane.setMargin(recipeBox, new Insets(10));
                    
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            

    }

}
