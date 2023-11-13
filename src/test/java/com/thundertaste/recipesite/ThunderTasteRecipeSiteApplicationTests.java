package com.thundertaste.recipesite;

import com.thundertaste.recipesite.category.Category;
import com.thundertaste.recipesite.recipe.Recipe;
import com.thundertaste.recipesite.recipe.RecipeRepository;
import com.thundertaste.recipesite.recipe.RecipeService;
import com.thundertaste.recipesite.user.User;
import com.thundertaste.recipesite.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ThunderTasteRecipeSiteApplicationTests {

	void contextLoads() {
		// This test ensures that the Spring context loads successfully.
	}

	@Mock
	private RecipeRepository recipeRepository; // Mocking the RecipeRepository

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private RecipeService recipeService; // Injecting the mock into RecipeService

	private Recipe dummyRecipe; // Dummy Recipe object for testing

	@BeforeEach
	void setUp() {
		// Setting up the dummy Recipe object with test data
		String title = "Test Recipe";
		String description = "Test Description";
		List<String> ingredients = Arrays.asList("Ingredient 1", "Ingredient 2");
		List<String> steps = Arrays.asList("Step 1", "Step 2");
		String image = "test_image.jpg";
		Category category = new Category(); // Assuming Category is a class you have - adjust as necessary
		String prepTime = "30 mins";
		String cookTime = "1 hour";
		int servingSize = 4;
		User author = new User(); // Assuming User is a class you have - adjust as necessary
		Date datePosted = new Date(); // Current date for the test

		dummyRecipe = new Recipe(title, description, ingredients, steps, image, category, prepTime, cookTime, servingSize, author, datePosted);

		MockitoAnnotations.openMocks(this);
	}

	@Test
	@DisplayName("Test Get Newest Recipes")
	void testGetNewestRecipes() {
		// Testing getNewestRecipes method
		when(recipeRepository.findTop6ByOrderByIdDesc()).thenReturn(Arrays.asList(dummyRecipe));
		List<Recipe> recipes = recipeService.getNewestRecipes();
		assertFalse(recipes.isEmpty());
		assertEquals(dummyRecipe, recipes.get(0));
	}

	@Test
	@DisplayName("Test Find Recipe by ID")
	void testFindById() {
		// Testing findById method
		when(recipeRepository.findById(1L)).thenReturn(Optional.of(dummyRecipe));
		Optional<Recipe> recipe = recipeService.findById(1L);
		assertTrue(recipe.isPresent());
		assertEquals(dummyRecipe, recipe.get());
	}

	@ParameterizedTest
	@ValueSource(strings = {"Test", "Recipe", ""})
	@DisplayName("Test Search Recipes")
	void testSearchRecipes(String query) {
		// Testing searchRecipes method with different query parameters
		when(recipeRepository.findByTitleContaining(query)).thenReturn(Arrays.asList(dummyRecipe));
		List<Recipe> recipes = recipeService.searchRecipes(query);
		assertFalse(recipes.isEmpty());
		assertEquals(dummyRecipe, recipes.get(0));
	}

	@Test
	@DisplayName("Test Save Recipe")
	void testSaveRecipe() {
		// Testing saveRecipe method
		when(recipeRepository.save(dummyRecipe)).thenReturn(dummyRecipe);
		Recipe savedRecipe = recipeService.save(dummyRecipe);
		assertNotNull(savedRecipe);
		assertEquals(dummyRecipe, savedRecipe);
	}

	@Test
	@DisplayName("Test Find Recipes by User ID")
	void testFindRecipesByUserId() {
		// Testing findRecipesByUserId method
		Long userId = 123L;
		when(recipeRepository.findByAuthorUserID(userId)).thenReturn(Arrays.asList(dummyRecipe));
		List<Recipe> recipes = recipeService.findRecipesByUserId(userId);
		assertFalse(recipes.isEmpty());
		assertEquals(dummyRecipe, recipes.get(0));
	}

	@Test
	@DisplayName("Test Find By Username")
	void testFindByUsername() {
		// values for the constructor parameters
		String username = "testUser";
		String email = "test@example.com";
		String passwordHash = "dummyHash123";
		String profileImage = "path/to/dummy/image.jpg";
		Date joinedDate = new Date(); // Current date for the test
		String bio = "This is a bio for the test user.";

		User dummyUser = new User(username, email, passwordHash, profileImage, joinedDate, bio);


		when(userRepository.findByUsername(username)).thenReturn(Optional.of(dummyUser));

		Optional<User> foundUser = userRepository.findByUsername(username);

		assertTrue(foundUser.isPresent());
		assertEquals(username, foundUser.get().getUsername());
	}

	@Test
	@DisplayName("Test Find By Email")
	void testFindByEmail() {
		String email = "test@example.com";
		User dummyUser = new User();
		dummyUser.setEmail(email);
		// Set other properties of dummyUser as needed

		when(userRepository.findByEmail(email)).thenReturn(Optional.of(dummyUser));

		Optional<User> foundUser = userRepository.findByEmail(email);

		assertTrue(foundUser.isPresent());
		assertEquals(email, foundUser.get().getEmail());
	}

	@Test
	@DisplayName("Test Exists By Username")
	void testExistsByUsername() {
		String username = "existingUser";

		when(userRepository.existsByUsername(username)).thenReturn(true);

		boolean exists = userRepository.existsByUsername(username);

		assertTrue(exists);
	}

	@Test
	@DisplayName("Test Exists By Email")
	void testExistsByEmail() {
		String email = "existing@example.com";

		when(userRepository.existsByEmail(email)).thenReturn(true);

		boolean exists = userRepository.existsByEmail(email);

		assertTrue(exists);
	}

}
