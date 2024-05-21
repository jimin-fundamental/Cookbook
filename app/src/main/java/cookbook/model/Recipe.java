    package cookbook.model;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;

    import java.sql.Date;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;

    public class Recipe {
        private Long id; // Unique identifier for the recipe
        private String name; // The name of the recipe
        private String shortDescription; // A short description of the recipe
        private List<Ingredient> ingredients; // A list of ingredients as strings
        private List<String> processSteps; // A detailed description of the process
        private int numberOfPersons; // The number of persons the recipe serves
        private int author;
        private int averageRating;
    //    private List<String> tags; // Tags for the recipe
        //only for predefined
        private ObservableList<String> tags = FXCollections.observableArrayList();
        private ObservableList<String> customTags = FXCollections.observableArrayList();
        private List<Comment> comments; // User comments on the recipe
        private String imagePath; // URL or path to the recipe's image
        private boolean isFavourite; // true if the recipe is selected as a favourite by the user
        private Map<Date, Integer> weeklyDates; // null if it is not in a weekly list
        // Default constructor
        public Recipe() {
            // Initialize lists
            ingredients = new ArrayList<>();
            comments = new ArrayList<>();
            processSteps = new ArrayList<>();
        }

        // Constructor, getters, and setters
        public Recipe(Long id, String name, String shortDescription, List<Ingredient> ingredients, List<String> processSteps, int numberOfPersons, int author, List<String> tags) {
            this.id = id;
            this.name = name;
            this.shortDescription = shortDescription;
            this.ingredients = ingredients;
            this.processSteps = processSteps;
            this.numberOfPersons = numberOfPersons;
            this.author = author;
    //        this.tags = tags;
        }
        public Recipe(Long id, String name, String shortDescription, List<Ingredient> ingredients, List<String> processSteps, int numberOfPersons, int author, List<String> tags, List<Comment> comments) {
            this.id = id;
            this.name = name;
            this.shortDescription = shortDescription;
            this.ingredients = ingredients;
            this.processSteps = processSteps;
            this.numberOfPersons = numberOfPersons;
            this.author = author;
    //        this.tags = tags;
            this.comments = comments;
        }
        // Constructor with all parameters including image path
        public Recipe(Long id, String name, String shortDescription, List<Ingredient> ingredients,
            List<String> processSteps, int numberOfPersons, int author, List<String> tags,
            List<Comment> comments, String imagePath, boolean isFavourite,
            Map<Date, Integer> weeklyDates, Date comparableDate) {
            this.id = id;
            this.name = name;
            this.shortDescription = shortDescription;
            this.ingredients = ingredients;
            this.processSteps = processSteps;
            this.numberOfPersons = numberOfPersons;
            this.author = author;
    //        this.tags = tags;
            this.comments = comments;
            this.imagePath = imagePath;
            this.isFavourite = isFavourite;
            this.weeklyDates = weeklyDates;
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

        public List<Ingredient> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<Ingredient> ingredients) {
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

        public int getAuthor() {
            return author;
        }

        public ObservableList<String> getTags() {
            return tags;
        }

        public ObservableList<String> getCustomTags() {
            return customTags;
        }

        public void setTags(List<String> newTags) {
            if (newTags == null) {
                throw new IllegalArgumentException("Tags cannot be null");
            }
            this.tags.setAll(newTags);
        }

        public void setCustomTags(List<String> newTags) {
            if (newTags == null) {
                throw new IllegalArgumentException("Tags cannot be null");
            }
            this.customTags.setAll(newTags);
        }


        public List<Comment> getComments() {
            return comments;
        }

        //user_ID, Comment_ID, Comment
        public void setComments(List<Comment> comments) {
            if (comments == null) {
                this.comments = new ArrayList<>(); // Treat null as an empty list
            } else {
                this.comments = new ArrayList<>(comments); // Make a copy of the comments list
            }
        }

        public String getImagePath() {
            return imagePath;
        }

        public void setImagePath(String imagePath) {
            this.imagePath = imagePath;
        }

        public void setIsFavourite(boolean isFavourite){
            this.isFavourite = isFavourite;
        }
        public boolean getIsFavourite(){
            return isFavourite;
        }
        public void setWeeklyDates(Map<Date, Integer> weeklyDates){
            this.weeklyDates = weeklyDates;
        }
        public Map<Date, Integer> getWeeklyDates(){
            return weeklyDates;
        }

        public int getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(int averageRating) {
            this.averageRating = averageRating;
        }
    }

