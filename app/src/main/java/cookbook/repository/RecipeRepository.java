package cookbook.repository;

import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;

import java.sql.Date;
import java.util.List;

public interface RecipeRepository {
    void addRecipeRepo(int userId, String name, String shortDescription, String description, String imageUrl, int servings, int author, String ingredients, String tags);
    Recipe getRecipeById(Long id);
    List<Recipe> getAllRecipes();
    void updateRecipe(Long id, String name, String shortDescription, String description, String imageUrl, int servings, int author, String ingredients, String tags);
    void deleteRecipe(Long id);

    List<Recipe> findByName(String name);
    List<Recipe> findByIngredient(String ingredient);
    List<Recipe> findByTag(String tag);

    void saveToFavorites(Recipe recipe, User user);
    void removeFromFavorites(Recipe recipe, User user);
    List<Recipe> getFavorites(List<Recipe> recipes, User user);

    void addToWeekPlan(Recipe recipe, User user, Date date);
    void removeFromWeekPlan(Recipe recipe, User user, Date date);
    List<Recipe> getRecipeWeeklyDates(List<Recipe> recipes, User user);

    List<Ingredient> fetchIngredients(Long id);
    List<String> fetchTags(Long id);
    List<String> fetchComments(Long id);
    List<String> parseProcessSteps(String json);

}

