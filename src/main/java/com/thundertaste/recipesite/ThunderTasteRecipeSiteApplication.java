package com.thundertaste.recipesite;

import com.thundertaste.recipesite.user.User;
import com.thundertaste.recipesite.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@SpringBootApplication
public class ThunderTasteRecipeSiteApplication  {


	public static void main(String[] args) {
		SpringApplication.run(ThunderTasteRecipeSiteApplication.class, args);
	}



}
