package cookbook.repository;

import cookbook.DatabaseManager;
import cookbook.model.Ingredient;
import cookbook.model.Recipe;
import cookbook.model.User;

import java.sql.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.mysql.cj.protocol.Resultset;

public class MySqlRecipeRepository implements RecipeRepository{

    private DatabaseManager dbManager;
    private UserDao userDao;
    private User currentUser; // Current user object

    public MySqlRecipeRepository(DatabaseManager dbManager) {
        this.dbManager = dbManager;
        this.currentUser = currentUser; // Initialize with current user
        this.userDao = new UserDao(dbManager);

        //loadPredeterminedTags();  // Pre-load tags to optimize later checks
    }
    public MySqlRecipeRepository(DatabaseManager dbManager, User currentUser) {
        this.dbManager = dbManager;
        this.currentUser = currentUser; // Initialize with current user
        this.userDao = new UserDao(dbManager);

    }


    private List<String> cachedPredeterminedTags;

    //Cache Predetermined Tags
//    private void loadPredeterminedTags() {
//        cachedPredeterminedTags = getAllPredeterminedTags();
//    }


    @Override
    public void addRecipeRepo(int userID, String name, String shortDescription, String description, String imageUrl, int servings, String ingredients, String tags) {
        try (Connection connection = DriverManager.getConnection(dbManager.url)) {
            // Prepare the SQL statement
            String sql = "CALL AddNewRecipe(?, ?, ?, ?, ?, ?, ?, ?)";
            try (CallableStatement statement = connection.prepareCall(sql)) {
                // Set the parameters for the stored procedure
                statement.setInt(1, userID); // Pass the userID to the stored procedure
                statement.setString(2, name);
                statement.setString(3, shortDescription);
                statement.setString(4, description);
                statement.setString(5, imageUrl);
                statement.setInt(6, servings);
                statement.setString(7, ingredients); // Directly pass the ingredients string
                statement.setString(8, tags); // Directly pass the tags string

                System.out.println("statement: " + statement);
                // Execute the stored procedure
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    //String tags = 'starter;fruit; <- this form'
    //1. split tags spring divided by ';' and make List<String>
    //2. add each of a tag to Tags table - ID is auto generated, ispredifined = 0, tagname is what is entered
    //3. get each of tagID from Tags table and start fill out RecipeCustomTag table - fill out Tags_ID with that
    //4. get ID from User table(which is UserId)
    //5. get ID from Recipe table(which is recipeId)
    public void addCustomTagsRepo(String tags, Long userId, Recipe recipe) {
        if (tags == null || tags.isEmpty()) {
            System.out.println("No tags provided.");
            return;
        }

        // Split tags string divided by ';' and make List<String>
        List<String> tagList = Arrays.stream(tags.split(";"))
                .filter(tag -> !tag.isEmpty())
                .collect(Collectors.toList());

        if (tagList.isEmpty()) {
            System.out.println("No valid tags to process.");
            return;
        }

        String insertTagSql = "INSERT INTO Tags (tagname, ispredefined) VALUES (?, 0) ON DUPLICATE KEY UPDATE ID=LAST_INSERT_ID(ID)";
        String fetchTagIdSql = "SELECT ID FROM Tags WHERE tagname = ?";
        String insertRecipeTagSql = "INSERT INTO RecipeCustomTag (Recipe_ID, User_ID, Tags_ID) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement insertTagStmt = connection.prepareStatement(insertTagSql, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement fetchTagIdStmt = connection.prepareStatement(fetchTagIdSql);
             PreparedStatement insertRecipeTagStmt = connection.prepareStatement(insertRecipeTagSql)) {

            for (String tag : tagList) {
                // Insert the tag or update it to get the last insert ID
                insertTagStmt.setString(1, tag);
                insertTagStmt.executeUpdate();
                ResultSet tagIds = insertTagStmt.getGeneratedKeys();

                long tagId;
                if (tagIds.next()) {
                    tagId = tagIds.getLong(1);  // Get the generated tag ID
                } else {
                    // Fetch existing tag ID
                    fetchTagIdStmt.setString(1, tag);
                    ResultSet rs = fetchTagIdStmt.executeQuery();
                    rs.next();
                    tagId = rs.getLong("ID");
                }

                // Associate tag with user and recipe in RecipeCustomTag table
                Long recipeId = recipe.getId();

                insertRecipeTagStmt.setLong(1, recipeId);
                insertRecipeTagStmt.setLong(2, userId);
                insertRecipeTagStmt.setLong(3, tagId);
                insertRecipeTagStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("SQL Error: " + e.getMessage());
        }
    }





    public void fetchRecipeDetails(Recipe rec) {


        // Fetch comments for the recipe
        // List<String> comments = fetchComments(rec.getId());
        // rec.setComments(comments);
    }

    @Override
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        //String sql = "SELECT Recipe_ID, Recipe_Name, Short_Description, Description, Ingredients_JSON, Tags_JSON, Servings FROM FullRecipeView";
        String sql = "SELECT Recipe_ID, Recipe_Name, Short_Description, Description, Ingredients_JSON, Tags_JSON, Servings, Image_URL FROM FullRecipeView";


        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Recipe recipe = new Recipe();
                recipe.setId(rs.getLong("Recipe_ID"));
                recipe.setName(rs.getString("Recipe_Name"));
                recipe.setShortDescription(rs.getString("Short_Description"));
                recipe.setNumberOfPersons(rs.getInt("Servings"));
                recipe.setImagePath(rs.getString("Image_URL"));


                // Extract and parse process steps from JSON
                String jsonProcessSteps = rs.getString("Description");
                List<String> processSteps = parseProcessSteps(jsonProcessSteps);
                recipe.setProcessSteps(processSteps);

                // Fetch tags for the recipe
                String jsonTags = rs.getString("Tags_JSON");
                List<String> tags = parseTags(jsonTags);
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
    public List<Recipe> getFavorites(List<Recipe> recipes, User user){
        // add Information for favourite recipes
        List<Long> favourites = fetchFavourites(user);
        for (Recipe recipe : recipes){
            if (favourites.contains(recipe.getId())){
                recipe.setIsFavourite(true);
            }
            else{
                recipe.setIsFavourite(false);
            }
        }
        return recipes;
    }

    @Override
    public void updateRecipe(Long id, String name, String shortDescription, String description, String imageUrl, int servings, String ingredientsText, String tagsText) {
        try (Connection connection = DriverManager.getConnection(dbManager.url)) {
//            String sql = "SELECT Recipe_ID FROM UserRecipe " +
//                    "WHERE User_ID = ? AND Recipe_ID IS NOT NULL AND isstar = true";
            String sql = "CALL UpdateRecipe(?, ?, ?, ?, ?, ?, ?, ?)";
            try (CallableStatement statement = connection.prepareCall(sql)) {
                statement.setLong(1, id);
                statement.setString(2, name);
                statement.setString(3, shortDescription);
                statement.setString(4, description);
                statement.setString(5, imageUrl);
                statement.setInt(6, servings);
                statement.setString(7, ingredientsText);
                statement.setString(8, tagsText);

                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
    public void saveToFavorites(Recipe recipe, User user) {
        String sql = "INSERT INTO UserRecipe (User_ID, Recipe_ID, isstar)" +
            	     "VALUES (?, ?, true)" +
                     "ON DUPLICATE KEY UPDATE isstar = true";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // set values
            pstmt.setLong(1, user.getId());
            pstmt.setLong(2, recipe.getId());

            pstmt.executeUpdate();

            pstmt.close();
            connection.close();
            recipe.setIsFavourite(true);


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("problem while adding favourite");

        }
    }

    @Override
    public void removeFromFavorites(Recipe recipe, User user) {
        String sql = "UPDATE UserRecipe " +
            	     "SET isstar = false " +
                     "WHERE User_ID = ? AND Recipe_ID = ?";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // set values
            pstmt.setLong(1, user.getId());
            pstmt.setLong(2, recipe.getId());

            pstmt.executeUpdate();

            pstmt.close();
            connection.close();
            recipe.setIsFavourite(false);


        } catch (SQLException e) {
            e.printStackTrace();
        }
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

        String sql = "SELECT comment FROM Comments " +
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

    public List<Long> fetchFavourites(User user) {
        List<Long> favourites = new ArrayList<>();

        String sql = "SELECT Recipe_ID FROM UserRecipe " +
                     "WHERE User_ID = ? AND Recipe_ID IS NOT NULL AND isstar = true";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            // Set the recipe ID parameter
            pstmt.setLong(1, user.getId());

            // Execute the query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Long ingredientName = rs.getLong("Recipe_ID");
                    favourites.add(ingredientName);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return favourites;
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
            String[] stepsArray = steps.split("\", \"");
            for (String step : stepsArray) {
                processSteps.add(step.trim().replaceAll("\"", ""));
            }
        }

        return processSteps;
    }

    public List<String> parseTags(String json) {
        List<String> processSteps = new ArrayList<>();

        // Use regex to extract process steps from JSON string
        Pattern pattern = Pattern.compile("\\{\\s*\"tagname\"\\s*:\\s*\"([^\"]+)\"\\s*\\}");
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

        List<String> jsonElementsList = new ArrayList<>();

        // Use regex to match each JSON element in the array
        Pattern pat = Pattern.compile("\\{[^\\}]+\\}");
        Matcher mat = pat.matcher(json);

        while (mat.find()) {
            String jsonElement = mat.group();
            jsonElementsList.add(jsonElement);
        }
        for (String element : jsonElementsList){

            // Use regex to extract process steps from JSON string
            Pattern pattern = Pattern.compile("\\{\\s*\"name\"\\s*:\\s*\"([^\"]+)\",\\s*\"amount\"\\s*:\\s*\"([^\"]+)\",\\s*\"unit\"\\s*:\\s*\"([^\"]+)\"\\s*\\}");
            Matcher matcher = pattern.matcher(element);

            if (matcher.find()) {
                String name = matcher.group(1).replaceAll("\"", "").trim();
                String amount = matcher.group(2).replaceAll("\"", "").trim();
                String unit = matcher.group(3).replaceAll("\"", "").trim();

                Ingredient ingredient = new Ingredient();
                ingredient.setName(name);
                ingredient.setAmount(Integer.parseInt(amount));
                ingredient.setUnit(unit);
                ingredients.add(ingredient);
            }
        }

        return ingredients;
    }

    //method for getting whole predeterminedTags
    public List<String> getAllPredeterminedTags() {
        List<String> tags = new ArrayList<>();

        String sql = "SELECT ID, tagname FROM Tags WHERE ispredefined = 1";

        try (Connection connection = DriverManager.getConnection(dbManager.url);
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String tag = rs.getString("tagname");
                tags.add(tag);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tags;
    }



//    private boolean tagExists(String tag) {
//        // Check in cached predetermined tags first
//        if (cachedPredeterminedTags.contains(tag)) {
//            return true;
//        }
//
//        // Check in the database if not found in the cache (for non-predetermined tags)
//        String sql = "SELECT COUNT(*) FROM Tags WHERE tagname = ?";
//        try (Connection connection = DriverManager.getConnection(dbManager.url);
//             PreparedStatement pstmt = connection.prepareStatement(sql)) {
//            pstmt.setString(1, tag);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt(1) > 0;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public void ensureTagsExist(List<String> tags) {
//        for (String tag : tags) {
//            if (!tagExists(tag)) {
//                addTag(tag);
//            }
//        }
//    }



}
