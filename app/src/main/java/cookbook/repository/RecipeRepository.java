package cookbook.repository;

import cookbook.model.Comment;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface RecipeRepository {
    void addRecipeRepo(Long userId, String name, String shortDescription, String description, String imageUrl, int servings, Long author, String ingredients, String tags);
    void getAllRecipes(List<Recipe> recipes, boolean useThread);
    void updateRecipe(Long id, String name, String shortDescription, String description, String imageUrl, int servings, Long author, String ingredients, String tags);
    void deleteRecipe(Long id);

    List<Recipe> findByName(String name);
    List<Recipe> findByIngredient(String ingredient);
    List<Recipe> findByTag(String tag);

    void saveToFavorites(Recipe recipe, User user);
    void removeFromFavorites(Recipe recipe, User user);
    void getFavorites(List<Recipe> recipes, User user);

    void addToWeekPlan(Recipe recipe, User user, Date date, int servings, Text text);
    void removeFromWeekPlan(Recipe recipe, User user, Date date);
    void getRecipeWeeklyDates(List<Recipe> recipes, User user);

    List<Ingredient> fetchIngredients(Long id);
    List<String> fetchTags(Long id);
    List<Comment> fetchComments(Long recipeId);
    List<String> parseProcessSteps(String json);

}

