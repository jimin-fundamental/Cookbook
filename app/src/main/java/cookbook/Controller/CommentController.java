package cookbook.Controller;

import cookbook.DatabaseManager;
import cookbook.repository.MySqlRecipeRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import cookbook.model.Comment;

//Manages editing and deleting of its associated comment.
public class CommentController {
    @FXML
    private Text usernameText;
    @FXML
    private Text commentText;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    private Comment comment;
    private RecipeSceneController parentController;

    private MySqlRecipeRepository sqlRepos = new MySqlRecipeRepository(new DatabaseManager());

    public void setComment(Comment comment) {
        this.comment = comment;
        usernameText.setText(comment.getUserName());
        commentText.setText(comment.getComment());
    }

    public void setParentController(RecipeSceneController controller) {
        this.parentController = controller;
    }

    public Button getEditButton() {
        return editButton;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }



//    @FXML
//    private void handleEditAction() {
//        // Trigger edit logic possibly using a dialog to get new text
//        String newCommentText = parentController.commentInputField.getText();
//        if (newCommentText != null && !newCommentText.equals(comment.getComment())) {
//            sqlRepos.editComment(comment.getCommentID(), newCommentText);
//            parentController.refreshComments();  // Notify the parent controller to refresh comments
//        }
//    }
//
//    @FXML
//    private void handleDeleteAction() {
//        sqlRepos.deleteComment(comment.getCommentID());
//        parentController.refreshComments();  // Notify the parent controller to refresh comments
//    }


}

