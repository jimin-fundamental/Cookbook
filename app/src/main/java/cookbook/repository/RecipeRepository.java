package cookbook.repository;

import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;

import java.util.List;

public interface RecipeRepository {
    void addRecipeRepo(int userId, String name, String shortDescription, String description, String imageUrl, int servings, String ingredients, String tags);
    Recipe getRecipeById(Long id);
    List<Recipe> getAllRecipes();
    void updateRecipe(Long id, String name, String shortDescription, String description, String imageUrl, int servings, String ingredients, String tags);
    void deleteRecipe(Long id);

    List<Recipe> findByName(String name);
    List<Recipe> findByIngredient(String ingredient);
    List<Recipe> findByTag(String tag);

    void saveToFavorites(Recipe recipe, User user);
    void removeFromFavorites(Recipe recipe, User user);
    List<Recipe> getFavorites(List<Recipe> recipes, User user);

    void addToWeekPlan(Recipe recipe, String week);
    void removeFromWeekPlan(Recipe recipe, String week);
    List<Recipe> getWeekPlan(String week);

    List<Ingredient> fetchIngredients(Long id);
    List<String> fetchTags(Long id);
    List<String> fetchComments(Long id);
    List<String> parseProcessSteps(String json);

}

