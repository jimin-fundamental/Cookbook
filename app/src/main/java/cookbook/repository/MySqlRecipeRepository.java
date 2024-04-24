package cookbook.repository;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;

import java.util.List;

public class MySqlRecipeRepository implements RecipeRepository{

    private DatabaseManager dbManager;

    public MySqlRecipeRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public void addRecipe(Recipe recipe) {

    }

    @Override
    public Recipe getRecipeById(Long id) {
        return null;
            }

    @Override
    public List<Recipe> getAllRecipes() {
        return List.of();
    }

    @Override
    public void updateRecipe(Recipe recipe) {

    }

    @Override
    public void deleteRecipe(Long id) {

    }

    @Override
    public List<Recipe> findByName(String name) {
        return List.of();
    }

    @Override
    public List<Recipe> findByIngredient(String ingredient) {
        return List.of();
    }

    @Override
    public List<Recipe> findByTag(String tag) {
        return List.of();
    }

    @Override
    public void saveToFavorites(Recipe recipe) {

    }

    @Override
    public void removeFromFavorites(Recipe recipe) {

    }

    @Override
    public List<Recipe> getFavorites() {
        return List.of();
    }

    @Override
    public void addToWeekPlan(Recipe recipe, String week) {

    }

    @Override
    public void removeFromWeekPlan(Recipe recipe, String week) {

    }

    @Override
    public List<Recipe> getWeekPlan(String week) {
        return List.of();
    }
}
