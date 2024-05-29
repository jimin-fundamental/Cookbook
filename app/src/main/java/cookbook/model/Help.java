package cookbook.model;

import java.util.List;

public class Help {
    private Long id;
    private String title;
    private String description;
    private String text;
    private List<String> keyWords;

    public Help(Long id, String title, String description, String text) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getText() {
        return text;
    }

    public List<String> getkeyWords() {
        return keyWords;
    }

    public void setkeyWords(List<String> keyWords) {
        this.keyWords = keyWords;
    }
}