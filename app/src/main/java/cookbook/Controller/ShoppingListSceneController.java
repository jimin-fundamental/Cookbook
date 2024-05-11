package cookbook.Controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;
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


    public void setUser(User user){
        this.user = user;
    }

    public void setIngredients(List<Ingredient> ingredientList){
        this.ingredients = ingredientList;
        for (Ingredient ingredient : ingredients){
            displayShoppingItem(ingredient);
        }
    }

    public void setTitle(int week, String weekDuration){
        this.titleText.setText("Shopping list Week " + week + " (" + weekDuration + ")" );
    }

    private void displayShoppingItem(Ingredient ingredient) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/cookbook.view/ShoppingListItem.fxml"));
            HBox recipeBox = fxmlLoader.load();
            ShoppingListItemController controller = fxmlLoader.getController();
            controller.setUser(user);
            controller.setIngredients(ingredient);
            this.listsVBox.getChildren().add(recipeBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

