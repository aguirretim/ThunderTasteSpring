package com.thundertaste.recipesite.rating;


import com.thundertaste.recipesite.review.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public Rating save(Rating rating) {
        return ratingRepository.save(rating);
    }


    public List<Rating> findRatingsByRecipeId(Long recipeId) {
        return ratingRepository.findByRecipeID(recipeId);
    }
}


