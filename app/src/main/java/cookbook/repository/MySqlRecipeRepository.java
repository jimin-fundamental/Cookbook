package cookbook.repository;

import cookbook.DatabaseManager;
import cookbook.model.Ingredient;
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
        List<Ingredient> ingredients = fetchIngredients(id);
        recipe.setIngredients(ingredients);

        // Fetch tags for the recipe
        List<String> tags = fetchTags(id);
        recipe.setTags(tags);

        // Fetch comments for the recipe
        List<String> comments = fetchComments(id);
        recipe.setComments(comments);        
        return recipe;
    }

    public void fetchRecipeDetails(Recipe rec) {

        // Fetch comments for the recipe
        List<String> comments = fetchComments(rec.getId());
        rec.setComments(comments);        
    }

    @Override
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        String sql = "SELECT Recipe_ID, Recipe_Name, Short_Description, Description, Ingredients_JSON, Tags_JSON, Servings FROM FullRecipeView";
        
        try (Connection connection = DriverManager.getConnection(dbManager.url);
        PreparedStatement pstmt = connection.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Recipe recipe = new Recipe();
                recipe.setId(rs.getLong("Recipe_ID"));
                recipe.setName(rs.getString("Recipe_Name"));
                recipe.setShortDescription(rs.getString("Short_Description"));
                recipe.setNumberOfPersons(rs.getInt("Servings"));


                // Extract and parse process steps from JSON
                String jsonProcessSteps = rs.getString("Description");
                List<String> processSteps = parseProcessSteps(jsonProcessSteps);
                recipe.setProcessSteps(processSteps);

                // Fetch tags for the recipe
                String jsonTags = rs.getString("Tags_JSON");
                List<String> tags = parseProcessSteps(jsonTags);
                recipe.setTags(tags);

                        // Fetch ingredients for the recipe
                String jsonIngredients = rs.getString("Ingredients_JSON");
                List<Ingredient> ingredients = parseIngredients(jsonIngredients);
                recipe.setIngredients(ingredients);

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
    public List<Ingredient> fetchIngredients(Long id) {
        List<Ingredient> ingredients = new ArrayList<>();

        String sql = "SELECT i.name, ri.amount_int, ri.amount_unit FROM Ingredient i " +
                     "INNER JOIN RecipeIngredient ri ON i.ID = ri.Ingredient_ID " +
                     "WHERE ri.Recipe_ID = ?";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set the recipe ID parameter
            pstmt.setLong(1, id);

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setName(rs.getString("name"));
                    ingredient.setAmount(rs.getInt("amount_int"));
                    ingredient.setUnit(rs.getString("amount_unit"));
                    ingredients.add(ingredient);
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

    public List<String> parseTags(String json) {
        List<String> processSteps = new ArrayList<>();

        // Use regex to extract process steps from JSON string
        Pattern pattern = Pattern.compile("\"tagname\"\\s*:\\s*\\[([^\\]]*)\\]");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String tag = matcher.group(1);
            // Split the steps string by comma and trim each step
            String[] tags = tag.split(",");
            for (String t : tags) {
                processSteps.add(t.trim().replaceAll("\"", ""));
            }
        }

        return processSteps;
    }

    public List<Ingredient> parseIngredients(String json) {
        List<Ingredient> ingredients = new ArrayList<>();

        // Use regex to extract process steps from JSON string
        Pattern pattern = Pattern.compile("\\{\"name\":\\s*\"(.*?)\",\\s*\"amount\":\\s*\"(.*?)\",\\s*\"unit\":\\s*\"(.*?)\"\\}");
        Matcher matcher = pattern.matcher(json);

        if (matcher.find()) {
            String name = matcher.group(1);
            String amount = matcher.group(2);
            String unit = matcher.group(3);
            // Split the steps string by comma and trim each step
            String[] names = name.split(",");
            String[] amounts = amount.split(",");
            String[] units = unit.split(",");
            for (int i = 0; i < names.length; i++) {
                Ingredient ingredient = new Ingredient();
                ingredient.setName(names[i].trim().replaceAll("\"", ""));
                ingredient.setAmount(Integer.parseInt(amounts[i].trim().replaceAll("\"", "")));
                ingredient.setUnit(units[i].trim().replaceAll("\"", ""));
                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }
}
