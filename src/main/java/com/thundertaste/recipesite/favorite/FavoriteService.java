package com.thundertaste.recipesite.favorite;
import com.thundertaste.recipesite.recipe.Recipe;
import com.thundertaste.recipesite.recipe.RecipeRepository;
import com.thundertaste.recipesite.user.User;
import com.thundertaste.recipesite.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    public boolean toggleFavorite(Long recipeId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Favorite favorite = favoriteRepository.findByUserUserIDAndRecipeId(user.getUserID(), recipeId)
                .orElse(null);

        if (favorite == null) {
            // Add to favorites
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setRecipe(recipeRepository.findById(recipeId).orElseThrow(()->
                    new IllegalArgumentException("Recipe not found with id: " + recipeId)));
            favoriteRepository.save(newFavorite);
            return true;
        } else {
            // Remove from favorites
            favoriteRepository.delete(favorite);
            return false;
        }
    }


    public Set<Long> getFavoriteRecipeIdsByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return favoriteRepository.findByUser(user)
                .stream()
                .map(Favorite::getRecipe)
                .map(Recipe::getId)
                .collect(Collectors.toSet());
    }

}
