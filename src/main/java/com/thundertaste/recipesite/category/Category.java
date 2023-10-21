package com.thundertaste.recipesite.category;
import jakarta.persistence.*;


@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryID;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private String image;


    // Constructors, getters, setters, and other methods...

}

