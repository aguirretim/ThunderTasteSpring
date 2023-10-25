package com.thundertaste.recipesite.recipe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;

    public List<Recipe> getNewestRecipes() {
        return recipeRepository.findTop6ByOrderByIdDesc();
    }



}
