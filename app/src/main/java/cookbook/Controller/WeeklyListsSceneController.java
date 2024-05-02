package cookbook.Controller;

import cookbook.model.Recipe;
import javafx.fxml.FXML;

public class WeeklyListsSceneController {
    private Recipe recipe;

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @FXML
    void addWeeklyList() {
    }

}
