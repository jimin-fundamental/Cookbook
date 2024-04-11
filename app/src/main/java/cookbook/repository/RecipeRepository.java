package cookbook.repository;

import cookbook.model.Recipe;
import java.util.List;

public interface RecipeRepository {
    void addRecipe(Recipe recipe);
    Recipe getRecipeById(Long id);
    List<Recipe> getAllRecipes();
    void updateRecipe(Recipe recipe);
    void deleteRecipe(Long id);
    // Other relevant methods
}

