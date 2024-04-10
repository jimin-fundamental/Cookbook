package cookbook.domain;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private Long id; // Unique identifier for the recipe
    private String name; // The name of the recipe
    private String shortDescription; // A short description of the recipe
    private List<String> ingredients; // A list of ingredients as strings
    private List<String> processSteps; // A detailed description of the process
    private int numberOfPersons; // The number of persons the recipe serves
    private List<String> tags; // Tags for the recipe
    private List<String> comments; // User comments on the recipe

    public Recipe() {
        // Initialize lists
        ingredients = new ArrayList<>();
        tags = new ArrayList<>();
        comments = new ArrayList<>();
        processSteps = new ArrayList<>();
    }

    // Constructor, getters, and setters
    public Recipe(Long id, String name, String shortDescription, List<String> ingredients, String process, int numberOfPersons, List<String> tags) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.ingredients = ingredients;
        this.process = process;
        this.numberOfPersons = numberOfPersons;
        this.tags = tags;
    }
    public Recipe(Long id, String name, String shortDescription, List<String> ingredients, String detailedDescription, int numberOfPersons, List<String> tags, List<String> comments) {
        this.id = id;
        this.name = name;
        this.shortDescription = shortDescription;
        this.ingredients = ingredients;
        this.process = process;
        this.numberOfPersons = numberOfPersons;
        this.tags = tags;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getProcessSteps() {
        return processSteps;
    }

    public void setProcessSteps(List<String> processSteps) {
        this.processSteps = processSteps;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }


}

