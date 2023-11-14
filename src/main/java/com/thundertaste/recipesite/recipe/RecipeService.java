package com.thundertaste.recipesite.recipe;

import com.thundertaste.recipesite.favorite.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private FavoriteService favoriteService;

    public List<Recipe> getNewestRecipes() {
        return recipeRepository.findTop6ByOrderByIdDesc();
    }

    public Optional<Recipe> findById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe save(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public List<Recipe> searchRecipes(String query) {
        // search logic
        return recipeRepository.findByTitleContaining(query);
    }

    public List<Recipe> getAllRecipes() {
        return (List<Recipe>) recipeRepository.findAll();
    }

    // Method to find recipes by user ID
    public List<Recipe> findRecipesByUserId(Long userId) {
        // Logic to fetch recipes from the repository/database
        return recipeRepository.findByAuthorUserID(userId);
    }

    public boolean toggleFavorite(Long recipeId, String username) {
        // Logic to add or remove recipe from user's favorites
        // Return true if it's now a favorite, false otherwise
        return favoriteService.toggleFavorite(recipeId, username);
    }


    public Set<Long> getFavoritedRecipeIdsByUser(String username) {
        return favoriteService.getFavoriteRecipeIdsByUser(username);
    }


    public List<Recipe> getFavoritedRecipes(String username) {
        Set<Long> favoritedRecipeIds = favoriteService.getFavoriteRecipeIdsByUser(username);
        return (List<Recipe>) recipeRepository.findAllById(favoritedRecipeIds);
    }



}
