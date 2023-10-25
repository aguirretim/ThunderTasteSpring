package com.thundertaste.recipesite.recipe;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends CrudRepository<Recipe, Long> {
    @Query ("select r from Recipe r where r.title like %:searchString%")
    List<Recipe> searchByName(@Param("searchString")String keyword);



    // Fetch top 6 recipes, sorted by creation date (newest first)
    List<Recipe> findTop6ByOrderByIdDesc();



}
