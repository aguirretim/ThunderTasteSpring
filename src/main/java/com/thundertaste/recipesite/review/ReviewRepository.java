package com.thundertaste.recipesite.review;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review,Long> {

    List<Review> findByRecipeID(Long recipeID);

}
