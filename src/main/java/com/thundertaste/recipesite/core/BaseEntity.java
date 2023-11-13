package com.thundertaste.recipesite.core;

import jakarta.persistence.*;


@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    protected BaseEntity() {
        this.id = null;
    }

    public Long getId() {
        return id;
    }
}
