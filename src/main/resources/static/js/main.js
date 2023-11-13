document.addEventListener("DOMContentLoaded", function() {
    // Delegate the event to the parent of dynamically loaded favorite icons
    document.querySelector('main').addEventListener('click', function(event) {
        // Check if the clicked element or its ancestor is a Fave-icon
        const faveIcon = event.target.closest('.Fave-icon');
        if (faveIcon) {
            event.stopPropagation(); // Prevent event from bubbling up

            // Get the recipe ID from the closest recipe-card data attribute
            const recipeId = faveIcon.closest('.recipe-card').getAttribute('data-recipe-id');

            // Call the toggleFavorite function
            toggleFavorite(recipeId);
        }else {
            // Handle clicks on the recipe card but outside the favorite icon
            const recipeCard = event.target.closest('.recipe-card');
            if (recipeCard) {
                navigateToRecipe(recipeCard.getAttribute('data-recipe-id'));
            }
        }
    });
});
function toggleFavorite(recipeId) {
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
}

function navigateToRecipe(recipeId) {
    window.location.href = `/recipe/${recipeId}`;
}
