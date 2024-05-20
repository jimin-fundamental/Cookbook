package cookbook.repository;

import cookbook.model.Message;
import cookbook.model.Recipe;

public class MessageService {
    private MySqlRecipeRepository sqlrepos;

    public MessageService(MySqlRecipeRepository recipeRepository) {
        this.sqlrepos = recipeRepository;
    }

    public Recipe fetchRecipeForMessage(Message message) {
        System.out.println("Fetching recipe for message " + message);
        return sqlrepos.fetchRecipeById(message.getRecipeId());
    }
}
