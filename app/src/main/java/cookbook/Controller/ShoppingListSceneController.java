package cookbook.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import cookbook.DatabaseManager;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ShoppingListSceneController {

    @FXML
    private VBox listsVBox;

    @FXML
    private Text titleText;

    private List<Ingredient> ingredients;
    private User user;
    private List<ShoppingListItemController> controllers;
    private MySqlRecipeRepository recipeRepos;
    private int week;


    public void setUser(User user){
        this.user = user;
        recipeRepos = new MySqlRecipeRepository(new DatabaseManager(), user);
    }

    public void setIngredients(List<Ingredient> ingredientList){
        controllers = new ArrayList<>();
        this.ingredients = ingredientList;
        ingredients = recipeRepos.fetchShoppingList(ingredients, week, user);
        for (Ingredient ingredient : ingredients){
            displayShoppingItem(ingredient);
        }
    }

    public void setTitle(int week, String weekDuration){
        this.titleText.setText("Shopping list Week " + week + " (" + weekDuration + ")" );
        this.week = week;
    }

    @FXML
    void updatListPressed(ActionEvent event) {
        for(ShoppingListItemController controller : this.controllers){
            controller.updateIngredient();
        }
        recipeRepos.writeShoppingList(ingredients, week, user);
    }

    private void displayShoppingItem(Ingredient ingredient) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cookbook.view/ShoppingListItem.fxml"));
            HBox recipeBox = fxmlLoader.load();
            ShoppingListItemController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setIngredients(ingredient);
            controllers.add(controller);
            this.listsVBox.getChildren().add(recipeBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

