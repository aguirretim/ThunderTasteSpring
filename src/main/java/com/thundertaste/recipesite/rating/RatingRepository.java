package com.thundertaste.recipesite.rating;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RatingRepository extends CrudRepository<Rating, Long> {

    //Method to find ratings by recipe ID
    List<Rating> findByRecipeID(Long recipeID);

}
