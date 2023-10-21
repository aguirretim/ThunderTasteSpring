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

    @Column(nullable = false)
    private Long recipeID;

    @Column(nullable = false, length = 2000) // Assuming comment text max length to be 2000. Adjust as needed.
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
    // Getter, Setter methods, and other required methods.

}
