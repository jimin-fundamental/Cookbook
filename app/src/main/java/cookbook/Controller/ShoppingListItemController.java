package cookbook.Controller;

import java.util.List;

import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class ShoppingListItemController {

    @FXML
    private Text ingredientAmount;

    @FXML
    private Text ingredientName;

    @FXML
    private Text ingredientUnit;

    private List<Ingredient> ingredients;
    private User user;


    public void setUser(User user){
        this.user = user;
    }

    public void setIngredients(Ingredient ingredient){
        ingredientName.setText(ingredient.getName());
        ingredientAmount.setText(Integer.toString(ingredient.getAmount()));
        ingredientUnit.setText(ingredient.getUnit());
    }
    @FXML
    void ingredientClicked(MouseEvent event) {
        
    }

}