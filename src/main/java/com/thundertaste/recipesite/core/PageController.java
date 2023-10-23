package com.thundertaste.recipesite.core;


import com.thundertaste.recipesite.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class PageController {

    @GetMapping("/home")
    public String displayHome() {
        return "index";
    }
    @GetMapping("/index")
    public String displayindex() {
        return "index";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup"; // Name of the Thymeleaf template
    }

    @GetMapping("/my-account")
    public String displayMyAcctDetails() {
        return "my-account";
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
