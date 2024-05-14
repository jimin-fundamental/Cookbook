package cookbook.Controller;

import java.net.URL;
import java.util.ResourceBundle;

import cookbook.DatabaseManager;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;
import cookbook.repository.MySqlRecipeRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class ChangeRecipeController implements Initializable{

    @FXML
    private TextArea descriptionArea;

    @FXML
    private TextField imageUrlField;

    @FXML
    private TextArea ingredientsArea;

    @FXML
    private TextField servingsField;

    @FXML
    private TextField shortDescriptionField;

    @FXML
    private TextArea tagsArea;

    @FXML
    private TextField titleField;
    
    private Recipe recipe;
    private MySqlRecipeRepository sqlRepos;
    private User user;

    public void setUser(User user){
        this.user = user;
    }

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
        imageUrlField.setText(recipe.getImagePath());
        servingsField.setText((String.valueOf(recipe.getNumberOfPersons())));
        shortDescriptionField.setText(recipe.getShortDescription());
        titleField.setText(recipe.getName());

        String description = "";
        String tags= "";
        String ingredients = "";

        for (String step : recipe.getProcessSteps()){
            description += step + "; ";
        }
        
        for (String tag : recipe.getTags()){
            tags += tag + ", ";
        }
        
        for (Ingredient ing : recipe.getIngredients()){
            ingredients += ing.getName() + ", " + ing.getAmount() + ", " + ing.getUnit() + "; ";
        }
        if (ingredients.length() >= 3){
            ingredients = ingredients.substring(0, ingredients.length() - 2);
        }
        if (tags.length() >= 3){
            tags = tags.substring(0, tags.length() - 2);
        }
        if (description.length() >= 3){
            description = description.substring(0, description.length() - 2);
        }
        
        descriptionArea.setText(description);
        ingredientsArea.setText(ingredients);
        tagsArea.setText(tags);
    }

    @FXML
    void changeRecipe(ActionEvent event) {
        sqlRepos.updateRecipe(recipe.getId(), titleField.getText(), shortDescriptionField.getText(), descriptionArea.getText(), imageUrlField.getText(), Integer.parseInt(servingsField.getText()), user.getId(), ingredientsArea.getText(), tagsArea.getText());
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.sqlRepos = new MySqlRecipeRepository(new DatabaseManager());
    }
}
