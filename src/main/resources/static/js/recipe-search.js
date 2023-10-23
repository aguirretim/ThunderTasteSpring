document.addEventListener("DOMContentLoaded", function() {
    // Listen for clicks on any recipe-card
    document.querySelectorAll('.recipe-card').forEach(card => {
        card.addEventListener('click', function(event) {
            // Check if the clicked element or its parent is the Fave-icon
            if (event.target.closest('.Fave-icon')) {
                // Don't navigate if the Fave-icon or its children were clicked
                return;
            }

            // Navigate to recipe-view.html
            window.location = 'recipe-view.html';
        });
    });
});
