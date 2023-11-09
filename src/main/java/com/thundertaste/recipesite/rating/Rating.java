package com.thundertaste.recipesite.rating;

import com.thundertaste.recipesite.category.Category;
import com.thundertaste.recipesite.core.BaseEntity;
import com.thundertaste.recipesite.user.User;
import jakarta.persistence.*;


import java.util.Date;


@Entity
@Table(name = "rating")
public class Rating  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ratingid", unique = true, nullable = false)
    private Long ratingID;

    @Column(nullable = false)
    private Long userID;

    @Column(nullable = false)
    private Long recipeID;

    @Column(nullable = false)
    private int score;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateGiven;

    public Rating(Long userID, Long recipeID, int score, Date dateGiven) {
        this.userID = userID;
        this.recipeID = recipeID;
        this.score = score;
        this.dateGiven = dateGiven;
    }

    protected Rating() {
        this.ratingID = null;
    }

    // Static factory method to create a new Rating instance
    public static Rating createRating() {
        return new Rating();
    }

    // Getter, Setter methods, and other required methods.
    public Long getRatingID() {
        return ratingID;
    }

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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDateGiven() {
        return dateGiven;
    }

    public void setDateGiven(Date dateGiven) {
        this.dateGiven = dateGiven;
    }


}



