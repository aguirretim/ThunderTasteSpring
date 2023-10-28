package com.thundertaste.recipesite.core;

import com.thundertaste.recipesite.recipe.Recipe;
import com.thundertaste.recipesite.recipe.RecipeRepository;
import com.thundertaste.recipesite.user.User;
import com.thundertaste.recipesite.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// @Component annotation marks this class as a Spring component,
// meaning Spring will automatically detect and create an instance of this class during application startup.
@Component
public class DatabaseLoader implements ApplicationRunner {

    // Repository for User entities
    private final UserRepository users;

    // Repository for Recipe entities
    private final RecipeRepository recipes;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // The @Autowired annotation tells Spring to automatically inject the required repositories
    // (UserRepository and RecipeRepository) into this DatabaseLoader class when it's instantiated.
    @Autowired
    public DatabaseLoader(UserRepository users, RecipeRepository recipes) {
        this.users = users;
        this.recipes = recipes;
    }

    // The run method is a callback method that's executed after the application context is loaded.
    // It's defined by the ApplicationRunner interface.
    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Creating a list of sample User entities.
        List<User> sampleUsers = Arrays.asList(
                new User("johndoe",
                        "johndoe@example.com",
                        passwordEncoder.encode("passwordHash1"),
                        "profileImage1",
                        new Date(), "Bio for John Doe"),
                new User("janedoe",
                        "janedoe@example.com",
                        passwordEncoder.encode("passwordHash2"),
                        "profileImage2",
                        new Date(),
                        "Bio for Jane Doe")
                // Add more sample users as needed
        );

        // Using the UserRepository, we save all the sample users into the database.
        users.saveAll(sampleUsers);

        // Creating a list to hold sample Recipe entities.
        List<Recipe> sampleRecipes = new ArrayList<>();

// Loop to create 10 sample Recipe entities.
        for (int i = 0; i < 10; i++) {
            List<String> ingredients = Arrays.asList(
                    "Ingredient 1 for recipe " + i,
                    "Ingredient 2 for recipe " + i,
                    "meat" + i,
                    "veggie" + i
                    // Add more ingredients as needed
            );

            List<String> steps = Arrays.asList(
                    "Step 1 for recipe " + i,
                    "Step 2 for recipe " + i,
                    "Step 3 for recipe " + i
                    // Add more steps as needed
            );

            Recipe recipe = new Recipe(
                    "Recipe " + i,
                    "Description for recipe " + i,
                    ingredients,
                    steps,  // Passing the list of steps
                    "image" + i,
                    null,  // Assuming category can be null for simplicity
                    "30 minutes",
                    "1 hour",
                    4,
                    sampleUsers.get(i % sampleUsers.size()),  // Cyclically assign a user as the author
                    new Date()
            );
            sampleRecipes.add(recipe);
        }

        // Using the RecipeRepository, we save all the sample recipes into the database.
        recipes.saveAll(sampleRecipes);

    }
}
