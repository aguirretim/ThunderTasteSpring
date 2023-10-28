package com.thundertaste.recipesite.recipe;


import com.thundertaste.recipesite.category.Category;
import com.thundertaste.recipesite.core.BaseEntity;
import com.thundertaste.recipesite.user.User;
import com.thundertaste.recipesite.user.UserTransferObject;
import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.logging.Logger;

@Entity
@Table(name = "recipe")
public class Recipe extends BaseEntity {


    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    @ElementCollection
    private List<String> ingredients;

    @Column(nullable = false)
    private List<String> steps = new ArrayList<>();

    @Column
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryID")
    private Category category;

    @Column(nullable = false)
    private String prepTime;

    @Column(nullable = false)
    private String cookTime;

    @Column(nullable = false)
    private int servingSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private User author;

    @Column(nullable = false)
    private Date datePosted;

    protected Recipe() {
        super();
    }


    public Recipe(String title, String description, List<String> ingredients, List<String> steps, String image, Category category, String prepTime, String cookTime, int servingSize, User author, Date datePosted) {
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
        this.image = image;
        this.category = category;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.servingSize = servingSize;
        this.author = author;
        this.datePosted = datePosted;
    }

    // Getters, setters, and other methods...

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }


    // getters and setters for steps
    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    private static final Logger LOGGER = Logger.getLogger(Recipe.class.getName());

    public int getTotalTimeInMinutes() {

        int total = 0;
        total += convertTimeToMinutes(this.prepTime);
        total += convertTimeToMinutes(this.cookTime);
        return total;
    }

    private int convertTimeToMinutes(String timeString) {

        LOGGER.info("Converting time string to minutes: " + timeString);

        // Implement logic to convert "1 hr 15 mins" to 75, etc.
        // This is just a stub and needs actual implementation
        int minutes = 0;

        // Regular expression to find hours and minutes in the string.
        // \d+ matches one or more digits. (?:...)? is a non-capturing group for zero or one occurrence.
        Pattern pattern = Pattern.compile("(?:(\\d+)\\s*(?:hours?|hrs?))?\\s*(?:(\\d+)\\s*(?:minutes?|mins?))?", Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(timeString);
        if (matcher.find()) {
            // Extract hours and minutes from the matcher groups.
            // Group 1 is hours; group 2 is minutes.
            String hoursStr = matcher.group(1);
            String minutesStr = matcher.group(2);

            int hours = (hoursStr != null) ? Integer.parseInt(hoursStr) : 0;
            int mins = (minutesStr != null) ? Integer.parseInt(minutesStr) : 0;

            // Total minutes = hours converted to minutes + minutes.
            minutes = hours * 60 + mins;
        }
        LOGGER.info("Total minutes: " + minutes);
        return minutes;
    }

}
