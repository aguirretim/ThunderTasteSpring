package com.thundertaste.recipesite.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {

    // Fetch top 6 recipes, sorted by creation date (newest first)
    List<Recipe> findTop6ByOrderByIdDesc();

    List<Recipe> findByTitleContaining(String title);

    // Query method to find recipes by user ID
    List<Recipe> findByAuthorUserID(Long userId);

}
