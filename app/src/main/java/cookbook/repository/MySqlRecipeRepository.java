package cookbook.repository;

import cookbook.DatabaseManager;
import cookbook.model.Recipe;
import java.sql.*;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mysql.cj.protocol.Resultset;

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
        Recipe recipe = new Recipe();

        // Fetch ingredients for the recipe
        List<String> ingredients = fetchIngredients(id);
        recipe.setIngredients(ingredients);

        // Fetch tags for the recipe
        List<String> tags = fetchTags(id);
        recipe.setTags(tags);

        // Fetch comments for the recipe
        List<String> comments = fetchComments(id);
        recipe.setComments(comments);        
        return recipe;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        String sql = "SELECT ID, name, sdecr, descr FROM Recipe";
        
        try (Connection connection = DriverManager.getConnection(dbManager.url);
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Recipe recipe = new Recipe();
                recipe.setId(rs.getLong("ID"));
                recipe.setName(rs.getString("name"));
                recipe.setShortDescription(rs.getString("sdecr"));

                // Extract and parse process steps from JSON
                String jsonProcessSteps = rs.getString("descr");
                List<String> processSteps = parseProcessSteps(jsonProcessSteps);
                recipe.setProcessSteps(processSteps);

                recipes.add(recipe);
            }
            
            
            pstmt.close();
            connection.close();
            

            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipes;
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

    @Override
    public List<String> fetchIngredients(Long id) {
        List<String> ingredients = new ArrayList<>();

        String sql = "SELECT name FROM Ingredient " +
                     "INNER JOIN RecipeIngredient ON Ingredient.ID = RecipeIngredient.Ingredient_ID " +
                     "WHERE RecipeIngredient.Recipe_ID = ?";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set the recipe ID parameter
            pstmt.setLong(1, id);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String ingredientName = rs.getString("name");
                    ingredients.add(ingredientName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredients;
    }

    @Override
    public List<String> fetchTags(Long id) {
        List<String> tags = new ArrayList<>();

        String sql = "SELECT t.tagname FROM Tags t " +
                     "INNER JOIN RecipeTag rt ON t.ID = rt.Tags_ID " +
                     "WHERE rt.Recipe_ID = ?";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set the recipe ID parameter
            pstmt.setLong(1, id);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String ingredientName = rs.getString("tagname");
                    tags.add(ingredientName);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }

    @Override
    public List<String> fetchComments(Long id) {
        List<String> comments = new ArrayList<>();

        String sql = "SELECT comment FROM UserRecipe " +
                     "WHERE Recipe_ID = ? AND comment IS NOT NULL";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set the recipe ID parameter
            pstmt.setLong(1, id);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String ingredientName = rs.getString("name");
                    comments.add(ingredientName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comments;
    }

    @Override
    public List<String> parseProcessSteps(String json) {
        List<String> processSteps = new ArrayList<>();

        // Use regex to extract process steps from JSON string
        Pattern pattern = Pattern.compile("\"steps\"\\s*:\\s*\\[([^\\]]*)\\]");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String steps = matcher.group(1);
            // Split the steps string by comma and trim each step
            String[] stepsArray = steps.split(",");
            for (String step : stepsArray) {
                processSteps.add(step.trim().replaceAll("\"", ""));
            }
        }

        return processSteps;
    }

}
