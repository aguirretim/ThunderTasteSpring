package com.thundertaste.recipesite.favorite;

import com.thundertaste.recipesite.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    Optional<Favorite> findByUserUserIDAndRecipeId(Long userID, Long recipeId);

    // Find all favorites by user
    List<Favorite> findByUser(User user);
}
