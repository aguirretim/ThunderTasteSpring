document.addEventListener("DOMContentLoaded", function () {



        const personalRecipesDiv = document.getElementById('personal-recipes');
        const favoritedRecipesDiv = document.getElementById('favorited-recipes');

        // Event listener for toggle buttons
        const recipeTypeInputs = document.querySelectorAll('input[name="recipeType"]');
        recipeTypeInputs.forEach(input => {
            input.addEventListener('change', function () {
                console.log("Radio button changed:", this.id);
                if (this.id === 'savedRecipes') {
                    personalRecipesDiv.style.display = 'none';
                    favoritedRecipesDiv.style.display = 'flex';
                } else {
                    personalRecipesDiv.style.display = 'flex';
                    favoritedRecipesDiv.style.display = 'none';
                }
            });
        });




/*
    // Event listener for recipe cards
    const recipeCards = document.querySelectorAll('.recipe-card');
    recipeCards.forEach(card => {
        card.addEventListener('click', function (event) {
            if (!event.target.closest('.Fave-icon')) {
                // Navigate to the recipe's detailed view
                window.location.href = this.getAttribute('data-url');
            }
        });
    });
*/


    // Event delegation for remove icon
    document.querySelector('main').addEventListener('click', function(event) {
        const removeIcon = event.target.closest('.remove-icon');
        if (removeIcon) {
            event.stopPropagation(); // Prevent event from bubbling up

            // Extract recipe ID correctly
            const recipeCard = removeIcon.closest('.recipe-card');
            const recipeUrl = recipeCard.getAttribute('data-url');
            const recipeId = recipeUrl.split('/').pop(); // Assuming the last segment of the URL is the recipe ID

            // Call the removeRecipe function
            removeRecipe(recipeId, recipeCard);
        } else {
            // Handle other clicks
            const recipeCard = event.target.closest('.recipe-card');
            if (recipeCard && !event.target.closest('.Fave-icon')) {
                navigateToRecipe(recipeCard.getAttribute('data-url'));
            }
        }
    });

   /* // Event listener for the toggle buttons
    const recipeTypeInputs = document.querySelectorAll('input[name="recipeType"]');
    recipeTypeInputs.forEach(input => {
        input.addEventListener('change', function () {
            const show = this.id === 'savedRecipes' ? { hide: '.multi-menu-icon', show: '.remove-icon' } : { hide: '.remove-icon', show: '.multi-menu-icon' };
            recipeCards.forEach(card => {
                card.querySelector(show.hide).style.display = 'none';
                card.querySelector(show.show).style.display = 'block';
            });
        });
    });*/

    // Event listener for button group toggles
    const btnGroupToggleButtons = document.querySelectorAll('.btn-group-toggle .btn');
    btnGroupToggleButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            // Custom behavior when a button is clicked
            console.log('Button clicked:', this);

            // Remove active class from all buttons
            btnGroupToggleButtons.forEach(b => b.classList.remove('active'));

            // Add active class to clicked button
            this.classList.add('active');
        });
    });


});
/*function removeRecipeFromFavorites(recipeId) {

    // Include CSRF token in the request
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`/toggle-favorite/${recipeId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken  // Include CSRF token in the request header
        }
    })
        .then(response => response.json())
        .then(data => {
            const icon = document.querySelector(`.recipe-card[data-recipe-id="${recipeId}"] .Fave-icon`);
            if (data.isFavorite) {
                // Change icon to indicate it's a favorite
                icon.classList.add('favorite-active'); // Add a class that changes the icon's appearance
            } else {
                // Change icon to indicate it's not a favorite
                icon.classList.remove('favorite-active'); // Remove the class to revert appearance
            }
        })
        .catch(error => console.error('Error:', error));
}*/


function removeRecipe(recipeId, recipeCardElement) {
    // CSRF token inclusion as in your existing code
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`/toggle-favorite/${recipeId}`, { // Update the endpoint if needed
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        }
    })
        .then(response => response.json())
        .then(data => {
            // Assuming the server sends back a field indicating success or failure
            if (data.success || data.isFavorite === false) { // Adjust this condition based on actual server response
                // Remove the recipe card element from the DOM
                recipeCardElement.remove();
            } else {
                // handle the case where removing the recipe was not successful
                console.error('Failed to remove recipe:', data.message);
            }
        })
        .catch(error => console.error('Error:', error));
}

function navigateToRecipe(recipeId) {
    window.location.href = `${recipeId}`;
}
