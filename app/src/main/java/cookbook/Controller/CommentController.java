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
    private Text editButton;
    @FXML
    private Text deleteButton;

    private Comment comment;

    public void setComment(Comment comment) {
        this.comment = comment;
        usernameText.setText(comment.getUserName());
        commentText.setText(comment.getComment());
    }

    public Text getEditButton() {
        return editButton;
    }

    public Text getDeleteButton() {
        return deleteButton;
    }


}

