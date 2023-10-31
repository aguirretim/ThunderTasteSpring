package com.thundertaste.recipesite.core;


import com.thundertaste.recipesite.recipe.Recipe;
import com.thundertaste.recipesite.recipe.RecipeRepository;
import com.thundertaste.recipesite.recipe.RecipeService;
import com.thundertaste.recipesite.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Slf4j
public class PageController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeService recipeService;

    @GetMapping({"/", "/home"})
    public String displayHome(Model model) {
        List<Recipe> recipes = recipeService.getNewestRecipes();
        model.addAttribute("recipes", recipes);
        return "index";
    }
    // Add a method to handle the home page request and list recipes
    @GetMapping("/index")
    public String homePage(Model model) {
        List<Recipe> recipes = recipeService.getNewestRecipes();
        model.addAttribute("recipes", recipes);
        return "index"; // name of your template
    }




    @GetMapping("/my-recipes")
    public String displayMyRecipes() {
        return "my-recipes";
    }

    @GetMapping("/recipe-search")
    public String browseRecipes() {
        return "recipe-search"; // This refers to 'recipe-search.html' in the 'templates' directory
    }




}
