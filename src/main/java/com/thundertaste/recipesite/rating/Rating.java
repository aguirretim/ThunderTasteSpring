package com.thundertaste.recipesite.rating;

import com.thundertaste.recipesite.category.Category;
import com.thundertaste.recipesite.core.BaseEntity;
import com.thundertaste.recipesite.user.User;
import jakarta.persistence.*;


import java.util.Date;


@Entity
@Table(name = "rating")
public class Rating extends BaseEntity {

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

    // Getter, Setter methods, and other required methods.
}



