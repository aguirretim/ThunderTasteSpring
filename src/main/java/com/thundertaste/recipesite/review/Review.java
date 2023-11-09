package com.thundertaste.recipesite.review;

import com.thundertaste.recipesite.core.BaseEntity;
import com.thundertaste.recipesite.rating.Rating;
import jakarta.persistence.*;
import java.util.Date;


@Entity
@Table(name = "review")
public class Review extends BaseEntity {

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private Long recipeID;

    @Column(nullable = false, length = 2000)
    private String text;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date datePosted;

    @ManyToOne
    @JoinColumn(name = "rating_id", referencedColumnName = "ratingid")
    private Rating rating;

    protected Review(){
        super();
    }

    // Static factory method to create a new Review instance
    public static Review createReview() {
        return new Review();
    }

    public Review(Long userID, Long recipeID, String text, Date datePosted, Rating rating) {
        this.userID = userID;
        this.recipeID = recipeID;
        this.text = text;
        this.datePosted = datePosted;
        this.rating = rating;
    }

    // Getter, Setter methods, and other required methods.

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

}
