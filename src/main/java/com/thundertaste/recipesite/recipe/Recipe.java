package com.thundertaste.recipesite.recipe;


import com.thundertaste.recipesite.category.Category;
import com.thundertaste.recipesite.core.BaseEntity;
import com.thundertaste.recipesite.user.User;
import jakarta.persistence.*;


import java.util.Date;

@Entity
@Table(name = "recipe")
public class Recipe extends BaseEntity {


    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(nullable = false)
    private String ingredients;

    @Column(nullable = false)
    private String steps;

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

    protected Recipe(){
        super();
    }

    public Recipe(String title, String description, String ingredients, String steps, String image, Category category, String prepTime, String cookTime, int servingSize, User author, Date datePosted) {
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
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

}
