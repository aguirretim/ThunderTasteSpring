package com.thundertaste.recipesite.review;

import com.thundertaste.recipesite.user.User;
import com.thundertaste.recipesite.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    @Autowired
    public ReviewService(ReviewRepository reviewRepository,UserRepository userRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    // Other methods...

    public List<Review> findReviewsByRecipeId(Long recipeID) {
        return reviewRepository.findByRecipeID(recipeID);
    }

    public List<ReviewTransferObject> getReviewsWithAuthors(Long recipeId) {
        List<Review> reviews = reviewRepository.findByRecipeID(recipeId);
        List<ReviewTransferObject> reviewDTOs = new ArrayList<>();

        for (Review review : reviews) {
            User author = userRepository.findById(review.getUserID()).orElse(null);
            String username = author != null ? author.getUsername() : "Unknown";
            int score = review.getRating().getScore();
            ReviewTransferObject dto = new ReviewTransferObject(review.getText(), review.getDatePosted(), username,score);
            // ... set other properties like rating
            reviewDTOs.add(dto);
        }

        return reviewDTOs;
    }


}
