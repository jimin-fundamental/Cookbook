package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.sql.Date;

public class WeeklyViewItemController {

    
    @FXML
    private Text recipeName;

    @FXML
    private Text numberOfServings;

    @FXML
    private HBox hBox;

    private Recipe recipe;
    private User user;
    private LocalDate date;
    private MySqlRecipeRepository recipeRepos;
    private int servings;

    public void setServings(int servings){
        this.servings = servings;
        this.numberOfServings.setText(Integer.toString(servings));
    }

    public void setText(String recipeName){
        this.recipeName.setText(recipeName);
    }

    public void setData(User user, LocalDate date, Recipe recipe){
        this.recipe = recipe;
        this.user = user;
        this.date = date;
        this.recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);

    }

    @FXML
    void removeWeeklyClicked(ActionEvent event) {
        removeWeekly();
    }

    @FXML
    void recipeClicked(MouseEvent event) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/cookbook.view/RecipeScene.fxml"));

        // create new stage for new window of the recipe
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(new Scene(fxmlLoader.load()));

        // get the controller to call the method to set the data
        RecipeSceneController controller = fxmlLoader.getController();
        controller.setRecipeData(this.recipe, servings);
        controller.setUser(user);
        stage.show();

    }

    public void removeWeekly(){
        // remove the element from the list
        this.recipe.getWeeklyDates().remove(Date.valueOf(this.date));
        
        this.recipeRepos.removeFromWeekPlan(recipe, user, Date.valueOf(date));

        Parent parent = hBox.getParent();
        
        WeeklyListSceneController parentController = (WeeklyListSceneController)parent.getProperties().get("Controller");
        parentController.updateView();
    }
}
