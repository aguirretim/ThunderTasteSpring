document.addEventListener("DOMContentLoaded", function() {
    document.querySelector('main').addEventListener('click', function(event) {




        const faveIcon = event.target.closest('.Fave-icon');
        if (faveIcon) {
            event.stopPropagation(); // Prevent event from bubbling up
            console.log('Fave icon clicked', faveIcon);
            const recipeCard = faveIcon.closest('.recipe-card');
            console.log('Recipe card found', recipeCard);
            if (recipeCard) {
                const recipeId = recipeCard.getAttribute('data-recipe-id');
                console.log('Recipe ID found', recipeId);
                if (recipeId) {
                    toggleFavorite(recipeId);
                } else {
                    console.error('No recipe ID found for the clicked favorite icon');
                }
            } else {
                console.error('Clicked favorite icon is not inside a recipe card');
            }
        }
    });
});




function toggleFavorite(recipeId) {

    if (!recipeId) {
        console.error('Recipe ID is not defined.');
        return;
    }


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
        .then(response => {
            console.log('Response received:', response);
            return response.json();
        })
        .then(data => {
            console.log('Data received:', data);
            const iconWrapper = document.querySelector(`.recipe-card[data-recipe-id="${recipeId}"] .recipe-image-wrapper .Fave-icon`);
            console.log('Icon Wrapper:', iconWrapper);

            if (iconWrapper) {
                // ... existing code ...
            } else {
                console.error(`Icon wrapper not found for recipe ID: ${recipeId}`);
            }

            if (data.isFavorite) {
                // Change icon to indicate it's a favorite
                iconWrapper.classList.add('favorite-active'); // Add a class that changes the icon's appearance
            } else {
                // Change icon to indicate it's not a favorite
                iconWrapper.classList.remove('favorite-active'); // Remove the class to revert appearance
            }
        })
        .catch(error => console.error('Error:', error));
}

function navigateToRecipe(recipeId) {
    window.location.href = `/recipe/${recipeId}`;
}
