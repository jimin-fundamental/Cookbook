package cookbook.repository;

import cookbook.model.Recipe;
import java.util.List;

public interface RecipeRepository {
    void addRecipe(Recipe recipe);
    Recipe getRecipeById(Long id);
    List<Recipe> getAllRecipes();
    void updateRecipe(Recipe recipe);
    void deleteRecipe(Long id);

    List<Recipe> findByName(String name);
    List<Recipe> findByIngredient(String ingredient);
    List<Recipe> findByTag(String tag);

    void saveToFavorites(Recipe recipe);
    void removeFromFavorites(Recipe recipe);
    List<Recipe> getFavorites();

    void addToWeekPlan(Recipe recipe, String week);
    void removeFromWeekPlan(Recipe recipe, String week);
    List<Recipe> getWeekPlan(String week);

    List<String> fetchIngredients(Long id);
    List<String> fetchTags(Long id);
    List<String> fetchComments(Long id);
    List<String> parseProcessSteps(String json);

}

