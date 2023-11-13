package com.thundertaste.recipesite.favorite;

import com.thundertaste.recipesite.core.BaseEntity;
import com.thundertaste.recipesite.recipe.Recipe;
import com.thundertaste.recipesite.user.User;
import jakarta.persistence.*;

@Entity
public class Favorite extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;


    // Constructors, getters, setters...
    public Favorite() {
        // Default constructor
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

}
